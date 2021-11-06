package com.tentel.shared.shell;

import android.content.Context;

import androidx.annotation.NonNull;

public class tentelShellEnvironmentClient implements ShellEnvironmentClient {

    @NonNull
    @Override
    public String getDefaultWorkingDirectoryPath() {
        return tentelShellUtils.getDefaultWorkingDirectoryPath();
    }

    @NonNull
    @Override
    public String getDefaultBinPath() {
        return tentelShellUtils.getDefaultBinPath();
    }

    @NonNull
    @Override
    public String[] buildEnvironment(Context currentPackageContext, boolean isFailSafe, String workingDirectory) {
        return tentelShellUtils.buildEnvironment(currentPackageContext, isFailSafe, workingDirectory);
    }

    @NonNull
    @Override
    public String[] setupProcessArgs(@NonNull String fileToExecute, String[] arguments) {
        return tentelShellUtils.setupProcessArgs(fileToExecute, arguments);
    }

}
