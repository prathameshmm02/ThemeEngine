package com.quickersilver.themeengine.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quickersilver.themeengine.ThemeChooserDialog
import com.quickersilver.themeengine.ThemeEngine
import com.quickersilver.themeengine.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeEngine.applyToActivity(this)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val themeEngine = ThemeEngine.getInstance(this)

        binding.dynamicTheme.setOnClickListener {
            themeEngine.isDynamicTheme = !themeEngine.isDynamicTheme
            recreate()
        }
        binding.recreateButton.setOnClickListener {
            recreate()
        }
        binding.changeTheme.setOnClickListener {
            ThemeChooserDialog(this)
                .setTitle(R.string.choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    themeEngine.staticTheme = theme
                    recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    themeEngine.resetTheme()
                    recreate()
                }
                .setIcon(R.drawable.ic_round_brush)
                .create()
                .show()
        }
    }
}