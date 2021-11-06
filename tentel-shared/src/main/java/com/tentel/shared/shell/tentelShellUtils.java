package com.tentel.shared.shell;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tentel.shared.models.errors.Error;
import com.tentel.shared.tentel.tentelConstants;
import com.tentel.shared.file.FileUtils;
import com.tentel.shared.logger.Logger;
import com.tentel.shared.packages.PackageUtils;
import com.tentel.shared.tentel.tentelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tentelShellUtils {

    public static String tentel_VERSION_NAME;
    public static String tentel_IS_DEBUGGABLE_BUILD;
    public static String tentel_APP_PID;
    public static String tentel_APK_RELEASE;

    public static String tentel_API_VERSION_NAME;

    public static String getDefaultWorkingDirectoryPath() {
        return tentelConstants.tentel_HOME_DIR_PATH;
    }

    public static String getDefaultBinPath() {
        return tentelConstants.tentel_BIN_PREFIX_DIR_PATH;
    }

    public static String[] buildEnvironment(Context currentPackageContext, boolean isFailSafe, String workingDirectory) {
        tentelConstants.tentel_HOME_DIR.mkdirs();

        if (workingDirectory == null || workingDirectory.isEmpty())
            workingDirectory = getDefaultWorkingDirectoryPath();

        List<String> environment = new ArrayList<>();

        loadtentelEnvVariables(currentPackageContext);

        if (tentel_VERSION_NAME != null)
            environment.add("tentel_VERSION=" + tentel_VERSION_NAME);
        if (tentel_IS_DEBUGGABLE_BUILD != null)
            environment.add("tentel_IS_DEBUGGABLE_BUILD=" + tentel_IS_DEBUGGABLE_BUILD);
        if (tentel_APP_PID != null)
            environment.add("tentel_APP_PID=" + tentel_APP_PID);
        if (tentel_APK_RELEASE != null)
            environment.add("tentel_APK_RELEASE=" + tentel_APK_RELEASE);

        if (tentel_API_VERSION_NAME != null)
            environment.add("tentel_API_VERSION=" + tentel_API_VERSION_NAME);

        environment.add("TERM=xterm-256color");
        environment.add("COLORTERM=truecolor");
        environment.add("HOME=" + tentelConstants.tentel_HOME_DIR_PATH);
        environment.add("PREFIX=" + tentelConstants.tentel_PREFIX_DIR_PATH);
        environment.add("BOOTCLASSPATH=" + System.getenv("BOOTCLASSPATH"));
        environment.add("ANDROID_ROOT=" + System.getenv("ANDROID_ROOT"));
        environment.add("ANDROID_DATA=" + System.getenv("ANDROID_DATA"));
        // EXTERNAL_STORAGE is needed for /system/bin/am to work on at least
        // Samsung S7 - see https://plus.google.com/110070148244138185604/posts/gp8Lk3aCGp3.
        environment.add("EXTERNAL_STORAGE=" + System.getenv("EXTERNAL_STORAGE"));

        // These variables are needed if running on Android 10 and higher.
        addToEnvIfPresent(environment, "ANDROID_ART_ROOT");
        addToEnvIfPresent(environment, "DEX2OATBOOTCLASSPATH");
        addToEnvIfPresent(environment, "ANDROID_I18N_ROOT");
        addToEnvIfPresent(environment, "ANDROID_RUNTIME_ROOT");
        addToEnvIfPresent(environment, "ANDROID_TZDATA_ROOT");

        if (isFailSafe) {
            // Keep the default path so that system binaries can be used in the failsafe session.
            environment.add("PATH= " + System.getenv("PATH"));
        } else {
            environment.add("LANG=en_US.UTF-8");
            environment.add("PATH=" + tentelConstants.tentel_BIN_PREFIX_DIR_PATH);
            environment.add("PWD=" + workingDirectory);
            environment.add("TMPDIR=" + tentelConstants.tentel_TMP_PREFIX_DIR_PATH);
        }

        return environment.toArray(new String[0]);
    }

    public static void addToEnvIfPresent(List<String> environment, String name) {
        String value = System.getenv(name);
        if (value != null) {
            environment.add(name + "=" + value);
        }
    }

    public static String[] setupProcessArgs(@NonNull String fileToExecute, String[] arguments) {
        // The file to execute may either be:
        // - An elf file, in which we execute it directly.
        // - A script file without shebang, which we execute with our standard shell $PREFIX/bin/sh instead of the
        //   system /system/bin/sh. The system shell may vary and may not work at all due to LD_LIBRARY_PATH.
        // - A file with shebang, which we try to handle with e.g. /bin/foo -> $PREFIX/bin/foo.
        String interpreter = null;
        try {
            File file = new File(fileToExecute);
            try (FileInputStream in = new FileInputStream(file)) {
                byte[] buffer = new byte[256];
                int bytesRead = in.read(buffer);
                if (bytesRead > 4) {
                    if (buffer[0] == 0x7F && buffer[1] == 'E' && buffer[2] == 'L' && buffer[3] == 'F') {
                        // Elf file, do nothing.
                    } else if (buffer[0] == '#' && buffer[1] == '!') {
                        // Try to parse shebang.
                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < bytesRead; i++) {
                            char c = (char) buffer[i];
                            if (c == ' ' || c == '\n') {
                                if (builder.length() == 0) {
                                    // Skip whitespace after shebang.
                                } else {
                                    // End of shebang.
                                    String executable = builder.toString();
                                    if (executable.startsWith("/usr") || executable.startsWith("/bin")) {
                                        String[] parts = executable.split("/");
                                        String binary = parts[parts.length - 1];
                                        interpreter = tentelConstants.tentel_BIN_PREFIX_DIR_PATH + "/" + binary;
                                    }
                                    break;
                                }
                            } else {
                                builder.append(c);
                            }
                        }
                    } else {
                        // No shebang and no ELF, use standard shell.
                        interpreter = tentelConstants.tentel_BIN_PREFIX_DIR_PATH + "/sh";
                    }
                }
            }
        } catch (IOException e) {
            // Ignore.
        }

        List<String> result = new ArrayList<>();
        if (interpreter != null) result.add(interpreter);
        result.add(fileToExecute);
        if (arguments != null) Collections.addAll(result, arguments);
        return result.toArray(new String[0]);
    }

    public static void cleartentelTMPDIR(boolean onlyIfExists) {
        if(onlyIfExists && !FileUtils.directoryFileExists(tentelConstants.tentel_TMP_PREFIX_DIR_PATH, false))
            return;

        Error error;
        error = FileUtils.clearDirectory("$TMPDIR", FileUtils.getCanonicalPath(tentelConstants.tentel_TMP_PREFIX_DIR_PATH, null));
        if (error != null) {
            Logger.logErrorExtended(error.toString());
        }
    }

    public static void loadtentelEnvVariables(Context currentPackageContext) {
        String tentelAPKReleaseOld = tentel_APK_RELEASE;
        tentel_VERSION_NAME = tentel_IS_DEBUGGABLE_BUILD = tentel_APP_PID = tentel_APK_RELEASE = null;

        // Check if tentel app is installed and not disabled
        if (tentelUtils.istentelAppInstalled(currentPackageContext) == null) {
            // This function may be called by a different package like a plugin, so we get version for tentel package via its context
            Context tentelPackageContext = tentelUtils.gettentelPackageContext(currentPackageContext);
            if (tentelPackageContext != null) {
                tentel_VERSION_NAME = PackageUtils.getVersionNameForPackage(tentelPackageContext);
                tentel_IS_DEBUGGABLE_BUILD = PackageUtils.isAppForPackageADebuggableBuild(tentelPackageContext) ? "1" : "0";

                tentel_APP_PID = tentelUtils.gettentelAppPID(currentPackageContext);

                // Getting APK signature is a slightly expensive operation, so do it only when needed
                if (tentelAPKReleaseOld == null) {
                    String signingCertificateSHA256Digest = PackageUtils.getSigningCertificateSHA256DigestForPackage(tentelPackageContext);
                    if (signingCertificateSHA256Digest != null)
                        tentel_APK_RELEASE = tentelUtils.getAPKRelease(signingCertificateSHA256Digest).replaceAll("[^a-zA-Z]", "_").toUpperCase();
                } else {
                    tentel_APK_RELEASE = tentelAPKReleaseOld;
                }
            }
        }


        tentel_API_VERSION_NAME = null;

        // Check if tentel:API app is installed and not disabled
        if (tentelUtils.istentelAPIAppInstalled(currentPackageContext) == null) {
            // This function may be called by a different package like a plugin, so we get version for tentel:API package via its context
            Context tentelAPIPackageContext = tentelUtils.gettentelAPIPackageContext(currentPackageContext);
            if (tentelAPIPackageContext != null)
                tentel_API_VERSION_NAME = PackageUtils.getVersionNameForPackage(tentelAPIPackageContext);
        }
    }

}
