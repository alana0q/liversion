package com.tentel.shared.crash;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tentel.shared.tentel.tentelConstants;
import com.tentel.shared.tentel.tentelUtils;

public class tentelCrashUtils implements CrashHandler.CrashHandlerClient {

    /**
     * Set default uncaught crash handler of current thread to {@link CrashHandler} for tentel app
     * and its plugin to log crashes at {@link tentelConstants#tentel_CRASH_LOG_FILE_PATH}.
     */
    public static void setCrashHandler(@NonNull final Context context) {
        CrashHandler.setCrashHandler(context, new tentelCrashUtils());
    }

    @NonNull
    @Override
    public String getCrashLogFilePath(Context context) {
        return tentelConstants.tentel_CRASH_LOG_FILE_PATH;
    }

    @Override
    public String getAppInfoMarkdownString(Context context) {
        return tentelUtils.getAppInfoMarkdownString(context, true);
    }

}
