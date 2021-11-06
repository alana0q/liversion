package com.tentel.shared.tentel;

import android.annotation.SuppressLint;

import com.tentel.shared.models.ResultConfig;
import com.tentel.shared.models.errors.Errno;

import java.io.File;
import java.util.Arrays;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.List;

/*
 * Version: v0.32.0
 *
 * Changelog
 *
 * - 0.1.0 (2021-03-08)
 *      - Initial Release.
 *
 * - 0.2.0 (2021-03-11)
 *      - Added `_DIR` and `_FILE` substrings to paths.
 *      - Added `INTERNAL_PRIVATE_APP_DATA_DIR*`, `tentel_CACHE_DIR*`, `tentel_DATABASES_DIR*`,
 *          `tentel_SHARED_PREFERENCES_DIR*`, `tentel_BIN_PREFIX_DIR*`, `tentel_ETC_DIR*`,
 *          `tentel_INCLUDE_DIR*`, `tentel_LIB_DIR*`, `tentel_LIBEXEC_DIR*`, `tentel_SHARE_DIR*`,
 *          `tentel_TMP_DIR*`, `tentel_VAR_DIR*`, `tentel_STAGING_PREFIX_DIR*`,
 *          `tentel_STORAGE_HOME_DIR*`, `tentel_DEFAULT_PREFERENCES_FILE_BASENAME*`,
 *          `tentel_DEFAULT_PREFERENCES_FILE`.
 *      - Renamed `DATA_HOME_PATH` to `tentel_DATA_HOME_DIR_PATH`.
 *      - Renamed `CONFIG_HOME_PATH` to `tentel_CONFIG_HOME_DIR_PATH`.
 *      - Updated javadocs and spacing.
 *
 * - 0.3.0 (2021-03-12)
 *      - Remove `tentel_CACHE_DIR_PATH*`, `tentel_DATABASES_DIR_PATH*`,
 *          `tentel_SHARED_PREFERENCES_DIR_PATH*` since they may not be consistent on all devices.
 *      - Renamed `tentel_DEFAULT_PREFERENCES_FILE_BASENAME` to
 *          `tentel_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`. This should be used for
 *           accessing shared preferences between tentel app and its plugins if ever needed by first
 *           getting shared package context with {@link Context.createPackageContext(String,int}).
 *
 * - 0.4.0 (2021-03-16)
 *      - Added `BROADCAST_tentel_OPENED`,
 *          `tentel_API_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`
 *          `tentel_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `tentel_FLOAT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `tentel_STYLING_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `tentel_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `tentel_WIDGET_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`.
 *
 * - 0.5.0 (2021-03-16)
 *      - Renamed "tentel Plugin app" labels to "tentel:Tasker app".
 *
 * - 0.6.0 (2021-03-16)
 *      - Added `tentel_FILE_SHARE_URI_AUTHORITY`.
 *
 * - 0.7.0 (2021-03-17)
 *      - Fixed javadocs.
 *
 * - 0.8.0 (2021-03-18)
 *      - Fixed Intent extra types javadocs.
 *      - Added following to `tentel_SERVICE`:
 *          `EXTRA_PENDING_INTENT`, `EXTRA_RESULT_BUNDLE`,
 *          `EXTRA_STDOUT`, `EXTRA_STDERR`, `EXTRA_EXIT_CODE`,
 *          `EXTRA_ERR`, `EXTRA_ERRMSG`.
 *
 * - 0.9.0 (2021-03-18)
 *      - Fixed javadocs.
 *
 * - 0.10.0 (2021-03-19)
 *      - Added following to `tentel_SERVICE`:
 *          `EXTRA_SESSION_ACTION`,
 *          `VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY`,
 *          `VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY`,
 *          `VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_DONT_OPEN_ACTIVITY`
 *          `VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY`.
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `EXTRA_SESSION_ACTION`.
 *
 * - 0.11.0 (2021-03-24)
 *      - Added following to `tentel_SERVICE`:
 *          `EXTRA_COMMAND_LABEL`, `EXTRA_COMMAND_DESCRIPTION`, `EXTRA_COMMAND_HELP`, `EXTRA_PLUGIN_API_HELP`.
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `EXTRA_COMMAND_LABEL`, `EXTRA_COMMAND_DESCRIPTION`, `EXTRA_COMMAND_HELP`.
 *      - Updated `RESULT_BUNDLE` related extras with `PLUGIN_RESULT_BUNDLE` prefixes.
 *
 * - 0.12.0 (2021-03-25)
 *      - Added following to `tentel_SERVICE`:
 *          `EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT_ORIGINAL_LENGTH`,
 *          `EXTRA_PLUGIN_RESULT_BUNDLE_STDERR_ORIGINAL_LENGTH`.
 *
 * - 0.13.0 (2021-03-25)
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `EXTRA_PENDING_INTENT`.
 *
 * - 0.14.0 (2021-03-25)
 *      - Added `FDROID_PACKAGES_BASE_URL`,
 *          `tentel_GITHUB_ORGANIZATION_NAME`, `tentel_GITHUB_ORGANIZATION_URL`,
 *          `tentel_GITHUB_REPO_NAME`, `tentel_GITHUB_REPO_URL`, `tentel_FDROID_PACKAGE_URL`,
 *          `tentel_API_GITHUB_REPO_NAME`,`tentel_API_GITHUB_REPO_URL`, `tentel_API_FDROID_PACKAGE_URL`,
 *          `tentel_BOOT_GITHUB_REPO_NAME`, `tentel_BOOT_GITHUB_REPO_URL`, `tentel_BOOT_FDROID_PACKAGE_URL`,
 *          `tentel_FLOAT_GITHUB_REPO_NAME`, `tentel_FLOAT_GITHUB_REPO_URL`, `tentel_FLOAT_FDROID_PACKAGE_URL`,
 *          `tentel_STYLING_GITHUB_REPO_NAME`, `tentel_STYLING_GITHUB_REPO_URL`, `tentel_STYLING_FDROID_PACKAGE_URL`,
 *          `tentel_TASKER_GITHUB_REPO_NAME`, `tentel_TASKER_GITHUB_REPO_URL`, `tentel_TASKER_FDROID_PACKAGE_URL`,
 *          `tentel_WIDGET_GITHUB_REPO_NAME`, `tentel_WIDGET_GITHUB_REPO_URL` `tentel_WIDGET_FDROID_PACKAGE_URL`.
 *
 * - 0.15.0 (2021-04-06)
 *      - Fixed some variables that had `PREFIX_` substring missing in their name.
 *      - Added `tentel_CRASH_LOG_FILE_PATH`, `tentel_CRASH_LOG_BACKUP_FILE_PATH`,
 *          `tentel_GITHUB_ISSUES_REPO_URL`, `tentel_API_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_BOOT_GITHUB_ISSUES_REPO_URL`, `tentel_FLOAT_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_STYLING_GITHUB_ISSUES_REPO_URL`, `tentel_TASKER_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_WIDGET_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_GITHUB_WIKI_REPO_URL`, `tentel_PACKAGES_GITHUB_WIKI_REPO_URL`,
 *          `tentel_PACKAGES_GITHUB_REPO_NAME`, `tentel_PACKAGES_GITHUB_REPO_URL`, `tentel_PACKAGES_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_GAME_PACKAGES_GITHUB_REPO_NAME`, `tentel_GAME_PACKAGES_GITHUB_REPO_URL`, `tentel_GAME_PACKAGES_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_SCIENCE_PACKAGES_GITHUB_REPO_NAME`, `tentel_SCIENCE_PACKAGES_GITHUB_REPO_URL`, `tentel_SCIENCE_PACKAGES_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_ROOT_PACKAGES_GITHUB_REPO_NAME`, `tentel_ROOT_PACKAGES_GITHUB_REPO_URL`, `tentel_ROOT_PACKAGES_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_UNSTABLE_PACKAGES_GITHUB_REPO_NAME`, `tentel_UNSTABLE_PACKAGES_GITHUB_REPO_URL`, `tentel_UNSTABLE_PACKAGES_GITHUB_ISSUES_REPO_URL`,
 *          `tentel_X11_PACKAGES_GITHUB_REPO_NAME`, `tentel_X11_PACKAGES_GITHUB_REPO_URL`, `tentel_X11_PACKAGES_GITHUB_ISSUES_REPO_URL`.
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `RUN_COMMAND_API_HELP_URL`.
 *
 * - 0.16.0 (2021-04-06)
 *      - Added `tentel_SUPPORT_EMAIL`, `tentel_SUPPORT_EMAIL_URL`, `tentel_SUPPORT_EMAIL_MAILTO_URL`,
 *          `tentel_REDDIT_SUBREDDIT`, `tentel_REDDIT_SUBREDDIT_URL`.
 *      - The `tentel_SUPPORT_EMAIL_URL` value must be fixed later when email has been set up.
 *
 * - 0.17.0 (2021-04-07)
 *      - Added `tentel_APP_NOTIFICATION_CHANNEL_ID`, `tentel_APP_NOTIFICATION_CHANNEL_NAME`, `tentel_APP_NOTIFICATION_ID`,
 *          `tentel_RUN_COMMAND_NOTIFICATION_CHANNEL_ID`, `tentel_RUN_COMMAND_NOTIFICATION_CHANNEL_NAME`, `tentel_RUN_COMMAND_NOTIFICATION_ID`,
 *          `tentel_PLUGIN_COMMAND_ERRORS_NOTIFICATION_CHANNEL_ID`, `tentel_PLUGIN_COMMAND_ERRORS_NOTIFICATION_CHANNEL_NAME`,
 *          `tentel_CRASH_REPORTS_NOTIFICATION_CHANNEL_ID`, `tentel_CRASH_REPORTS_NOTIFICATION_CHANNEL_NAME`.
 *      - Updated javadocs.
 *
 * - 0.18.0 (2021-04-11)
 *      - Updated `tentel_SUPPORT_EMAIL_URL` to a valid email.
 *      - Removed `tentel_SUPPORT_EMAIL`.
 *
 * - 0.19.0 (2021-04-12)
 *      - Added `tentel_ACTIVITY.ACTION_REQUEST_PERMISSIONS`.
 *      - Added `tentel_SERVICE.EXTRA_STDIN`.
 *      - Added `RUN_COMMAND_SERVICE.EXTRA_STDIN`.
 *      - Deprecated `tentel_ACTIVITY.EXTRA_RELOAD_STYLE`.
 *
 * - 0.20.0 (2021-05-13)
 *      - Added `tentel_WIKI`, `tentel_WIKI_URL`, `tentel_PLUGIN_APP_NAMES_LIST`, `tentel_PLUGIN_APP_PACKAGE_NAMES_LIST`.
 *      - Added `tentel_SETTINGS_ACTIVITY_NAME`.
 *
 * - 0.21.0 (2021-05-13)
 *      - Added `APK_RELEASE_FDROID`, `APK_RELEASE_FDROID_SIGNING_CERTIFICATE_SHA256_DIGEST`,
 *          `APK_RELEASE_GITHUB_DEBUG_BUILD`, `APK_RELEASE_GITHUB_DEBUG_BUILD_SIGNING_CERTIFICATE_SHA256_DIGEST`,
 *          `APK_RELEASE_GOOGLE_PLAYSTORE`, `APK_RELEASE_GOOGLE_PLAYSTORE_SIGNING_CERTIFICATE_SHA256_DIGEST`.
 *
 * - 0.22.0 (2021-05-13)
 *      - Added `tentel_DONATE_URL`.
 *
 * - 0.23.0 (2021-06-12)
 *      - Rename `INTERNAL_PRIVATE_APP_DATA_DIR_PATH` to `tentel_INTERNAL_PRIVATE_APP_DATA_DIR_PATH`.
 *
 * - 0.24.0 (2021-06-27)
 *      - Add `COMMA_NORMAL`, `COMMA_ALTERNATIVE`.
 *      - Added following to `tentel_APP.tentel_SERVICE`:
 *          `EXTRA_RESULT_DIRECTORY`, `EXTRA_RESULT_SINGLE_FILE`, `EXTRA_RESULT_FILE_BASENAME`,
 *          `EXTRA_RESULT_FILE_OUTPUT_FORMAT`, `EXTRA_RESULT_FILE_ERROR_FORMAT`, `EXTRA_RESULT_FILES_SUFFIX`.
 *      - Added following to `tentel_APP.RUN_COMMAND_SERVICE`:
 *          `EXTRA_RESULT_DIRECTORY`, `EXTRA_RESULT_SINGLE_FILE`, `EXTRA_RESULT_FILE_BASENAME`,
 *          `EXTRA_RESULT_FILE_OUTPUT_FORMAT`, `EXTRA_RESULT_FILE_ERROR_FORMAT`, `EXTRA_RESULT_FILES_SUFFIX`,
 *          `EXTRA_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS`, `EXTRA_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS`.
 *      - Added following to `RESULT_SENDER`:
 *           `FORMAT_SUCCESS_STDOUT`, `FORMAT_SUCCESS_STDOUT__EXIT_CODE`, `FORMAT_SUCCESS_STDOUT__STDERR__EXIT_CODE`
 *           `FORMAT_FAILED_ERR__ERRMSG__STDOUT__STDERR__EXIT_CODE`,
 *           `RESULT_FILE_ERR_PREFIX`, `RESULT_FILE_ERRMSG_PREFIX` `RESULT_FILE_STDOUT_PREFIX`,
 *           `RESULT_FILE_STDERR_PREFIX`, `RESULT_FILE_EXIT_CODE_PREFIX`.
 *
 * - 0.25.0 (2021-08-19)
 *      - Added following to `tentel_APP.tentel_SERVICE`:
 *          `EXTRA_BACKGROUND_CUSTOM_LOG_LEVEL`.
 *      - Added following to `tentel_APP.RUN_COMMAND_SERVICE`:
 *          `EXTRA_BACKGROUND_CUSTOM_LOG_LEVEL`.
 *
 * - 0.26.0 (2021-08-25)
 *      - Changed `tentel_ACTIVITY.ACTION_FAILSAFE_SESSION` to `tentel_ACTIVITY.EXTRA_FAILSAFE_SESSION`.
 *
 * - 0.27.0 (2021-09-02)
 *      - Added `tentel_FLOAT_APP_NOTIFICATION_CHANNEL_ID`, `tentel_FLOAT_APP_NOTIFICATION_CHANNEL_NAME`,
 *          `tentel_FLOAT_APP.tentel_FLOAT_SERVICE_NAME`.
 *      - Added following to `tentel_FLOAT_APP.tentel_FLOAT_SERVICE`:
 *          `ACTION_STOP_SERVICE`, `ACTION_SHOW`, `ACTION_HIDE`.
 *
 * - 0.28.0 (2021-09-02)
 *      - Added `tentel_FLOAT_PROPERTIES_PRIMARY_FILE*` and `tentel_FLOAT_PROPERTIES_SECONDARY_FILE*`.
 *
 * - 0.29.0 (2021-09-04)
 *      - Added `tentel_SHORTCUT_TASKS_SCRIPTS_DIR_BASENAME`, `tentel_SHORTCUT_SCRIPT_ICONS_DIR_BASENAME`,
 *          `tentel_SHORTCUT_SCRIPT_ICONS_DIR_PATH`, `tentel_SHORTCUT_SCRIPT_ICONS_DIR`.
 *      - Added following to `tentel_WIDGET.tentel_WIDGET_PROVIDER`:
 *          `ACTION_WIDGET_ITEM_CLICKED`, `ACTION_REFRESH_WIDGET`, `EXTRA_FILE_CLICKED`.
 *      - Changed naming convention of `tentel_FLOAT_APP.tentel_FLOAT_SERVICE.ACTION_*`.
 *      - Fixed wrong path set for `tentel_SHORTCUT_SCRIPTS_DIR_PATH`.
 *
 * - 0.30.0 (2021-09-08)
 *      - Changed `APK_RELEASE_GITHUB_DEBUG_BUILD`to `APK_RELEASE_GITHUB` and
 *          `APK_RELEASE_GITHUB_DEBUG_BUILD_SIGNING_CERTIFICATE_SHA256_DIGEST` to
 *          `APK_RELEASE_GITHUB_SIGNING_CERTIFICATE_SHA256_DIGEST`.
 *
 * - 0.31.0 (2021-09-09)
 *      - Added following to `tentel_APP.tentel_SERVICE`:
 *          `MIN_VALUE_EXTRA_SESSION_ACTION` and `MAX_VALUE_EXTRA_SESSION_ACTION`.
 *
 * - 0.32.0 (2021-09-23)
 *      - Added `tentel_API.tentel_API_ACTIVITY_NAME`, `tentel_TASKER.tentel_TASKER_ACTIVITY_NAME`
 *          and `tentel_WIDGET.tentel_WIDGET_ACTIVITY_NAME`.
 */

