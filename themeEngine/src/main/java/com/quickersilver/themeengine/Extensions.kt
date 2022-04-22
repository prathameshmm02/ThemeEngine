package com.quickersilver.themeengine

import android.content.Context
import android.content.res.Configuration

val Context.isDarkMode
    get() = (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES