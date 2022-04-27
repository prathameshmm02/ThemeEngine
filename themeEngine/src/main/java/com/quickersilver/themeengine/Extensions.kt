package com.quickersilver.themeengine

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

val Context.isDarkMode
    get() = (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

@ChecksSdkIntAtLeast(api = 31)
fun hasS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
