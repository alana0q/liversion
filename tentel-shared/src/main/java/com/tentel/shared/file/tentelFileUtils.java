package com.tentel.shared.file;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.tentel.shared.logger.Logger;
import com.tentel.shared.markdown.MarkdownUtils;
import com.tentel.shared.models.ExecutionCommand;
import com.tentel.shared.models.errors.Error;
import com.tentel.shared.models.errors.FileUtilsErrno;
import com.tentel.shared.shell.tentelShellEnvironmentClient;
import com.tentel.shared.shell.tentelTask;
import com.tentel.shared.tentel.AndroidUtils;
import com.tentel.shared.tentel.tentelConstants;
import com.tentel.shared.tentel.tentelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class tentelFileUtils {

    private static final String LOG_TAG = "tentelFileUtils";

    /**
     * Replace "$PREFIX/" or "~/" prefix with tentel absolute paths.
     *
     * @param paths The {@code paths} to expand.
     * @return Returns the {@code expand paths}.
     */
    public static List<String> getExpandedtentelPaths(List<String> paths) {
        if (paths == null) return null;
        List<String> expandedPaths = new ArrayList<>();

        for (int i = 0; i < paths.size(); i++) {
            expandedPaths.add(getExpandedtentelPath(paths.get(i)));
        }

        return expandedPaths;
    }

    /**
     * Replace "$PREFIX/" or "~/" prefix with tentel absolute paths.
     *
     * @param path The {@code path} to expand.
     * @return Returns the {@code expand path}.
     */
    public static String getExpandedtentelPath(String path) {
        if (path != null && !path.isEmpty()) {
            path = path.replaceAll("^\\$PREFIX$", tentelConstants.tentel_PREFIX_DIR_PATH);
            path = path.replaceAll("^\\$PREFIX/", tentelConstants.tentel_PREFIX_DIR_PATH + "/");
            path = path.replaceAll("^~/$", tentelConstants.tentel_HOME_DIR_PATH);
            path = path.replaceAll("^~/", tentelConstants.tentel_HOME_DIR_PATH + "/");
        }

        return path;
    }

    /**
     * Replace tentel absolute paths with "$PREFIX/" or "~/" prefix.
     *
     * @param paths The {@code paths} to unexpand.
     * @return Returns the {@code unexpand paths}.
     */
    public static List<String> getUnExpandedtentelPaths(List<String> paths) {
        if (paths == null) return null;
        List<String> unExpandedPaths = new ArrayList<>();

        for (int i = 0; i < paths.size(); i++) {
            unExpandedPaths.add(getUnExpandedtentelPath(paths.get(i)));
        }

        return unExpandedPaths;
    }

    /**
     * Replace tentel absolute paths with "$PREFIX/" or "~/" prefix.
     *
     * @param path The {@code path} to unexpand.
     * @return Returns the {@code unexpand path}.
     */
    public static String getUnExpandedtentelPath(String path) {
        if (path != null && !path.isEmpty()) {
            path = path.replaceAll("^" + Pattern.quote(tentelConstants.tentel_PREFIX_DIR_PATH) + "/", "\\$PREFIX/");
            path = path.replaceAll("^" + Pattern.quote(tentelConstants.tentel_HOME_DIR_PATH) + "/", "~/");
        }

        return path;
    }

    /**
     * Get canonical path.
     *
     * @param path The {@code path} to convert.
     * @param prefixForNonAbsolutePath Optional prefix path to prefix before non-absolute paths. This
     *                                 can be set to {@code null} if non-absolute paths should
     *                                 be prefixed with "/". The call to {@link File#getCanonicalPath()}
     *                                 will automatically do this anyways.
     * @param expandPath The {@code boolean} that decides if input path is first attempted to be expanded by calling
     *                   {@link tentelFileUtils#getExpandedtentelPath(String)} before its passed to
     *                   {@link FileUtils#getCanonicalPath(String, String)}.

     * @return Returns the {@code canonical path}.
     */
    public static String getCanonicalPath(String path, final String prefixForNonAbsolutePath, final boolean expandPath) {
        if (path == null) path = "";

        if (expandPath)
            path = getExpandedtentelPath(path);

        return FileUtils.getCanonicalPath(path, prefixForNonAbsolutePath);
    }

    /**
     * Check if {@code path} is under the allowed tentel working directory paths. If it is, then
     * allowed parent path is returned.
     *
     * @param path The {@code path} to check.
     * @return Returns the allowed path if it {@code path} is under it, otherwise {@link tentelConstants#tentel_FILES_DIR_PATH}.
     */
    public static String getMatchedAllowedtentelWorkingDirectoryParentPathForPath(String path) {
        if (path == null || path.isEmpty()) return tentelConstants.tentel_FILES_DIR_PATH;

        if (path.startsWith(tentelConstants.tentel_STORAGE_HOME_DIR_PATH + "/")) {
            return tentelConstants.tentel_STORAGE_HOME_DIR_PATH;
        } if (path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath() + "/")) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else if (path.startsWith("/sdcard/")) {
            return "/sdcard";
        } else {
            return tentelConstants.tentel_FILES_DIR_PATH;
        }
    }

    /**
     * Validate the existence and permissions of directory file at path as a working directory for
     * tentel app.
     *
     * The creation of missing directory and setting of missing permissions will only be done if
     * {@code path} is under paths returned by {@link #getMatchedAllowedtentelWorkingDirectoryParentPathForPath(String)}.
     *
     * The permissions set to directory will be {@link FileUtils#APP_WORKING_DIRECTORY_PERMISSIONS}.
     *
     * @param label The optional label for the directory file. This can optionally be {@code null}.
     * @param filePath The {@code path} for file to validate or create. Symlinks will not be followed.
     * @param createDirectoryIfMissing The {@code boolean} that decides if directory file
     *                                 should be created if its missing.
     * @param setPermissions The {@code boolean} that decides if permissions are to be
     *                              automatically set defined by {@code permissionsToCheck}.
     * @param setMissingPermissionsOnly The {@code boolean} that decides if only missing permissions
     *                                  are to be set or if they should be overridden.
     * @param ignoreErrorsIfPathIsInParentDirPath The {@code boolean} that decides if existence
     *                                  and permission errors are to be ignored if path is
     *                                  in {@code parentDirPath}.
     * @param ignoreIfNotExecutable The {@code boolean} that decides if missing executable permission
     *                              error is to be ignored. This allows making an attempt to set
     *                              executable permissions, but ignoring if it fails.
     * @return Returns the {@code error} if path is not a directory file, failed to create it,
     * or validating permissions failed, otherwise {@code null}.
     */
    public static Error validateDirectoryFileExistenceAndPermissions(String label, final String filePath, final boolean createDirectoryIfMissing,
                                                                     final boolean setPermissions, final boolean setMissingPermissionsOnly,
                                                                     final boolean ignoreErrorsIfPathIsInParentDirPath, final boolean ignoreIfNotExecutable) {
        return FileUtils.validateDirectoryFileExistenceAndPermissions(label, filePath,
            tentelFileUtils.getMatchedAllowedtentelWorkingDirectoryParentPathForPath(filePath), createDirectoryIfMissing,
            FileUtils.APP_WORKING_DIRECTORY_PERMISSIONS, setPermissions, setMissingPermissionsOnly,
            ignoreErrorsIfPathIsInParentDirPath, ignoreIfNotExecutable);
    }

    /**
     * Validate if {@link tentelConstants#tentel_FILES_DIR_PATH} exists and has
     * {@link FileUtils#APP_WORKING_DIRECTORY_PERMISSIONS} permissions.
     *
     * This is required because binaries compiled for tentel are hard coded with
     * {@link tentelConstants#tentel_PREFIX_DIR_PATH} and the path must be accessible.
     *
     * The permissions set to directory will be {@link FileUtils#APP_WORKING_DIRECTORY_PERMISSIONS}.
     *
     * This function does not create the directory manually but by calling {@link Context#getFilesDir()}
     * so that android itself creates it. However, the call will not create its parent package
     * data directory `/data/user/0/[package_name]` if it does not already exist and a `logcat`
     * error will be logged by android.
     * {@code Failed to ensure /data/user/0/<package_name>/files: mkdir failed: ENOENT (No such file or directory)}
     * An android app normally can't create the package data directory since its parent `/data/user/0`
     * is owned by `system` user and is normally created at app install or update time and not at app startup.
     *
     * Note that the path returned by {@link Context#getFilesDir()} may
     * be under `/data/user/[id]/[package_name]` instead of `/data/data/[package_name]`
     * defined by default by {@link tentelConstants#tentel_FILES_DIR_PATH} where id will be 0 for
     * primary user and a higher number for other users/profiles. If app is running under work profile
     * or secondary user, then {@link tentelConstants#tentel_FILES_DIR_PATH} will not be accessible
     * and will not be automatically created, unless there is a bind mount from `/data/data` to
     * `/data/user/[id]`, ideally in the right namespace.
     * https://source.android.com/devices/tech/admin/multi-user
     *
     *
     * On Android version `<=10`, the `/data/user/0` is a symlink to `/data/data` directory.
     * https://cs.android.com/android/platform/superproject/+/android-10.0.0_r47:system/core/rootdir/init.rc;l=589
     * {@code
     * symlink /data/data /data/user/0
     * }
     *
     * {@code
     * /system/bin/ls -lhd /data/data /data/user/0
     * drwxrwx--x 179 system system 8.0K 2021-xx-xx xx:xx /data/data
     * lrwxrwxrwx   1 root   root     10 2021-xx-xx xx:xx /data/user/0 -> /data/data
     * }
     *
     * On Android version `>=11`, the `/data/data` directory is bind mounted at `/data/user/0`.
     * https://cs.android.com/android/platform/superproject/+/android-11.0.0_r40:system/core/rootdir/init.rc;l=705
     * https://cs.android.com/android/_/android/platform/system/core/+/3cca270e95ca8d8bc8b800e2b5d7da1825fd7100
     * {@code
     * # Unlink /data/user/0 if we previously symlink it to /data/data
     * rm /data/user/0
     *
     * # Bind mount /data/user/0 to /data/data
     * mkdir /data/user/0 0700 system system encryption=None
     * mount none /data/data /data/user/0 bind rec
     * }
     *
     * {@code
     * /system/bin/grep -E '( /data )|( /data/data )|( /data/user/[0-9]+ )' /proc/self/mountinfo 2>&1 | /system/bin/grep -v '/data_mirror' 2>&1
     * 87 32 253:5 / /data rw,nosuid,nodev,noatime shared:27 - ext4 /dev/block/dm-5 rw,seclabel,resgid=1065,errors=panic
     * 91 87 253:5 /data /data/user/0 rw,nosuid,nodev,noatime shared:27 - ext4 /dev/block/dm-5 rw,seclabel,resgid=1065,errors=panic
     * }
     *
     * The column 4 defines the root of the mount within the filesystem.
     * Basically, `/dev/block/dm-5/` is mounted at `/data` and `/dev/block/dm-5/data` is mounted at
     * `/data/user/0`.
     * https://www.kernel.org/doc/Documentation/filesystems/proc.txt (section 3.5)
     * https://www.kernel.org/doc/Documentation/filesystems/sharedsubtree.txt
     * https://unix.stackexchange.com/a/571959
     *
     *
     * Also note that running `/system/bin/ls -lhd /data/user/0/com.tentel` as secondary user will result
     * in `ls: /data/user/0/com.tentel: Permission denied` where `0` is primary user id but running
     * `/system/bin/ls -lhd /data/user/10/com.tentel` will result in
     * `drwx------ 6 u10_a149 u10_a149 4.0K 2021-xx-xx xx:xx /data/user/10/com.tentel` where `10` is
     * secondary user id. So can't stat directory (not contents) of primary user from secondary user
     * but can the other way around. However, this is happening on android 10 avd, but not on android
     * 11 avd.
     *
     * @param context The {@link Context} for operations.
     * @param createDirectoryIfMissing The {@code boolean} that decides if directory file
     *                                 should be created if its missing.
     * @param setMissingPermissions The {@code boolean} that decides if permissions are to be
     *                              automatically set.
     * @return Returns the {@code error} if path is not a directory file, failed to create it,
     * or validating permissions failed, otherwise {@code null}.
     */
    public static Error istentelFilesDirectoryAccessible(@NonNull final Context context, boolean createDirectoryIfMissing, boolean setMissingPermissions) {
        if (createDirectoryIfMissing)
            context.getFilesDir();

        if (!FileUtils.directoryFileExists(tentelConstants.tentel_FILES_DIR_PATH, true))
            return FileUtilsErrno.ERRNO_FILE_NOT_FOUND_AT_PATH.getError("tentel files directory", tentelConstants.tentel_FILES_DIR_PATH);

        if (setMissingPermissions)
            FileUtils.setMissingFilePermissions("tentel files directory", tentelConstants.tentel_FILES_DIR_PATH,
                FileUtils.APP_WORKING_DIRECTORY_PERMISSIONS);

        return FileUtils.checkMissingFilePermissions("tentel files directory", tentelConstants.tentel_FILES_DIR_PATH,
            FileUtils.APP_WORKING_DIRECTORY_PERMISSIONS, false);
    }

    /**
     * Validate if {@link tentelConstants#tentel_PREFIX_DIR_PATH} exists and has
     * {@link FileUtils#APP_WORKING_DIRECTORY_PERMISSIONS} permissions.
     * .
     *
     * The {@link tentelConstants#tentel_PREFIX_DIR_PATH} directory would not exist if tentel has
     * not been installed or the bootstrap setup has not been run or if it was deleted by the user.
     *
     * @param createDirectoryIfMissing The {@code boolean} that decides if directory file
     *                                 should be created if its missing.
     * @param setMissingPermissions The {@code boolean} that decides if permissions are to be
     *                              automatically set.
     * @return Returns the {@code error} if path is not a directory file, failed to create it,
     * or validating permissions failed, otherwise {@code null}.
     */
    public static Error istentelPrefixDirectoryAccessible(boolean createDirectoryIfMissing, boolean setMissingPermissions) {
           return FileUtils.validateDirectoryFileExistenceAndPermissions("tentel prefix directory", tentelConstants.tentel_PREFIX_DIR_PATH,
                null, createDirectoryIfMissing,
                FileUtils.APP_WORKING_DIRECTORY_PERMISSIONS, setMissingPermissions, true,
                false, false);
    }

    /**
     * Validate if {@link tentelConstants#tentel_STAGING_PREFIX_DIR_PATH} exists and has
     * {@link FileUtils#APP_WORKING_DIRECTORY_PERMISSIONS} permissions.
     *
     * @param createDirectoryIfMissing The {@code boolean} that decides if directory file
     *                                 should be created if its missing.
     * @param setMissingPermissions The {@code boolean} that decides if permissions are to be
     *                              automatically set.
     * @return Returns the {@code error} if path is not a directory file, failed to create it,
     * or validating permissions failed, otherwise {@code null}.
     */
    public static Error istentelPrefixStagingDirectoryAccessible(boolean createDirectoryIfMissing, boolean setMissingPermissions) {
        return FileUtils.validateDirectoryFileExistenceAndPermissions("tentel prefix staging directory", tentelConstants.tentel_STAGING_PREFIX_DIR_PATH,
            null, createDirectoryIfMissing,
            FileUtils.APP_WORKING_DIRECTORY_PERMISSIONS, setMissingPermissions, true,
            false, false);
    }

    /**
     * Get a markdown {@link String} for stat output for various tentel app files paths.
     *
     * @param context The context for operations.
     * @return Returns the markdown {@link String}.
     */
    public static String gettentelFilesStatMarkdownString(@NonNull final Context context) {
        Context tentelPackageContext = tentelUtils.gettentelPackageContext(context);
        if (tentelPackageContext == null) return null;

        // Also ensures that tentel files directory is created if it does not already exist
        String filesDir = tentelPackageContext.getFilesDir().getAbsolutePath();

        // Build script
        StringBuilder statScript = new StringBuilder();
        statScript
            .append("echo 'ls info:'\n")
            .append("/system/bin/ls -lhdZ")
            .append(" '/data/data'")
            .append(" '/data/user/0'")
            .append(" '" + tentelConstants.tentel_INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "'")
            .append(" '/data/user/0/" + tentelConstants.tentel_PACKAGE_NAME + "'")
            .append(" '" + tentelConstants.tentel_FILES_DIR_PATH + "'")
            .append(" '" + filesDir + "'")
            .append(" '/data/user/0/" + tentelConstants.tentel_PACKAGE_NAME + "/files'")
            .append(" '/data/user/" + tentelConstants.tentel_PACKAGE_NAME + "/files'")
            .append(" '" + tentelConstants.tentel_STAGING_PREFIX_DIR_PATH + "'")
            .append(" '" + tentelConstants.tentel_PREFIX_DIR_PATH + "'")
            .append(" '" + tentelConstants.tentel_HOME_DIR_PATH + "'")
            .append(" '" + tentelConstants.tentel_BIN_PREFIX_DIR_PATH + "/login'")
            .append(" 2>&1")
            .append("\necho; echo 'mount info:'\n")
            .append("/system/bin/grep -E '( /data )|( /data/data )|( /data/user/[0-9]+ )' /proc/self/mountinfo 2>&1 | /system/bin/grep -v '/data_mirror' 2>&1");

        // Run script
        ExecutionCommand executionCommand = new ExecutionCommand(1, "/system/bin/sh", null, statScript.toString() + "\n", "/", true, true);
        executionCommand.commandLabel = tentelConstants.tentel_APP_NAME + " Files Stat Command";
        executionCommand.backgroundCustomLogLevel = Logger.LOG_LEVEL_OFF;
        tentelTask tentelTask = tentelTask.execute(context, executionCommand, null, new tentelShellEnvironmentClient(), true);
        if (tentelTask == null || !executionCommand.isSuccessful()) {
            Logger.logErrorExtended(LOG_TAG, executionCommand.toString());
            return null;
        }

        // Build script output
        StringBuilder statOutput = new StringBuilder();
        statOutput.append("$ ").append(statScript.toString());
        statOutput.append("\n\n").append(executionCommand.resultData.stdout.toString());

        boolean stderrSet = !executionCommand.resultData.stderr.toString().isEmpty();
        if (executionCommand.resultData.exitCode != 0 || stderrSet) {
            Logger.logErrorExtended(LOG_TAG, executionCommand.toString());
            if (stderrSet)
                statOutput.append("\n").append(executionCommand.resultData.stderr.toString());
            statOutput.append("\n").append("exit code: ").append(executionCommand.resultData.exitCode.toString());
        }

        // Build markdown output
        StringBuilder markdownString = new StringBuilder();
        markdownString.append("## ").append(tentelConstants.tentel_APP_NAME).append(" Files Info\n\n");
        AndroidUtils.appendPropertyToMarkdown(markdownString,"tentel_REQUIRED_FILES_DIR_PATH ($PREFIX)", tentelConstants.tentel_FILES_DIR_PATH);
        AndroidUtils.appendPropertyToMarkdown(markdownString,"ANDROID_ASSIGNED_FILES_DIR_PATH", filesDir);
        markdownString.append("\n\n").append(MarkdownUtils.getMarkdownCodeForString(statOutput.toString(), true));
        markdownString.append("\n##\n");

        return markdownString.toString();
    }

}