/**
 * A class that defines shared constants of the tentel app and its plugins.
 * This class will be hosted by tentel-shared lib and should be imported by other tentel plugin
 * apps as is instead of copying constants to random classes. The 3rd party apps can also import
 * it for interacting with tentel apps. If changes are made to this file, increment the version number
 * and add an entry in the Changelog section above.
 *
 * tentel app default package name is "com.tentel" and is used in {@link #tentel_PREFIX_DIR_PATH}.
 * The binaries compiled for tentel have {@link #tentel_PREFIX_DIR_PATH} hardcoded in them but it
 * can be changed during compilation.
 *
 * The {@link #tentel_PACKAGE_NAME} must be the same as the applicationId of tentel-app build.gradle
 * since its also used by {@link #tentel_FILES_DIR_PATH}.
 * If {@link #tentel_PACKAGE_NAME} is changed, then binaries, specially used in bootstrap need to be
 * compiled appropriately. Check https://github.com/tentel/tentel-packages/wiki/Building-packages
 * for more info.
 *
 * Ideally the only places where changes should be required if changing package name are the following:
 * - The {@link #tentel_PACKAGE_NAME} in {@link tentelConstants}.
 * - The "applicationId" in "build.gradle" of tentel-app. This is package name that android and app
 *      stores will use and is also the final package name stored in "AndroidManifest.xml".
 * - The "manifestPlaceholders" values for {@link #tentel_PACKAGE_NAME} and *_APP_NAME in
 *      "build.gradle" of tentel-app.
 * - The "ENTITY" values for {@link #tentel_PACKAGE_NAME} and *_APP_NAME in "strings.xml" of
 *      tentel-app and of tentel-shared.
 * - The "shortcut.xml" and "*_preferences.xml" files of tentel-app since dynamic variables don't
 *      work in it.
 * - Optionally the "package" in "AndroidManifest.xml" if modifying project structure of tentel-app.
 *      This is package name for java classes project structure and is prefixed if activity and service
 *      names use dot (.) notation. This is currently not advisable since this will break lot of
 *      stuff, including tentel-* packages.
 * - Optionally the *_PATH variables in {@link tentelConstants} containing the string "tentel".
 *
 * Check https://developer.android.com/studio/build/application-id for info on "package" in
 * "AndroidManifest.xml" and "applicationId" in "build.gradle".
 *
 * The {@link #tentel_PACKAGE_NAME} must be used in source code of tentel app and its plugins instead
 * of hardcoded "com.tentel" paths.
 */
