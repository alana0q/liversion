package com.tentel.shared.settings.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tentel.shared.packages.PackageUtils;
import com.tentel.shared.tentel.tentelConstants;
import com.tentel.shared.settings.preferences.tentelPreferenceConstants.tentel_TASKER_APP;
import com.tentel.shared.logger.Logger;

public class tentelTaskerAppSharedPreferences {

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences mMultiProcessSharedPreferences;


    private static final String LOG_TAG = "tentelTaskerAppSharedPreferences";

    private  tentelTaskerAppSharedPreferences(@NonNull Context context) {
        mContext = context;
        mSharedPreferences = getPrivateSharedPreferences(mContext);
        mMultiProcessSharedPreferences = getPrivateAndMultiProcessSharedPreferences(mContext);
    }

    /**
     * Get the {@link Context} for a package name.
     *
     * @param context The {@link Context} to use to get the {@link Context} of the
     *                {@link tentelConstants#tentel_TASKER_PACKAGE_NAME}.
     * @return Returns the {@link tentelTaskerAppSharedPreferences}. This will {@code null} if an exception is raised.
     */
    @Nullable
    public static tentelTaskerAppSharedPreferences build(@NonNull final Context context) {
        Context tentelTaskerPackageContext = PackageUtils.getContextForPackage(context, tentelConstants.tentel_TASKER_PACKAGE_NAME);
        if (tentelTaskerPackageContext == null)
            return null;
        else
            return new  tentelTaskerAppSharedPreferences(tentelTaskerPackageContext);
    }

    /**
     * Get the {@link Context} for a package name.
     *
     * @param context The {@link Activity} to use to get the {@link Context} of the
     *                {@link tentelConstants#tentel_TASKER_PACKAGE_NAME}.
     * @param exitAppOnError If {@code true} and failed to get package context, then a dialog will
     *                       be shown which when dismissed will exit the app.
     * @return Returns the {@link tentelTaskerAppSharedPreferences}. This will {@code null} if an exception is raised.
     */
    public static  tentelTaskerAppSharedPreferences build(@NonNull final Context context, final boolean exitAppOnError) {
        Context tentelTaskerPackageContext = PackageUtils.getContextForPackageOrExitApp(context, tentelConstants.tentel_TASKER_PACKAGE_NAME, exitAppOnError);
        if (tentelTaskerPackageContext == null)
            return null;
        else
            return new  tentelTaskerAppSharedPreferences(tentelTaskerPackageContext);
    }

    private static SharedPreferences getPrivateSharedPreferences(Context context) {
        if (context == null) return null;
        return SharedPreferenceUtils.getPrivateSharedPreferences(context, tentelConstants.tentel_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION);
    }

    private static SharedPreferences getPrivateAndMultiProcessSharedPreferences(Context context) {
        if (context == null) return null;
        return SharedPreferenceUtils.getPrivateAndMultiProcessSharedPreferences(context, tentelConstants.tentel_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION);
    }



    public int getLogLevel(boolean readFromFile) {
        if (readFromFile)
            return SharedPreferenceUtils.getInt(mMultiProcessSharedPreferences, tentel_TASKER_APP.KEY_LOG_LEVEL, Logger.DEFAULT_LOG_LEVEL);
        else
            return SharedPreferenceUtils.getInt(mSharedPreferences, tentel_TASKER_APP.KEY_LOG_LEVEL, Logger.DEFAULT_LOG_LEVEL);
    }

    public void setLogLevel(Context context, int logLevel, boolean commitToFile) {
        logLevel = Logger.setLogLevel(context, logLevel);
        SharedPreferenceUtils.setInt(mSharedPreferences, tentel_TASKER_APP.KEY_LOG_LEVEL, logLevel, commitToFile);
    }



    public int getLastPendingIntentRequestCode() {
        return SharedPreferenceUtils.getInt(mSharedPreferences, tentel_TASKER_APP.KEY_LAST_PENDING_INTENT_REQUEST_CODE, tentel_TASKER_APP.DEFAULT_VALUE_KEY_LAST_PENDING_INTENT_REQUEST_CODE);
    }

    public void setLastPendingIntentRequestCode(int lastPendingIntentRequestCode) {
        SharedPreferenceUtils.setInt(mSharedPreferences, tentel_TASKER_APP.KEY_LAST_PENDING_INTENT_REQUEST_CODE, lastPendingIntentRequestCode, false);
    }

}
