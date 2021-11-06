package com.tentel.shared.settings.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tentel.shared.logger.Logger;
import com.tentel.shared.packages.PackageUtils;
import com.tentel.shared.settings.preferences.tentelPreferenceConstants.tentel_BOOT_APP;
import com.tentel.shared.tentel.tentelConstants;

public class tentelBootAppSharedPreferences {

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences mMultiProcessSharedPreferences;


    private static final String LOG_TAG = "tentelBootAppSharedPreferences";

    private tentelBootAppSharedPreferences(@NonNull Context context) {
        mContext = context;
        mSharedPreferences = getPrivateSharedPreferences(mContext);
        mMultiProcessSharedPreferences = getPrivateAndMultiProcessSharedPreferences(mContext);
    }

    /**
     * Get the {@link Context} for a package name.
     *
     * @param context The {@link Context} to use to get the {@link Context} of the
     *                {@link tentelConstants#tentel_BOOT_PACKAGE_NAME}.
     * @return Returns the {@link tentelBootAppSharedPreferences}. This will {@code null} if an exception is raised.
     */
    @Nullable
    public static tentelBootAppSharedPreferences build(@NonNull final Context context) {
        Context tentelTaskerPackageContext = PackageUtils.getContextForPackage(context, tentelConstants.tentel_BOOT_PACKAGE_NAME);
        if (tentelTaskerPackageContext == null)
            return null;
        else
            return new tentelBootAppSharedPreferences(tentelTaskerPackageContext);
    }

    /**
     * Get the {@link Context} for a package name.
     *
     * @param context The {@link Activity} to use to get the {@link Context} of the
     *                {@link tentelConstants#tentel_BOOT_PACKAGE_NAME}.
     * @param exitAppOnError If {@code true} and failed to get package context, then a dialog will
     *                       be shown which when dismissed will exit the app.
     * @return Returns the {@link tentelBootAppSharedPreferences}. This will {@code null} if an exception is raised.
     */
    public static tentelBootAppSharedPreferences build(@NonNull final Context context, final boolean exitAppOnError) {
        Context tentelTaskerPackageContext = PackageUtils.getContextForPackageOrExitApp(context, tentelConstants.tentel_BOOT_PACKAGE_NAME, exitAppOnError);
        if (tentelTaskerPackageContext == null)
            return null;
        else
            return new tentelBootAppSharedPreferences(tentelTaskerPackageContext);
    }

    private static SharedPreferences getPrivateSharedPreferences(Context context) {
        if (context == null) return null;
        return SharedPreferenceUtils.getPrivateSharedPreferences(context, tentelConstants.tentel_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION);
    }

    private static SharedPreferences getPrivateAndMultiProcessSharedPreferences(Context context) {
        if (context == null) return null;
        return SharedPreferenceUtils.getPrivateAndMultiProcessSharedPreferences(context, tentelConstants.tentel_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION);
    }



    public int getLogLevel(boolean readFromFile) {
        if (readFromFile)
            return SharedPreferenceUtils.getInt(mMultiProcessSharedPreferences, tentel_BOOT_APP.KEY_LOG_LEVEL, Logger.DEFAULT_LOG_LEVEL);
        else
            return SharedPreferenceUtils.getInt(mSharedPreferences, tentel_BOOT_APP.KEY_LOG_LEVEL, Logger.DEFAULT_LOG_LEVEL);
    }

    public void setLogLevel(Context context, int logLevel, boolean commitToFile) {
        logLevel = Logger.setLogLevel(context, logLevel);
        SharedPreferenceUtils.setInt(mSharedPreferences, tentel_BOOT_APP.KEY_LOG_LEVEL, logLevel, commitToFile);
    }

}