public final class tentelConstants {


    /*
     * tentel organization variables.
     */

    /** tentel Github organization name */
    public static final String tentel_GITHUB_ORGANIZATION_NAME = "tentel"; // Default: "tentel"
    /** tentel Github organization url */
    public static final String tentel_GITHUB_ORGANIZATION_URL = "https://github.com" + "/" + tentel_GITHUB_ORGANIZATION_NAME; // Default: "https://github.com/tentel"

    /** F-Droid packages base url */
    public static final String FDROID_PACKAGES_BASE_URL = "https://f-droid.org/en/packages"; // Default: "https://f-droid.org/en/packages"





    /*
     * tentel and its plugin app and package names and urls.
     */

    /** tentel app name */
    public static final String tentel_APP_NAME = "tentel"; // Default: "tentel"
    /** tentel package name */
    public static final String tentel_PACKAGE_NAME = "com.tentel"; // Default: "com.tentel"
    /** tentel Github repo name */
    public static final String tentel_GITHUB_REPO_NAME = "tentel-app"; // Default: "tentel-app"
    /** tentel Github repo url */
    public static final String tentel_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-app"
    /** tentel Github issues repo url */
    public static final String tentel_GITHUB_ISSUES_REPO_URL = tentel_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-app/issues"
    /** tentel F-Droid package url */
    public static final String tentel_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel"


    /** tentel:API app name */
    public static final String tentel_API_APP_NAME = "tentel:API"; // Default: "tentel:API"
    /** tentel:API app package name */
    public static final String tentel_API_PACKAGE_NAME = tentel_PACKAGE_NAME + ".api"; // Default: "com.tentel.api"
    /** tentel:API Github repo name */
    public static final String tentel_API_GITHUB_REPO_NAME = "tentel-api"; // Default: "tentel-api"
    /** tentel:API Github repo url */
    public static final String tentel_API_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_API_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-api"
    /** tentel:API Github issues repo url */
    public static final String tentel_API_GITHUB_ISSUES_REPO_URL = tentel_API_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-api/issues"
    /** tentel:API F-Droid package url */
    public static final String tentel_API_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_API_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.api"


    /** tentel:Boot app name */
    public static final String tentel_BOOT_APP_NAME = "tentel:Boot"; // Default: "tentel:Boot"
    /** tentel:Boot app package name */
    public static final String tentel_BOOT_PACKAGE_NAME = tentel_PACKAGE_NAME + ".boot"; // Default: "com.tentel.boot"
    /** tentel:Boot Github repo name */
    public static final String tentel_BOOT_GITHUB_REPO_NAME = "tentel-boot"; // Default: "tentel-boot"
    /** tentel:Boot Github repo url */
    public static final String tentel_BOOT_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_BOOT_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-boot"
    /** tentel:Boot Github issues repo url */
    public static final String tentel_BOOT_GITHUB_ISSUES_REPO_URL = tentel_BOOT_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-boot/issues"
    /** tentel:Boot F-Droid package url */
    public static final String tentel_BOOT_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_BOOT_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.boot"


    /** tentel:Float app name */
    public static final String tentel_FLOAT_APP_NAME = "tentel:Float"; // Default: "tentel:Float"
    /** tentel:Float app package name */
    public static final String tentel_FLOAT_PACKAGE_NAME = tentel_PACKAGE_NAME + ".window"; // Default: "com.tentel.window"
    /** tentel:Float Github repo name */
    public static final String tentel_FLOAT_GITHUB_REPO_NAME = "tentel-float"; // Default: "tentel-float"
    /** tentel:Float Github repo url */
    public static final String tentel_FLOAT_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_FLOAT_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-float"
    /** tentel:Float Github issues repo url */
    public static final String tentel_FLOAT_GITHUB_ISSUES_REPO_URL = tentel_FLOAT_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-float/issues"
    /** tentel:Float F-Droid package url */
    public static final String tentel_FLOAT_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_FLOAT_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.window"


    /** tentel:Styling app name */
    public static final String tentel_STYLING_APP_NAME = "tentel:Styling"; // Default: "tentel:Styling"
    /** tentel:Styling app package name */
    public static final String tentel_STYLING_PACKAGE_NAME = tentel_PACKAGE_NAME + ".styling"; // Default: "com.tentel.styling"
    /** tentel:Styling Github repo name */
    public static final String tentel_STYLING_GITHUB_REPO_NAME = "tentel-styling"; // Default: "tentel-styling"
    /** tentel:Styling Github repo url */
    public static final String tentel_STYLING_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_STYLING_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-styling"
    /** tentel:Styling Github issues repo url */
    public static final String tentel_STYLING_GITHUB_ISSUES_REPO_URL = tentel_STYLING_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-styling/issues"
    /** tentel:Styling F-Droid package url */
    public static final String tentel_STYLING_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_STYLING_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.styling"


    /** tentel:Tasker app name */
    public static final String tentel_TASKER_APP_NAME = "tentel:Tasker"; // Default: "tentel:Tasker"
    /** tentel:Tasker app package name */
    public static final String tentel_TASKER_PACKAGE_NAME = tentel_PACKAGE_NAME + ".tasker"; // Default: "com.tentel.tasker"
    /** tentel:Tasker Github repo name */
    public static final String tentel_TASKER_GITHUB_REPO_NAME = "tentel-tasker"; // Default: "tentel-tasker"
    /** tentel:Tasker Github repo url */
    public static final String tentel_TASKER_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_TASKER_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-tasker"
    /** tentel:Tasker Github issues repo url */
    public static final String tentel_TASKER_GITHUB_ISSUES_REPO_URL = tentel_TASKER_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-tasker/issues"
    /** tentel:Tasker F-Droid package url */
    public static final String tentel_TASKER_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_TASKER_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.tasker"


    /** tentel:Widget app name */
    public static final String tentel_WIDGET_APP_NAME = "tentel:Widget"; // Default: "tentel:Widget"
    /** tentel:Widget app package name */
    public static final String tentel_WIDGET_PACKAGE_NAME = tentel_PACKAGE_NAME + ".widget"; // Default: "com.tentel.widget"
    /** tentel:Widget Github repo name */
    public static final String tentel_WIDGET_GITHUB_REPO_NAME = "tentel-widget"; // Default: "tentel-widget"
    /** tentel:Widget Github repo url */
    public static final String tentel_WIDGET_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_WIDGET_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-widget"
    /** tentel:Widget Github issues repo url */
    public static final String tentel_WIDGET_GITHUB_ISSUES_REPO_URL = tentel_WIDGET_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-widget/issues"
    /** tentel:Widget F-Droid package url */
    public static final String tentel_WIDGET_FDROID_PACKAGE_URL = FDROID_PACKAGES_BASE_URL + "/" + tentel_WIDGET_PACKAGE_NAME; // Default: "https://f-droid.org/en/packages/com.tentel.widget"





    /*
     * tentel plugin apps lists.
     */

    public static final List<String> tentel_PLUGIN_APP_NAMES_LIST = Arrays.asList(
        tentel_API_APP_NAME,
        tentel_BOOT_APP_NAME,
        tentel_FLOAT_APP_NAME,
        tentel_STYLING_APP_NAME,
        tentel_TASKER_APP_NAME,
        tentel_WIDGET_APP_NAME);

    public static final List<String> tentel_PLUGIN_APP_PACKAGE_NAMES_LIST = Arrays.asList(
        tentel_API_PACKAGE_NAME,
        tentel_BOOT_PACKAGE_NAME,
        tentel_FLOAT_PACKAGE_NAME,
        tentel_STYLING_PACKAGE_NAME,
        tentel_TASKER_PACKAGE_NAME,
        tentel_WIDGET_PACKAGE_NAME);





    /*
     * tentel APK releases.
     */

    /** F-Droid APK release */
    public static final String APK_RELEASE_FDROID = "F-Droid"; // Default: "F-Droid"

    /** F-Droid APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_FDROID_SIGNING_CERTIFICATE_SHA256_DIGEST = "228FB2CFE90831C1499EC3CCAF61E96E8E1CE70766B9474672CE427334D41C42"; // Default: "228FB2CFE90831C1499EC3CCAF61E96E8E1CE70766B9474672CE427334D41C42"

    /** Github APK release */
    public static final String APK_RELEASE_GITHUB = "Github"; // Default: "Github"

    /** Github APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_GITHUB_SIGNING_CERTIFICATE_SHA256_DIGEST = "B6DA01480EEFD5FBF2CD3771B8D1021EC791304BDD6C4BF41D3FAABAD48EE5E1"; // Default: "B6DA01480EEFD5FBF2CD3771B8D1021EC791304BDD6C4BF41D3FAABAD48EE5E1"

    /** Google Play Store APK release */
    public static final String APK_RELEASE_GOOGLE_PLAYSTORE = "Google Play Store"; // Default: "Google Play Store"

    /** Google Play Store APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_GOOGLE_PLAYSTORE_SIGNING_CERTIFICATE_SHA256_DIGEST = "738F0A30A04D3C8A1BE304AF18D0779BCF3EA88FB60808F657A3521861C2EBF9"; // Default: "738F0A30A04D3C8A1BE304AF18D0779BCF3EA88FB60808F657A3521861C2EBF9"





    /*
     * tentel packages urls.
     */

    /** tentel Packages Github repo name */
    public static final String tentel_PACKAGES_GITHUB_REPO_NAME = "tentel-packages"; // Default: "tentel-packages"
    /** tentel Packages Github repo url */
    public static final String tentel_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-packages"
    /** tentel Packages Github issues repo url */
    public static final String tentel_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-packages/issues"


    /** tentel Game Packages Github repo name */
    public static final String tentel_GAME_PACKAGES_GITHUB_REPO_NAME = "game-packages"; // Default: "game-packages"
    /** tentel Game Packages Github repo url */
    public static final String tentel_GAME_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_GAME_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/game-packages"
    /** tentel Game Packages Github issues repo url */
    public static final String tentel_GAME_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_GAME_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/game-packages/issues"


    /** tentel Science Packages Github repo name */
    public static final String tentel_SCIENCE_PACKAGES_GITHUB_REPO_NAME = "science-packages"; // Default: "science-packages"
    /** tentel Science Packages Github repo url */
    public static final String tentel_SCIENCE_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_SCIENCE_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/science-packages"
    /** tentel Science Packages Github issues repo url */
    public static final String tentel_SCIENCE_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_SCIENCE_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/science-packages/issues"


    /** tentel Root Packages Github repo name */
    public static final String tentel_ROOT_PACKAGES_GITHUB_REPO_NAME = "tentel-root-packages"; // Default: "tentel-root-packages"
    /** tentel Root Packages Github repo url */
    public static final String tentel_ROOT_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_ROOT_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/tentel-root-packages"
    /** tentel Root Packages Github issues repo url */
    public static final String tentel_ROOT_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_ROOT_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/tentel-root-packages/issues"


    /** tentel Unstable Packages Github repo name */
    public static final String tentel_UNSTABLE_PACKAGES_GITHUB_REPO_NAME = "unstable-packages"; // Default: "unstable-packages"
    /** tentel Unstable Packages Github repo url */
    public static final String tentel_UNSTABLE_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_UNSTABLE_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/unstable-packages"
    /** tentel Unstable Packages Github issues repo url */
    public static final String tentel_UNSTABLE_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_UNSTABLE_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/unstable-packages/issues"


    /** tentel X11 Packages Github repo name */
    public static final String tentel_X11_PACKAGES_GITHUB_REPO_NAME = "x11-packages"; // Default: "x11-packages"
    /** tentel X11 Packages Github repo url */
    public static final String tentel_X11_PACKAGES_GITHUB_REPO_URL = tentel_GITHUB_ORGANIZATION_URL + "/" + tentel_X11_PACKAGES_GITHUB_REPO_NAME; // Default: "https://github.com/tentel/x11-packages"
    /** tentel X11 Packages Github issues repo url */
    public static final String tentel_X11_PACKAGES_GITHUB_ISSUES_REPO_URL = tentel_X11_PACKAGES_GITHUB_REPO_URL + "/issues"; // Default: "https://github.com/tentel/x11-packages/issues"





    /*
     * tentel miscellaneous urls.
     */

    /** tentel Wiki */
    public static final String tentel_WIKI = tentel_APP_NAME + " Wiki"; // Default: "tentel Wiki"

    /** tentel Wiki url */
    public static final String tentel_WIKI_URL = "https://wiki.tentel.com"; // Default: "https://wiki.tentel.com"

    /** tentel Github wiki repo url */
    public static final String tentel_GITHUB_WIKI_REPO_URL = tentel_GITHUB_REPO_URL + "/wiki"; // Default: "https://github.com/tentel/tentel-app/wiki"

    /** tentel Packages wiki repo url */
    public static final String tentel_PACKAGES_GITHUB_WIKI_REPO_URL = tentel_PACKAGES_GITHUB_REPO_URL + "/wiki"; // Default: "https://github.com/tentel/tentel-packages/wiki"


    /** tentel support email url */
    public static final String tentel_SUPPORT_EMAIL_URL = "tentelreports@groups.io"; // Default: "tentelreports@groups.io"

    /** tentel support email mailto url */
    public static final String tentel_SUPPORT_EMAIL_MAILTO_URL = "mailto:" + tentel_SUPPORT_EMAIL_URL; // Default: "mailto:tentelreports@groups.io"


    /** tentel Reddit subreddit */
    public static final String tentel_REDDIT_SUBREDDIT = "r/tentel"; // Default: "r/tentel"

    /** tentel Reddit subreddit url */
    public static final String tentel_REDDIT_SUBREDDIT_URL = "https://www.reddit.com/r/tentel"; // Default: "https://www.reddit.com/r/tentel"


    /** tentel donate url */
    public static final String tentel_DONATE_URL = tentel_PACKAGES_GITHUB_REPO_URL + "/wiki/Donate"; // Default: "https://github.com/tentel/tentel-packages/wiki/Donate"





    /*
     * tentel app core directory paths.
     */

    /** tentel app internal private app data directory path */
    @SuppressLint("SdCardPath")
    public static final String tentel_INTERNAL_PRIVATE_APP_DATA_DIR_PATH = "/data/data/" + tentel_PACKAGE_NAME; // Default: "/data/data/com.tentel"
    /** tentel app internal private app data directory */
    public static final File tentel_INTERNAL_PRIVATE_APP_DATA_DIR = new File(tentel_INTERNAL_PRIVATE_APP_DATA_DIR_PATH);



    /** tentel app Files directory path */
    public static final String tentel_FILES_DIR_PATH = tentel_INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "/files"; // Default: "/data/data/com.tentel/files"
    /** tentel app Files directory */
    public static final File tentel_FILES_DIR = new File(tentel_FILES_DIR_PATH);



    /** tentel app $PREFIX directory path */
    public static final String tentel_PREFIX_DIR_PATH = tentel_FILES_DIR_PATH + "/usr"; // Default: "/data/data/com.tentel/files/usr"
    /** tentel app $PREFIX directory */
    public static final File tentel_PREFIX_DIR = new File(tentel_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/bin directory path */
    public static final String tentel_BIN_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/bin"; // Default: "/data/data/com.tentel/files/usr/bin"
    /** tentel app $PREFIX/bin directory */
    public static final File tentel_BIN_PREFIX_DIR = new File(tentel_BIN_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/etc directory path */
    public static final String tentel_ETC_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/etc"; // Default: "/data/data/com.tentel/files/usr/etc"
    /** tentel app $PREFIX/etc directory */
    public static final File tentel_ETC_PREFIX_DIR = new File(tentel_ETC_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/include directory path */
    public static final String tentel_INCLUDE_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/include"; // Default: "/data/data/com.tentel/files/usr/include"
    /** tentel app $PREFIX/include directory */
    public static final File tentel_INCLUDE_PREFIX_DIR = new File(tentel_INCLUDE_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/lib directory path */
    public static final String tentel_LIB_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/lib"; // Default: "/data/data/com.tentel/files/usr/lib"
    /** tentel app $PREFIX/lib directory */
    public static final File tentel_LIB_PREFIX_DIR = new File(tentel_LIB_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/libexec directory path */
    public static final String tentel_LIBEXEC_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/libexec"; // Default: "/data/data/com.tentel/files/usr/libexec"
    /** tentel app $PREFIX/libexec directory */
    public static final File tentel_LIBEXEC_PREFIX_DIR = new File(tentel_LIBEXEC_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/share directory path */
    public static final String tentel_SHARE_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/share"; // Default: "/data/data/com.tentel/files/usr/share"
    /** tentel app $PREFIX/share directory */
    public static final File tentel_SHARE_PREFIX_DIR = new File(tentel_SHARE_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/tmp and $TMPDIR directory path */
    public static final String tentel_TMP_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/tmp"; // Default: "/data/data/com.tentel/files/usr/tmp"
    /** tentel app $PREFIX/tmp and $TMPDIR directory */
    public static final File tentel_TMP_PREFIX_DIR = new File(tentel_TMP_PREFIX_DIR_PATH);


    /** tentel app $PREFIX/var directory path */
    public static final String tentel_VAR_PREFIX_DIR_PATH = tentel_PREFIX_DIR_PATH + "/var"; // Default: "/data/data/com.tentel/files/usr/var"
    /** tentel app $PREFIX/var directory */
    public static final File tentel_VAR_PREFIX_DIR = new File(tentel_VAR_PREFIX_DIR_PATH);



    /** tentel app usr-staging directory path */
    public static final String tentel_STAGING_PREFIX_DIR_PATH = tentel_FILES_DIR_PATH + "/usr-staging"; // Default: "/data/data/com.tentel/files/usr-staging"
    /** tentel app usr-staging directory */
    public static final File tentel_STAGING_PREFIX_DIR = new File(tentel_STAGING_PREFIX_DIR_PATH);



    /** tentel app $HOME directory path */
    public static final String tentel_HOME_DIR_PATH = tentel_FILES_DIR_PATH + "/home"; // Default: "/data/data/com.tentel/files/home"
    /** tentel app $HOME directory */
    public static final File tentel_HOME_DIR = new File(tentel_HOME_DIR_PATH);


    /** tentel app config home directory path */
    public static final String tentel_CONFIG_HOME_DIR_PATH = tentel_HOME_DIR_PATH + "/.config/tentel"; // Default: "/data/data/com.tentel/files/home/.config/tentel"
    /** tentel app config home directory */
    public static final File tentel_CONFIG_HOME_DIR = new File(tentel_CONFIG_HOME_DIR_PATH);


    /** tentel app data home directory path */
    public static final String tentel_DATA_HOME_DIR_PATH = tentel_HOME_DIR_PATH + "/.tentel"; // Default: "/data/data/com.tentel/files/home/.tentel"
    /** tentel app data home directory */
    public static final File tentel_DATA_HOME_DIR = new File(tentel_DATA_HOME_DIR_PATH);


    /** tentel app storage home directory path */
    public static final String tentel_STORAGE_HOME_DIR_PATH = tentel_HOME_DIR_PATH + "/storage"; // Default: "/data/data/com.tentel/files/home/storage"
    /** tentel app storage home directory */
    public static final File tentel_STORAGE_HOME_DIR = new File(tentel_STORAGE_HOME_DIR_PATH);





    /*
     * tentel app and plugin preferences and properties file paths.
     */

    /** tentel app default SharedPreferences file basename without extension */
    public static final String tentel_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_PACKAGE_NAME + "_preferences"; // Default: "com.tentel_preferences"

    /** tentel:API app default SharedPreferences file basename without extension */
    public static final String tentel_API_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_API_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.api_preferences"

    /** tentel:Boot app default SharedPreferences file basename without extension */
    public static final String tentel_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_BOOT_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.boot_preferences"

    /** tentel:Float app default SharedPreferences file basename without extension */
    public static final String tentel_FLOAT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_FLOAT_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.window_preferences"

    /** tentel:Styling app default SharedPreferences file basename without extension */
    public static final String tentel_STYLING_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_STYLING_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.styling_preferences"

    /** tentel:Tasker app default SharedPreferences file basename without extension */
    public static final String tentel_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_TASKER_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.tasker_preferences"

    /** tentel:Widget app default SharedPreferences file basename without extension */
    public static final String tentel_WIDGET_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = tentel_WIDGET_PACKAGE_NAME + "_preferences"; // Default: "com.tentel.widget_preferences"


    /** tentel app tentel.properties primary file path */
    public static final String tentel_PROPERTIES_PRIMARY_FILE_PATH = tentel_DATA_HOME_DIR_PATH + "/tentel.properties"; // Default: "/data/data/com.tentel/files/home/.tentel/tentel.properties"
    /** tentel app tentel.properties primary file */
    public static final File tentel_PROPERTIES_PRIMARY_FILE = new File(tentel_PROPERTIES_PRIMARY_FILE_PATH);

    /** tentel app tentel.properties secondary file path */
    public static final String tentel_PROPERTIES_SECONDARY_FILE_PATH = tentel_CONFIG_HOME_DIR_PATH + "/tentel.properties"; // Default: "/data/data/com.tentel/files/home/.config/tentel/tentel.properties"
    /** tentel app tentel.properties secondary file */
    public static final File tentel_PROPERTIES_SECONDARY_FILE = new File(tentel_PROPERTIES_SECONDARY_FILE_PATH);


    /** tentel:Float app tentel.properties primary file path */
    public static final String tentel_FLOAT_PROPERTIES_PRIMARY_FILE_PATH = tentel_DATA_HOME_DIR_PATH + "/tentel.float.properties"; // Default: "/data/data/com.tentel/files/home/.tentel/tentel.float.properties"
    /** tentel:Float app tentel.properties primary file */
    public static final File tentel_FLOAT_PROPERTIES_PRIMARY_FILE = new File(tentel_FLOAT_PROPERTIES_PRIMARY_FILE_PATH);

    /** tentel:Float app tentel.properties secondary file path */
    public static final String tentel_FLOAT_PROPERTIES_SECONDARY_FILE_PATH = tentel_CONFIG_HOME_DIR_PATH + "/tentel.float.properties"; // Default: "/data/data/com.tentel/files/home/.config/tentel/tentel.float.properties"
    /** tentel:Float app tentel.properties secondary file */
    public static final File tentel_FLOAT_PROPERTIES_SECONDARY_FILE = new File(tentel_FLOAT_PROPERTIES_SECONDARY_FILE_PATH);


    /** tentel app and tentel:Styling colors.properties file path */
    public static final String tentel_COLOR_PROPERTIES_FILE_PATH = tentel_DATA_HOME_DIR_PATH + "/colors.properties"; // Default: "/data/data/com.tentel/files/home/.tentel/colors.properties"
    /** tentel app and tentel:Styling colors.properties file */
    public static final File tentel_COLOR_PROPERTIES_FILE = new File(tentel_COLOR_PROPERTIES_FILE_PATH);

    /** tentel app and tentel:Styling font.ttf file path */
    public static final String tentel_FONT_FILE_PATH = tentel_DATA_HOME_DIR_PATH + "/font.ttf"; // Default: "/data/data/com.tentel/files/home/.tentel/font.ttf"
    /** tentel app and tentel:Styling font.ttf file */
    public static final File tentel_FONT_FILE = new File(tentel_FONT_FILE_PATH);


    /** tentel app and plugins crash log file path */
    public static final String tentel_CRASH_LOG_FILE_PATH = tentel_HOME_DIR_PATH + "/crash_log.md"; // Default: "/data/data/com.tentel/files/home/crash_log.md"

    /** tentel app and plugins crash log backup file path */
    public static final String tentel_CRASH_LOG_BACKUP_FILE_PATH = tentel_HOME_DIR_PATH + "/crash_log_backup.md"; // Default: "/data/data/com.tentel/files/home/crash_log_backup.md"





    /*
     * tentel app plugin specific paths.
     */

    /** tentel app directory path to store scripts to be run at boot by tentel:Boot */
    public static final String tentel_BOOT_SCRIPTS_DIR_PATH = tentel_DATA_HOME_DIR_PATH + "/boot"; // Default: "/data/data/com.tentel/files/home/.tentel/boot"
    /** tentel app directory to store scripts to be run at boot by tentel:Boot */
    public static final File tentel_BOOT_SCRIPTS_DIR = new File(tentel_BOOT_SCRIPTS_DIR_PATH);


    /** tentel app directory path to store foreground scripts that can be run by the tentel launcher
     * widget provided by tentel:Widget */
    public static final String tentel_SHORTCUT_SCRIPTS_DIR_PATH = tentel_HOME_DIR_PATH + "/.shortcuts"; // Default: "/data/data/com.tentel/files/home/.shortcuts"
    /** tentel app directory to store foreground scripts that can be run by the tentel launcher widget provided by tentel:Widget */
    public static final File tentel_SHORTCUT_SCRIPTS_DIR = new File(tentel_SHORTCUT_SCRIPTS_DIR_PATH);


    /** tentel app directory basename that stores background scripts that can be run by the tentel
     * launcher widget provided by tentel:Widget */
    public static final String tentel_SHORTCUT_TASKS_SCRIPTS_DIR_BASENAME =  "tasks"; // Default: "tasks"
    /** tentel app directory path to store background scripts that can be run by the tentel launcher
     * widget provided by tentel:Widget */
    public static final String tentel_SHORTCUT_TASKS_SCRIPTS_DIR_PATH = tentel_SHORTCUT_SCRIPTS_DIR_PATH + "/" + tentel_SHORTCUT_TASKS_SCRIPTS_DIR_BASENAME; // Default: "/data/data/com.tentel/files/home/.shortcuts/tasks"
    /** tentel app directory to store background scripts that can be run by the tentel launcher widget provided by tentel:Widget */
    public static final File tentel_SHORTCUT_TASKS_SCRIPTS_DIR = new File(tentel_SHORTCUT_TASKS_SCRIPTS_DIR_PATH);


    /** tentel app directory basename that stores icons for the foreground and background scripts
     * that can be run by the tentel launcher widget provided by tentel:Widget */
    public static final String tentel_SHORTCUT_SCRIPT_ICONS_DIR_BASENAME =  "icons"; // Default: "icons"
    /** tentel app directory path to store icons for the foreground and background scripts that can
     * be run by the tentel launcher widget provided by tentel:Widget */
    public static final String tentel_SHORTCUT_SCRIPT_ICONS_DIR_PATH = tentel_SHORTCUT_SCRIPTS_DIR_PATH + "/" + tentel_SHORTCUT_SCRIPT_ICONS_DIR_BASENAME; // Default: "/data/data/com.tentel/files/home/.shortcuts/icons"
    /** tentel app directory to store icons for the foreground and background scripts that can be
     * run by the tentel launcher widget provided by tentel:Widget */
    public static final File tentel_SHORTCUT_SCRIPT_ICONS_DIR = new File(tentel_SHORTCUT_SCRIPT_ICONS_DIR_PATH);


    /** tentel app directory path to store scripts to be run by 3rd party twofortyfouram locale plugin
     * host apps like Tasker app via the tentel:Tasker plugin client */
    public static final String tentel_TASKER_SCRIPTS_DIR_PATH = tentel_DATA_HOME_DIR_PATH + "/tasker"; // Default: "/data/data/com.tentel/files/home/.tentel/tasker"
    /** tentel app directory to store scripts to be run by 3rd party twofortyfouram locale plugin host apps like Tasker app via the tentel:Tasker plugin client */
    public static final File tentel_TASKER_SCRIPTS_DIR = new File(tentel_TASKER_SCRIPTS_DIR_PATH);





    /*
     * tentel app and plugins notification variables.
     */

    /** tentel app notification channel id used by {@link tentel_APP.tentel_SERVICE} */
    public static final String tentel_APP_NOTIFICATION_CHANNEL_ID = "tentel_notification_channel";
    /** tentel app notification channel name used by {@link tentel_APP.tentel_SERVICE} */
    public static final String tentel_APP_NOTIFICATION_CHANNEL_NAME = tentelConstants.tentel_APP_NAME + " App";
    /** tentel app unique notification id used by {@link tentel_APP.tentel_SERVICE} */
    public static final int tentel_APP_NOTIFICATION_ID = 1337;

    /** tentel app notification channel id used by {@link tentel_APP.RUN_COMMAND_SERVICE} */
    public static final String tentel_RUN_COMMAND_NOTIFICATION_CHANNEL_ID = "tentel_run_command_notification_channel";
    /** tentel app notification channel name used by {@link tentel_APP.RUN_COMMAND_SERVICE} */
    public static final String tentel_RUN_COMMAND_NOTIFICATION_CHANNEL_NAME = tentelConstants.tentel_APP_NAME + " RunCommandService";
    /** tentel app unique notification id used by {@link tentel_APP.RUN_COMMAND_SERVICE} */
    public static final int tentel_RUN_COMMAND_NOTIFICATION_ID = 1338;

    /** tentel app notification channel id used for plugin command errors */
    public static final String tentel_PLUGIN_COMMAND_ERRORS_NOTIFICATION_CHANNEL_ID = "tentel_plugin_command_errors_notification_channel";
    /** tentel app notification channel name used for plugin command errors */
    public static final String tentel_PLUGIN_COMMAND_ERRORS_NOTIFICATION_CHANNEL_NAME = tentelConstants.tentel_APP_NAME + " Plugin Commands Errors";

    /** tentel app notification channel id used for crash reports */
    public static final String tentel_CRASH_REPORTS_NOTIFICATION_CHANNEL_ID = "tentel_crash_reports_notification_channel";
    /** tentel app notification channel name used for crash reports */
    public static final String tentel_CRASH_REPORTS_NOTIFICATION_CHANNEL_NAME = tentelConstants.tentel_APP_NAME + " Crash Reports";


    /** tentel app notification channel id used by {@link tentel_FLOAT_APP.tentel_FLOAT_SERVICE} */
    public static final String tentel_FLOAT_APP_NOTIFICATION_CHANNEL_ID = "tentel_float_notification_channel";
    /** tentel app notification channel name used by {@link tentel_FLOAT_APP.tentel_FLOAT_SERVICE} */
    public static final String tentel_FLOAT_APP_NOTIFICATION_CHANNEL_NAME = tentelConstants.tentel_FLOAT_APP_NAME + " App";
    /** tentel app unique notification id used by {@link tentel_APP.tentel_SERVICE} */
    public static final int tentel_FLOAT_APP_NOTIFICATION_ID = 1339;





    /*
     * tentel app and plugins miscellaneous variables.
     */

    /** Android OS permission declared by tentel app in AndroidManifest.xml which can be requested by
     * 3rd party apps to run various commands in tentel app context */
    public static final String PERMISSION_RUN_COMMAND = tentel_PACKAGE_NAME + ".permission.RUN_COMMAND"; // Default: "com.tentel.permission.RUN_COMMAND"

    /** tentel property defined in tentel.properties file as a secondary check to PERMISSION_RUN_COMMAND
     * to allow 3rd party apps to run various commands in tentel app context */
    public static final String PROP_ALLOW_EXTERNAL_APPS = "allow-external-apps"; // Default: "allow-external-apps"
    /** Default value for {@link #PROP_ALLOW_EXTERNAL_APPS} */
    public static final String PROP_DEFAULT_VALUE_ALLOW_EXTERNAL_APPS = "false"; // Default: "false"

    /** The broadcast action sent when tentel App opens */
    public static final String BROADCAST_tentel_OPENED = tentel_PACKAGE_NAME + ".app.OPENED";

    /** The Uri authority for tentel app file shares */
    public static final String tentel_FILE_SHARE_URI_AUTHORITY = tentel_PACKAGE_NAME + ".files"; // Default: "com.tentel.files"

    /** The normal comma character (U+002C, &comma;, &#44;, comma) */
    public static final String COMMA_NORMAL = ","; // Default: ","

    /** The alternate comma character (U+201A, &sbquo;, &#8218;, single low-9 quotation mark) that
     * may be used instead of {@link #COMMA_NORMAL} */
    public static final String COMMA_ALTERNATIVE = "‚"; // Default: "‚"






    /**
     * tentel app constants.
     */
    public static final class tentel_APP {

        /** tentel app core activity name. */
        public static final String tentel_ACTIVITY_NAME = tentel_PACKAGE_NAME + ".app.tentelActivity"; // Default: "com.tentel.app.tentelActivity"

        /**
         * tentel app core activity.
         */
        public static final class tentel_ACTIVITY {

            /** Intent extra for if tentel failsafe session needs to be started and is used by {@link tentel_ACTIVITY} and {@link tentel_SERVICE#ACTION_STOP_SERVICE} */
            public static final String EXTRA_FAILSAFE_SESSION = tentelConstants.tentel_PACKAGE_NAME + ".app.failsafe_session"; // Default: "com.tentel.app.failsafe_session"


            /** Intent action to make tentel request storage permissions */
            public static final String ACTION_REQUEST_PERMISSIONS = tentelConstants.tentel_PACKAGE_NAME + ".app.request_storage_permissions"; // Default: "com.tentel.app.request_storage_permissions"

            /** Intent action to make tentel reload its tentel session styling */
            public static final String ACTION_RELOAD_STYLE = tentelConstants.tentel_PACKAGE_NAME + ".app.reload_style"; // Default: "com.tentel.app.reload_style"
            /** Intent {@code String} extra for what to reload for the tentel_ACTIVITY.ACTION_RELOAD_STYLE intent. This has been deperecated. */
            @Deprecated
            public static final String EXTRA_RELOAD_STYLE = tentelConstants.tentel_PACKAGE_NAME + ".app.reload_style"; // Default: "com.tentel.app.reload_style"

        }





        /** tentel app settings activity name. */
        public static final String tentel_SETTINGS_ACTIVITY_NAME = tentel_PACKAGE_NAME + ".app.activities.SettingsActivity"; // Default: "com.tentel.app.activities.SettingsActivity"





        /** tentel app core service name. */
        public static final String tentel_SERVICE_NAME = tentel_PACKAGE_NAME + ".app.tentelService"; // Default: "com.tentel.app.tentelService"

        /**
         * tentel app core service.
         */
        public static final class tentel_SERVICE {

            /** Intent action to stop tentel_SERVICE */
            public static final String ACTION_STOP_SERVICE = tentel_PACKAGE_NAME + ".service_stop"; // Default: "com.tentel.service_stop"


            /** Intent action to make tentel_SERVICE acquire a wakelock */
            public static final String ACTION_WAKE_LOCK = tentel_PACKAGE_NAME + ".service_wake_lock"; // Default: "com.tentel.service_wake_lock"


            /** Intent action to make tentel_SERVICE release wakelock */
            public static final String ACTION_WAKE_UNLOCK = tentel_PACKAGE_NAME + ".service_wake_unlock"; // Default: "com.tentel.service_wake_unlock"


            /** Intent action to execute command with tentel_SERVICE */
            public static final String ACTION_SERVICE_EXECUTE = tentel_PACKAGE_NAME + ".service_execute"; // Default: "com.tentel.service_execute"

            /** Uri scheme for paths sent via intent to tentel_SERVICE */
            public static final String URI_SCHEME_SERVICE_EXECUTE = tentel_PACKAGE_NAME + ".file"; // Default: "com.tentel.file"
            /** Intent {@code String[]} extra for arguments to the executable of the command for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_ARGUMENTS = tentel_PACKAGE_NAME + ".execute.arguments"; // Default: "com.tentel.execute.arguments"
            /** Intent {@code String} extra for stdin of the command for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_STDIN = tentel_PACKAGE_NAME + ".execute.stdin"; // Default: "com.tentel.execute.stdin"
            /** Intent {@code String} extra for command current working directory for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_WORKDIR = tentel_PACKAGE_NAME + ".execute.cwd"; // Default: "com.tentel.execute.cwd"
            /** Intent {@code boolean} extra for command background mode for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_BACKGROUND = tentel_PACKAGE_NAME + ".execute.background"; // Default: "com.tentel.execute.background"
            /** Intent {@code String} extra for custom log level for background commands defined by {@link com.tentel.shared.logger.Logger} for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_BACKGROUND_CUSTOM_LOG_LEVEL = tentel_PACKAGE_NAME + ".execute.background_custom_log_level"; // Default: "com.tentel.execute.background_custom_log_level"
            /** Intent {@code String} extra for session action for foreground commands for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_SESSION_ACTION = tentel_PACKAGE_NAME + ".execute.session_action"; // Default: "com.tentel.execute.session_action"
            /** Intent {@code String} extra for label of the command for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_LABEL = tentel_PACKAGE_NAME + ".execute.command_label"; // Default: "com.tentel.execute.command_label"
            /** Intent markdown {@code String} extra for description of the command for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_DESCRIPTION = tentel_PACKAGE_NAME + ".execute.command_description"; // Default: "com.tentel.execute.command_description"
            /** Intent markdown {@code String} extra for help of the command for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_HELP = tentel_PACKAGE_NAME + ".execute.command_help"; // Default: "com.tentel.execute.command_help"
            /** Intent markdown {@code String} extra for help of the plugin API for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent (Internal Use Only) */
            public static final String EXTRA_PLUGIN_API_HELP = tentel_PACKAGE_NAME + ".execute.plugin_api_help"; // Default: "com.tentel.execute.plugin_help"
            /** Intent {@code Parcelable} extra for the pending intent that should be sent with the
             * result of the execution command to the execute command caller for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_PENDING_INTENT = "pendingIntent"; // Default: "pendingIntent"
            /** Intent {@code String} extra for the directory path in which to write the result of the
             * execution command for the execute command caller for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_DIRECTORY = tentel_PACKAGE_NAME + ".execute.result_directory"; // Default: "com.tentel.execute.result_directory"
            /** Intent {@code boolean} extra for whether the result should be written to a single file
             * or multiple files (err, errmsg, stdout, stderr, exit_code) in
             * {@link #EXTRA_RESULT_DIRECTORY} for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_SINGLE_FILE = tentel_PACKAGE_NAME + ".execute.result_single_file"; // Default: "com.tentel.execute.result_single_file"
            /** Intent {@code String} extra for the basename of the result file that should be created
             * in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is {@code true}
             * for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_FILE_BASENAME = tentel_PACKAGE_NAME + ".execute.result_file_basename"; // Default: "com.tentel.execute.result_file_basename"
            /** Intent {@code String} extra for the output {@link Formatter} format of the
             * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_FILE_OUTPUT_FORMAT = tentel_PACKAGE_NAME + ".execute.result_file_output_format"; // Default: "com.tentel.execute.result_file_output_format"
            /** Intent {@code String} extra for the error {@link Formatter} format of the
             * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_FILE_ERROR_FORMAT = tentel_PACKAGE_NAME + ".execute.result_file_error_format"; // Default: "com.tentel.execute.result_file_error_format"
            /** Intent {@code String} extra for the optional suffix of the result files that should
             * be created in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is
             * {@code false} for the tentel_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_RESULT_FILES_SUFFIX = tentel_PACKAGE_NAME + ".execute.result_files_suffix"; // Default: "com.tentel.execute.result_files_suffix"



            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will set the new session as
             * the current session and will start {@link tentel_ACTIVITY} if its not running to bring
             * the new session to foreground.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY = 0;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
             * as the current session and will start {@link tentel_ACTIVITY} if its not running to
             * bring the existing session to foreground. The new session will be added to the left
             * sidebar in the sessions list.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY = 1;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will set the new session as
             * the current session but will not start {@link tentel_ACTIVITY} if its not running
             * and session(s) will be seen in tentel notification and can be clicked to bring new
             * session to foreground. If the {@link tentel_ACTIVITY} is already running, then this
             * will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_DONT_OPEN_ACTIVITY = 2;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
             * as the current session but will not start {@link tentel_ACTIVITY} if its not running
             * and session(s) will be seen in tentel notification and can be clicked to bring
             * existing session to foreground. If the {@link tentel_ACTIVITY} is already running,
             * then this will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY = 3;

            /** The minimum allowed value for {@link #EXTRA_SESSION_ACTION}. */
            public static final int MIN_VALUE_EXTRA_SESSION_ACTION = VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY;

            /** The maximum allowed value for {@link #EXTRA_SESSION_ACTION}. */
            public static final int MAX_VALUE_EXTRA_SESSION_ACTION = VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY;


            /** Intent {@code Bundle} extra to store result of execute command that is sent back for the
             * tentel_SERVICE.ACTION_SERVICE_EXECUTE intent if the {@link #EXTRA_PENDING_INTENT} is not
             * {@code null} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE = "result"; // Default: "result"
            /** Intent {@code String} extra for stdout value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT = "stdout"; // Default: "stdout"
            /** Intent {@code String} extra for original length of stdout value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT_ORIGINAL_LENGTH = "stdout_original_length"; // Default: "stdout_original_length"
            /** Intent {@code String} extra for stderr value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDERR = "stderr"; // Default: "stderr"
            /** Intent {@code String} extra for original length of stderr value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDERR_ORIGINAL_LENGTH = "stderr_original_length"; // Default: "stderr_original_length"
            /** Intent {@code int} extra for exit code value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_EXIT_CODE = "exitCode"; // Default: "exitCode"
            /** Intent {@code int} extra for err value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERR = "err"; // Default: "err"
            /** Intent {@code String} extra for errmsg value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERRMSG = "errmsg"; // Default: "errmsg"

        }





        /** tentel app run command service name. */
        public static final String RUN_COMMAND_SERVICE_NAME = tentel_PACKAGE_NAME + ".app.RunCommandService"; // tentel app service to receive commands from 3rd party apps "com.tentel.app.RunCommandService"

        /**
         * tentel app run command service to receive commands sent by 3rd party apps.
         */
        public static final class RUN_COMMAND_SERVICE {

            /** tentel RUN_COMMAND Intent help url */
            public static final String RUN_COMMAND_API_HELP_URL = tentel_GITHUB_WIKI_REPO_URL + "/RUN_COMMAND-Intent"; // Default: "https://github.com/tentel/tentel-app/wiki/RUN_COMMAND-Intent"


            /** Intent action to execute command with RUN_COMMAND_SERVICE */
            public static final String ACTION_RUN_COMMAND = tentel_PACKAGE_NAME + ".RUN_COMMAND"; // Default: "com.tentel.RUN_COMMAND"

            /** Intent {@code String} extra for absolute path of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_PATH = tentel_PACKAGE_NAME + ".RUN_COMMAND_PATH"; // Default: "com.tentel.RUN_COMMAND_PATH"
            /** Intent {@code String[]} extra for arguments to the executable of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_ARGUMENTS = tentel_PACKAGE_NAME + ".RUN_COMMAND_ARGUMENTS"; // Default: "com.tentel.RUN_COMMAND_ARGUMENTS"
            /** Intent {@code boolean} extra for whether to replace comma alternative characters in arguments with comma characters for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS = tentel_PACKAGE_NAME + ".RUN_COMMAND_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"; // Default: "com.tentel.RUN_COMMAND_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"
            /** Intent {@code String} extra for the comma alternative characters in arguments that should be replaced instead of the default {@link #COMMA_ALTERNATIVE} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS = tentel_PACKAGE_NAME + ".RUN_COMMAND_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"; // Default: "com.tentel.RUN_COMMAND_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"

            /** Intent {@code String} extra for stdin of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_STDIN = tentel_PACKAGE_NAME + ".RUN_COMMAND_STDIN"; // Default: "com.tentel.RUN_COMMAND_STDIN"
            /** Intent {@code String} extra for current working directory of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_WORKDIR = tentel_PACKAGE_NAME + ".RUN_COMMAND_WORKDIR"; // Default: "com.tentel.RUN_COMMAND_WORKDIR"
            /** Intent {@code boolean} extra for whether to run command in background or foreground terminal session for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_BACKGROUND = tentel_PACKAGE_NAME + ".RUN_COMMAND_BACKGROUND"; // Default: "com.tentel.RUN_COMMAND_BACKGROUND"
            /** Intent {@code String} extra for custom log level for background commands defined by {@link com.tentel.shared.logger.Logger} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_BACKGROUND_CUSTOM_LOG_LEVEL = tentel_PACKAGE_NAME + ".RUN_COMMAND_BACKGROUND_CUSTOM_LOG_LEVEL"; // Default: "com.tentel.RUN_COMMAND_BACKGROUND_CUSTOM_LOG_LEVEL"
            /** Intent {@code String} extra for session action of foreground commands for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_SESSION_ACTION = tentel_PACKAGE_NAME + ".RUN_COMMAND_SESSION_ACTION"; // Default: "com.tentel.RUN_COMMAND_SESSION_ACTION"
            /** Intent {@code String} extra for label of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_LABEL = tentel_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_LABEL"; // Default: "com.tentel.RUN_COMMAND_COMMAND_LABEL"
            /** Intent markdown {@code String} extra for description of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_DESCRIPTION = tentel_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_DESCRIPTION"; // Default: "com.tentel.RUN_COMMAND_COMMAND_DESCRIPTION"
            /** Intent markdown {@code String} extra for help of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_HELP = tentel_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_HELP"; // Default: "com.tentel.RUN_COMMAND_COMMAND_HELP"
            /** Intent {@code Parcelable} extra for the pending intent that should be sent with the result of the execution command to the execute command caller for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_PENDING_INTENT = tentel_PACKAGE_NAME + ".RUN_COMMAND_PENDING_INTENT"; // Default: "com.tentel.RUN_COMMAND_PENDING_INTENT"
            /** Intent {@code String} extra for the directory path in which to write the result of
             * the execution command for the execute command caller for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_DIRECTORY = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_DIRECTORY"; // Default: "com.tentel.RUN_COMMAND_RESULT_DIRECTORY"
            /** Intent {@code boolean} extra for whether the result should be written to a single file
             * or multiple files (err, errmsg, stdout, stderr, exit_code) in
             * {@link #EXTRA_RESULT_DIRECTORY} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_SINGLE_FILE = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_SINGLE_FILE"; // Default: "com.tentel.RUN_COMMAND_RESULT_SINGLE_FILE"
            /** Intent {@code String} extra for the basename of the result file that should be created
             * in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is {@code true}
             * for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_FILE_BASENAME = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_BASENAME"; // Default: "com.tentel.RUN_COMMAND_RESULT_FILE_BASENAME"
            /** Intent {@code String} extra for the output {@link Formatter} format of the
             * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_FILE_OUTPUT_FORMAT = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_OUTPUT_FORMAT"; // Default: "com.tentel.RUN_COMMAND_RESULT_FILE_OUTPUT_FORMAT"
            /** Intent {@code String} extra for the error {@link Formatter} format of the
             * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_FILE_ERROR_FORMAT = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_ERROR_FORMAT"; // Default: "com.tentel.RUN_COMMAND_RESULT_FILE_ERROR_FORMAT"
            /** Intent {@code String} extra for the optional suffix of the result files that should be
             * created in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is
             * {@code false} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_RESULT_FILES_SUFFIX = tentel_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILES_SUFFIX"; // Default: "com.tentel.RUN_COMMAND_RESULT_FILES_SUFFIX"

        }
    }





    /**
     * tentel class to send back results of commands to their callers like plugin or 3rd party apps.
     */
    public static final class RESULT_SENDER {

        /*
         * The default `Formatter` format strings to use for `ResultConfig#resultFileBasename`
         * if `ResultConfig#resultSingleFile` is `true`.
         */

        /** The {@link Formatter} format string for success if only `stdout` needs to be written to
         * {@link ResultConfig#resultFileBasename} where `stdout` maps to `%1$s`.
         * This is used when `err` equals {@link Errno#ERRNO_SUCCESS} (-1) and `stderr` is empty
         * and `exit_code` equals `0` and {@link ResultConfig#resultFileOutputFormat} is not passed. */
        public static final String FORMAT_SUCCESS_STDOUT = "%1$s%n";
        /** The {@link Formatter} format string for success if `stdout` and `exit_code` need to be written to
         * {@link ResultConfig#resultFileBasename} where `stdout` maps to `%1$s` and `exit_code` to `%2$s`.
         * This is used when `err` equals {@link Errno#ERRNO_SUCCESS} (-1) and `stderr` is empty
         * and `exit_code` does not equal `0` and {@link ResultConfig#resultFileOutputFormat} is not passed.
         * The exit code will be placed in a markdown inline code. */
        public static final String FORMAT_SUCCESS_STDOUT__EXIT_CODE = "%1$s%n%n%n%nexit_code=%2$s%n";
        /** The {@link Formatter} format string for success if `stdout`, `stderr` and `exit_code` need to be
         * written to {@link ResultConfig#resultFileBasename} where `stdout` maps to `%1$s`, `stderr`
         * maps to `%2$s` and `exit_code` to `%3$s`.
         * This is used when `err` equals {@link Errno#ERRNO_SUCCESS} (-1) and `stderr` is not empty
         * and {@link ResultConfig#resultFileOutputFormat} is not passed.
         * The stdout and stderr will be placed in a markdown code block. The exit code will be placed
         * in a markdown inline code. The surrounding backticks will be 3 more than the consecutive
         * backticks in any parameter itself for code blocks. */
        public static final String FORMAT_SUCCESS_STDOUT__STDERR__EXIT_CODE = "stdout=%n%1$s%n%n%n%nstderr=%n%2$s%n%n%n%nexit_code=%3$s%n";
        /** The {@link Formatter} format string for failure if `err`, `errmsg`(`error`), `stdout`,
         * `stderr` and `exit_code` need to be written to {@link ResultConfig#resultFileBasename} where
         * `err` maps to `%1$s`, `errmsg` maps to `%2$s`, `stdout` maps
         * to `%3$s`, `stderr` to `%4$s` and `exit_code` maps to `%5$s`.
         * Do not define an argument greater than `5`, like `%6$s` if you change this value since it will
         * raise {@link IllegalFormatException}.
         * This is used when `err` does not equal {@link Errno#ERRNO_SUCCESS} (-1) and
         * {@link ResultConfig#resultFileErrorFormat} is not passed.
         * The errmsg, stdout and stderr will be placed in a markdown code block. The err and exit code
         * will be placed in a markdown inline code. The surrounding backticks will be 3 more than
         * the consecutive backticks in any parameter itself for code blocks. The stdout, stderr
         * and exit code may be empty without any surrounding backticks if not set. */
        public static final String FORMAT_FAILED_ERR__ERRMSG__STDOUT__STDERR__EXIT_CODE = "err=%1$s%n%n%n%nerrmsg=%n%2$s%n%n%n%nstdout=%n%3$s%n%n%n%nstderr=%n%4$s%n%n%n%nexit_code=%5$s%n";



        /*
         * The default prefixes to use for result files under `ResultConfig#resultDirectoryPath`
         * if `ResultConfig#resultSingleFile` is `false`.
         */

        /** The prefix for the err result file. */
        public static final String RESULT_FILE_ERR_PREFIX = "err";
        /** The prefix for the errmsg result file. */
        public static final String RESULT_FILE_ERRMSG_PREFIX = "errmsg";
        /** The prefix for the stdout result file. */
        public static final String RESULT_FILE_STDOUT_PREFIX = "stdout";
        /** The prefix for the stderr result file. */
        public static final String RESULT_FILE_STDERR_PREFIX = "stderr";
        /** The prefix for the exitCode result file. */
        public static final String RESULT_FILE_EXIT_CODE_PREFIX = "exit_code";

    }





    /**
     * tentel:API app constants.
     */
    public static final class tentel_API {

        /** tentel:API app core activity name. */
        public static final String tentel_API_ACTIVITY_NAME = tentel_API_PACKAGE_NAME + ".activities.tentelAPIActivity"; // Default: "com.tentel.tasker.activities.tentelAPIActivity"

    }





    /**
     * tentel:Float app constants.
     */
    public static final class tentel_FLOAT_APP {

        /** tentel:Float app core service name. */
        public static final String tentel_FLOAT_SERVICE_NAME = tentel_FLOAT_PACKAGE_NAME + ".tentelFloatService"; // Default: "com.tentel.window.tentelFloatService"

        /**
         * tentel:Float app core service.
         */
        public static final class tentel_FLOAT_SERVICE {

            /** Intent action to stop tentel_FLOAT_SERVICE. */
            public static final String ACTION_STOP_SERVICE = tentel_FLOAT_PACKAGE_NAME + ".ACTION_STOP_SERVICE"; // Default: "com.tentel.float.ACTION_STOP_SERVICE"

            /** Intent action to show float window. */
            public static final String ACTION_SHOW = tentel_FLOAT_PACKAGE_NAME + ".ACTION_SHOW"; // Default: "com.tentel.float.ACTION_SHOW"

            /** Intent action to hide float window. */
            public static final String ACTION_HIDE = tentel_FLOAT_PACKAGE_NAME + ".ACTION_HIDE"; // Default: "com.tentel.float.ACTION_HIDE"

        }

    }





    /**
     * tentel:Styling app constants.
     */
    public static final class tentel_STYLING {

        /** tentel:Styling app core activity name. */
        public static final String tentel_STYLING_ACTIVITY_NAME = tentel_STYLING_PACKAGE_NAME + ".tentelStyleActivity"; // Default: "com.tentel.styling.tentelStyleActivity"

    }





    /**
     * tentel:Tasker app constants.
     */
    public static final class tentel_TASKER {

        /** tentel:Tasker app core activity name. */
        public static final String tentel_TASKER_ACTIVITY_NAME = tentel_TASKER_PACKAGE_NAME + ".activities.tentelTaskerActivity"; // Default: "com.tentel.tasker.activities.tentelTaskerActivity"

    }





    /**
     * tentel:Widget app constants.
     */
    public static final class tentel_WIDGET {

        /** tentel:Widget app core activity name. */
        public static final String tentel_WIDGET_ACTIVITY_NAME = tentel_WIDGET_PACKAGE_NAME + ".activities.tentelWidgetActivity"; // Default: "com.tentel.widget.activities.tentelWidgetActivity"


        /**  Intent {@code String} extra for the token of the tentel:Widget app shortcuts. */
        public static final String EXTRA_TOKEN_NAME = tentel_PACKAGE_NAME + ".shortcut.token"; // Default: "com.tentel.shortcut.token"

        /**
         * tentel:Widget app {@link android.appwidget.AppWidgetProvider} class.
         */
        public static final class tentel_WIDGET_PROVIDER {

            /** Intent action for if an item is clicked in the widget. */
            public static final String ACTION_WIDGET_ITEM_CLICKED = tentel_WIDGET_PACKAGE_NAME + ".ACTION_WIDGET_ITEM_CLICKED"; // Default: "com.tentel.widget.ACTION_WIDGET_ITEM_CLICKED"


            /** Intent action to refresh files in the widget. */
            public static final String ACTION_REFRESH_WIDGET = tentel_WIDGET_PACKAGE_NAME + ".ACTION_REFRESH_WIDGET"; // Default: "com.tentel.widget.ACTION_REFRESH_WIDGET"


            /**  Intent {@code String} extra for the file clicked for the tentel_WIDGET_PROVIDER.ACTION_WIDGET_ITEM_CLICKED intent. */
            public static final String EXTRA_FILE_CLICKED = tentel_WIDGET_PACKAGE_NAME + ".EXTRA_FILE_CLICKED"; // Default: "com.tentel.widget.EXTRA_FILE_CLICKED"

        }

    }

}
