package com.quickersilver.themeengine

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class ThemeEngine(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Returns current ThemeMode.
     * Setting this property applies the given theme mode to the activity.
     */
    var themeMode: ThemeMode
        get() = when (prefs.getInt(THEME_MODE, 0)) {
            0 -> ThemeMode.AUTO
            1 -> ThemeMode.LIGHT
            2 -> ThemeMode.DARK
            else -> ThemeMode.AUTO
        }
        set(value) {
            prefs.edit {
                putInt(
                    THEME_MODE,
                    when (value) {
                        ThemeMode.AUTO -> 0
                        ThemeMode.LIGHT -> 1
                        ThemeMode.DARK -> 2
                    }
                )
            }
            AppCompatDelegate.setDefaultNightMode(
                when (themeMode) {
                    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }

    private val nightMode
        get() = when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    /**
     * Returns true if Dynamic Colors are enabled, false otherwise.
     * Setting this property to true enables dynamic colors, false disables dynamic colors.
     * Keep in mind that dynamic colors will work only on Android 12 i.e. API 31 and higher devices.
     * And call Activity.recreate() after changing this property so that the changes get applied to the activity.
     */
    var isDynamicTheme
        get() = prefs.getBoolean(DYNAMIC_THEME, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) = prefs.edit { putBoolean(DYNAMIC_THEME, value) }

    /**
     * Get current app theme if {@link isDynamicTheme} is enabled or a static theme instead.
     * @return a dynamic theme if
     */
    fun getTheme(): Int {
        return if (isDynamicTheme) R.style.Theme_ThemeEngine_Dynamic else staticTheme
    }

    /**
     * Get current static app theme
     */
    var staticTheme
        get() = prefs.getInt(APP_THEME, R.style.Theme_ThemeEngine_Blue)
        set(value) = prefs.edit { putInt(APP_THEME, value) }

    /**
     * Resets static theme
     */
    fun resetTheme() {
        prefs.edit { remove(APP_THEME) }
    }

    companion object {
        private var INSTANCE: ThemeEngine? = null

        fun getInstance(context: Context): ThemeEngine {
            val currentInstance = INSTANCE

            if (currentInstance != null) {
                return currentInstance
            }

            synchronized(this) {
                val newInstance = ThemeEngine(context)
                INSTANCE = newInstance
                return newInstance
            }
        }

        /**
         * Applies themes and night mode to all activities by registering a ActivityLifecycleCallbacks to your application.
         * @param application Target Application
         */
        fun applyToActivities(application: Application) {
            application.registerActivityLifecycleCallbacks(ThemeEngineActivityCallback())
        }

        /**
         * Applies themes and night mode to given activity
         * @param activity Target activity
         */
        fun applyToActivity(activity: Activity) {
            with(getInstance(activity)) {
                activity.setTheme(getTheme())
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }

        const val THEME_MODE = "theme_mode"
        const val DYNAMIC_THEME = "dynamic_theme"
        const val APP_THEME = "app_theme"
    }
}

private class ThemeEngineActivityCallback : ActivityLifecycleCallbacks {
    override fun onActivityPreCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        ThemeEngine.applyToActivity(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}