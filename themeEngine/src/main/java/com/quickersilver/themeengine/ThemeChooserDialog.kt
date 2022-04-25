package com.quickersilver.themeengine

import android.content.Context
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.DialogInterface.BUTTON_POSITIVE
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.quickersilver.themeengine.databinding.RecyclerviewBinding

class ThemeChooserDialog(
    private val context: Context,
    private val themes: List<Int> = Themes.themes,
    private val primaryColorsLight: List<Int> = Themes.primaryColorsLight,
    private val primaryColorsDark: List<Int> = Themes.primaryColorsDark,
) {

    private lateinit var builder: MaterialAlertDialogBuilder

    private lateinit var colorAdapter: ColorAdapter

    init {
        require(themes.size == primaryColorsDark.size) {
            "List size of themes not equal to list size of primary colors"
        }
        require(primaryColorsDark.size == primaryColorsLight.size) {
            "List size of primary colors is different"
        }
        createDialog()
    }

    private fun createDialog() {
        val binding = RecyclerviewBinding.inflate(LayoutInflater.from(context))

        val themeEngine = ThemeEngine.getInstance(context)
        val colorArray = if (context.isDarkMode) {
            primaryColorsDark
        } else {
            primaryColorsLight
        }
        colorAdapter = ColorAdapter(colorArray)
        colorAdapter.setCheckedPosition(themes.indexOf(themeEngine.staticTheme))
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = colorAdapter
        }
        builder = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
    }

    fun setTitle(@StringRes res: Int): ThemeChooserDialog {
        builder.setTitle(res)
        return this
    }

    fun setIcon(@DrawableRes iconId: Int): ThemeChooserDialog {
        builder.setIcon(iconId)
        return this
    }

    fun setPositiveButton(text: String, listener: OnClickListener): ThemeChooserDialog {
        builder.setPositiveButton(text) { _, which ->
            if (which == BUTTON_POSITIVE) {
                val checkedPosition = colorAdapter.checkedPosition
                val theme = themes[checkedPosition]
                listener.onClick(checkedPosition, theme)
            }
        }
        return this
    }

    fun setPositiveButton(@StringRes res: Int, listener: OnClickListener): ThemeChooserDialog {
        setPositiveButton(context.getString(res), listener)
        return this
    }

    fun setNegativeButton(text: String): ThemeChooserDialog {
        builder.setNegativeButton(text, null)
        return this
    }

    fun setNegativeButton(@StringRes res: Int): ThemeChooserDialog {
        builder.setNegativeButton(res, null)
        return this
    }

    fun setNeutralButton(text: String, listener: OnClickListener): ThemeChooserDialog {
        builder.setNeutralButton(text) { _, which ->
            if (which == BUTTON_NEUTRAL) {
                val checkedPosition = colorAdapter.checkedPosition
                val theme = themes[checkedPosition]
                listener.onClick(checkedPosition, theme)
            }
        }
        return this
    }

    fun setNeutralButton(@StringRes res: Int, listener: OnClickListener): ThemeChooserDialog {
        setNeutralButton(context.getString(res), listener)
        return this
    }

    fun create(): AlertDialog {
        return builder.create()
    }

    fun interface OnClickListener {
        fun onClick(position: Int, @StyleRes theme: Int)
    }
}