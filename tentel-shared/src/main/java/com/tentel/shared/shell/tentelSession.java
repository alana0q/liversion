package com.tentel.shared.shell;

import android.content.Context;
import android.system.OsConstants;

import androidx.annotation.NonNull;

import com.tentel.shared.R;
import com.tentel.shared.models.ExecutionCommand;
import com.tentel.shared.models.ResultData;
import com.tentel.shared.models.errors.Errno;
import com.tentel.shared.logger.Logger;
import com.tentel.terminal.TerminalSession;
import com.tentel.terminal.TerminalSessionClient;

import java.io.File;

/**
 * A class that maintains info for foreground tentel sessions.
 * It also provides a way to link each {@link TerminalSession} with the {@link ExecutionCommand}
 * that started it.
 */
public class tentelSession {

    private final TerminalSession mTerminalSession;
    private final ExecutionCommand mExecutionCommand;
    private final tentelSessionClient mtentelSessionClient;
    private final boolean mSetStdoutOnExit;

    private static final String LOG_TAG = "tentelSession";

    private tentelSession(@NonNull final TerminalSession terminalSession, @NonNull final ExecutionCommand executionCommand,
                          final tentelSessionClient tentelSessionClient, final boolean setStdoutOnExit) {
        this.mTerminalSession = terminalSession;
        this.mExecutionCommand = executionCommand;
        this.mtentelSessionClient = tentelSessionClient;
        this.mSetStdoutOnExit = setStdoutOnExit;
    }

    /**
     * Start execution of an {@link ExecutionCommand} with {@link Runtime#exec(String[], String[], File)}.
     *
     * The {@link ExecutionCommand#executable}, must be set, {@link ExecutionCommand#commandLabel},
     * {@link ExecutionCommand#arguments} and {@link ExecutionCommand#workingDirectory} may optionally
     * be set.
     *
     * If {@link ExecutionCommand#executable} is {@code null}, then a default shell is automatically
     * chosen.
     *
     * @param context The {@link Context} for operations.
     * @param executionCommand The {@link ExecutionCommand} containing the information for execution command.
     * @param terminalSessionClient The {@link TerminalSessionClient} interface implementation.
     * @param tentelSessionClient The {@link tentelSessionClient} interface implementation.
     * @param shellEnvironmentClient The {@link ShellEnvironmentClient} interface implementation.
     * @param sessionName The optional {@link TerminalSession} name.
     * @param setStdoutOnExit If set to {@code true}, then the {@link ResultData#stdout}
     *                        available in the {@link tentelSessionClient#ontentelSessionExited(tentelSession)}
     *                        callback will be set to the {@link TerminalSession} transcript. The session
     *                        transcript will contain both stdout and stderr combined, basically
     *                        anything sent to the the pseudo terminal /dev/pts, including PS1 prefixes.
     *                        Set this to {@code true} only if the session transcript is required,
     *                        since this requires extra processing to get it.
     * @return Returns the {@link tentelSession}. This will be {@code null} if failed to start the execution command.
     */
    public static tentelSession execute(@NonNull final Context context, @NonNull ExecutionCommand executionCommand,
                                        @NonNull final TerminalSessionClient terminalSessionClient, final tentelSessionClient tentelSessionClient,
                                        @NonNull final ShellEnvironmentClient shellEnvironmentClient,
                                        final String sessionName, final boolean setStdoutOnExit) {
        if (executionCommand.workingDirectory == null || executionCommand.workingDirectory.isEmpty())
            executionCommand.workingDirectory = shellEnvironmentClient.getDefaultWorkingDirectoryPath();
        if (executionCommand.workingDirectory.isEmpty())
            executionCommand.workingDirectory = "/";

        String[] environment = shellEnvironmentClient.buildEnvironment(context, executionCommand.isFailsafe, executionCommand.workingDirectory);

        String defaultBinPath = shellEnvironmentClient.getDefaultBinPath();
        if (defaultBinPath.isEmpty())
            defaultBinPath = "/system/bin";

        boolean isLoginShell = false;
        if (executionCommand.executable == null) {
            if (!executionCommand.isFailsafe) {
                for (String shellBinary : new String[]{"login", "bash", "zsh"}) {
                    File shellFile = new File(defaultBinPath, shellBinary);
                    if (shellFile.canExecute()) {
                        executionCommand.executable = shellFile.getAbsolutePath();
                        break;
                    }
                }
            }

            if (executionCommand.executable == null) {
                // Fall back to system shell as last resort:
                // Do not start a login shell since ~/.profile may cause startup failure if its invalid.
                // /system/bin/sh is provided by mksh (not toybox) and does load .mkshrc but for android its set
                // to /system/etc/mkshrc even though its default is ~/.mkshrc.
                // So /system/etc/mkshrc must still be valid for failsafe session to start properly.
                // https://cs.android.com/android/platform/superproject/+/android-11.0.0_r3:external/mksh/src/main.c;l=663
                // https://cs.android.com/android/platform/superproject/+/android-11.0.0_r3:external/mksh/src/main.c;l=41
                // https://cs.android.com/android/platform/superproject/+/android-11.0.0_r3:external/mksh/Android.bp;l=114
                executionCommand.executable = "/system/bin/sh";
            } else {
                isLoginShell = true;
            }

        }

        String[] processArgs = shellEnvironmentClient.setupProcessArgs(executionCommand.executable, executionCommand.arguments);

        executionCommand.executable = processArgs[0];
        String processName = (isLoginShell ? "-" : "") + ShellUtils.getExecutableBasename(executionCommand.executable);

        String[] arguments = new String[processArgs.length];
        arguments[0] = processName;
        if (processArgs.length > 1) System.arraycopy(processArgs, 1, arguments, 1, processArgs.length - 1);

        executionCommand.arguments = arguments;

        if (executionCommand.commandLabel == null)
            executionCommand.commandLabel = processName;

        if (!executionCommand.setState(ExecutionCommand.ExecutionState.EXECUTING)) {
            executionCommand.setStateFailed(Errno.ERRNO_FAILED.getCode(), context.getString(R.string.error_failed_to_execute_tentel_session_command, executionCommand.getCommandIdAndLabelLogString()));
            tentelSession.processtentelSessionResult(null, executionCommand);
            return null;
        }

        Logger.logDebugExtended(LOG_TAG, executionCommand.toString());

        Logger.logDebug(LOG_TAG, "Running \"" + executionCommand.getCommandIdAndLabelLogString() + "\" tentelSession");
        TerminalSession terminalSession = new TerminalSession(executionCommand.executable, executionCommand.workingDirectory, executionCommand.arguments, environment, executionCommand.terminalTranscriptRows, terminalSessionClient);

        if (sessionName != null) {
            terminalSession.mSessionName = sessionName;
        }

        return new tentelSession(terminalSession, executionCommand, tentelSessionClient, setStdoutOnExit);
    }

