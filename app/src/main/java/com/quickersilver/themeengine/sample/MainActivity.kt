package com.quickersilver.themeengine.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.quickersilver.themeengine.ThemeChooserDialog
import com.quickersilver.themeengine.ThemeEngine
import com.quickersilver.themeengine.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeEngine.applyToActivity(this)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val themeEngine = ThemeEngine.getInstance(this)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            SettingsFragment().show(supportFragmentManager, "Settings")
        }
        return super.onOptionsItemSelected(item)
    }
}