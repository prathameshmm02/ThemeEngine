package com.quickersilver.themeengine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class ThemeEngine(context: Context) {
    private val mEditor = PreferenceManager.getDefaultSharedPreferences(context)

    var isDynamicTheme
        get() = mEditor.getBoolean(DYNAMIC_THEME, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        set(value) = mEditor.edit { putBoolean(DYNAMIC_THEME, value) }

    fun getTheme(): Int {
        return if (isDynamicTheme) R.style.Theme_ThemeEngine_Dynamic else staticTheme
    }

    var staticTheme
        get() = mEditor.getInt(APP_THEME, R.style.Theme_ThemeEngine_Blue)
        set(value) = mEditor.edit { putInt(APP_THEME, value) }

    fun resetTheme() {
        mEditor.edit { remove(APP_THEME) }
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

        fun applyToActivities(application: Application) {
            application.registerActivityLifecycleCallbacks(ThemeEngineActivityCallback())
        }

        fun applyToActivity(activity: Activity) {
            activity.setTheme(getInstance(activity).getTheme())
        }

        const val DYNAMIC_THEME = "dynamic_theme"
        const val APP_THEME = "app_theme"
    }
}

private class ThemeEngineActivityCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityPreCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        activity.setTheme(ThemeEngine.getInstance(activity).getTheme())
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}