    /**
     * Signal that this {@link tentelSession} has finished.  This should be called when
     * {@link TerminalSessionClient#onSessionFinished(TerminalSession)} callback is received by the caller.
     *
     * If the processes has finished, then sets {@link ResultData#stdout}, {@link ResultData#stderr}
     * and {@link ResultData#exitCode} for the {@link #mExecutionCommand} of the {@code tentelTask}
     * and then calls {@link #processtentelSessionResult(tentelSession, ExecutionCommand)} to process the result}.
     *
     */
    public void finish() {
        // If process is still running, then ignore the call
        if (mTerminalSession.isRunning()) return;

        int exitCode = mTerminalSession.getExitStatus();

        if (exitCode == 0)
            Logger.logDebug(LOG_TAG, "The \"" + mExecutionCommand.getCommandIdAndLabelLogString() + "\" tentelSession exited normally");
        else
            Logger.logDebug(LOG_TAG, "The \"" + mExecutionCommand.getCommandIdAndLabelLogString() + "\" tentelSession exited with code: " + exitCode);

        // If the execution command has already failed, like SIGKILL was sent, then don't continue
        if (mExecutionCommand.isStateFailed()) {
            Logger.logDebug(LOG_TAG, "Ignoring setting \"" + mExecutionCommand.getCommandIdAndLabelLogString() + "\" tentelSession state to ExecutionState.EXECUTED and processing results since it has already failed");
            return;
        }

        mExecutionCommand.resultData.exitCode = exitCode;

        if (this.mSetStdoutOnExit)
            mExecutionCommand.resultData.stdout.append(ShellUtils.getTerminalSessionTranscriptText(mTerminalSession, true, false));

        if (!mExecutionCommand.setState(ExecutionCommand.ExecutionState.EXECUTED))
            return;

        tentelSession.processtentelSessionResult(this, null);
    }

    /**
     * Kill this {@link tentelSession} by sending a {@link OsConstants#SIGILL} to its {@link #mTerminalSession}
     * if its still executing.
     *
     * @param context The {@link Context} for operations.
     * @param processResult If set to {@code true}, then the {@link #processtentelSessionResult(tentelSession, ExecutionCommand)}
     *                      will be called to process the failure.
     */
    public void killIfExecuting(@NonNull final Context context, boolean processResult) {
        // If execution command has already finished executing, then no need to process results or send SIGKILL
        if (mExecutionCommand.hasExecuted()) {
            Logger.logDebug(LOG_TAG, "Ignoring sending SIGKILL to \"" + mExecutionCommand.getCommandIdAndLabelLogString() + "\" tentelSession since it has already finished executing");
            return;
        }

        Logger.logDebug(LOG_TAG, "Send SIGKILL to \"" + mExecutionCommand.getCommandIdAndLabelLogString() + "\" tentelSession");
        if (mExecutionCommand.setStateFailed(Errno.ERRNO_FAILED.getCode(), context.getString(R.string.error_sending_sigkill_to_process))) {
            if (processResult) {
                mExecutionCommand.resultData.exitCode = 137; // SIGKILL

                // Get whatever output has been set till now in case its needed
                if (this.mSetStdoutOnExit)
                    mExecutionCommand.resultData.stdout.append(ShellUtils.getTerminalSessionTranscriptText(mTerminalSession, true, false));

                tentelSession.processtentelSessionResult(this, null);
            }
        }

        // Send SIGKILL to process
        mTerminalSession.finishIfRunning();
    }

    /**
     * Process the results of {@link tentelSession} or {@link ExecutionCommand}.
     *
     * Only one of {@code tentelSession} and {@code executionCommand} must be set.
     *
     * If the {@code tentelSession} and its {@link #mtentelSessionClient} are not {@code null},
     * then the {@link tentelSession.tentelSessionClient#ontentelSessionExited(tentelSession)}
     * callback will be called.
     *
     * @param tentelSession The {@link tentelSession}, which should be set if
     *                  {@link #execute(Context, ExecutionCommand, TerminalSessionClient, tentelSessionClient, ShellEnvironmentClient, String, boolean)}
     *                   successfully started the process.
     * @param executionCommand The {@link ExecutionCommand}, which should be set if
     *                          {@link #execute(Context, ExecutionCommand, TerminalSessionClient, tentelSessionClient, ShellEnvironmentClient, String, boolean)}
     *                          failed to start the process.
     */
    private static void processtentelSessionResult(final tentelSession tentelSession, ExecutionCommand executionCommand) {
        if (tentelSession != null)
            executionCommand = tentelSession.mExecutionCommand;

        if (executionCommand == null) return;

        if (executionCommand.shouldNotProcessResults()) {
            Logger.logDebug(LOG_TAG, "Ignoring duplicate call to process \"" + executionCommand.getCommandIdAndLabelLogString() + "\" tentelSession result");
            return;
        }

        Logger.logDebug(LOG_TAG, "Processing \"" + executionCommand.getCommandIdAndLabelLogString() + "\" tentelSession result");

        if (tentelSession != null && tentelSession.mtentelSessionClient != null) {
            tentelSession.mtentelSessionClient.ontentelSessionExited(tentelSession);
        } else {
            // If a callback is not set and execution command didn't fail, then we set success state now
            // Otherwise, the callback host can set it himself when its done with the tentelSession
            if (!executionCommand.isStateFailed())
                executionCommand.setState(ExecutionCommand.ExecutionState.SUCCESS);
        }
    }

    public TerminalSession getTerminalSession() {
        return mTerminalSession;
    }

    public ExecutionCommand getExecutionCommand() {
        return mExecutionCommand;
    }



    public interface tentelSessionClient {

        /**
         * Callback function for when {@link tentelSession} exits.
         *
         * @param tentelSession The {@link tentelSession} that exited.
         */
        void ontentelSessionExited(tentelSession tentelSession);

    }

}
