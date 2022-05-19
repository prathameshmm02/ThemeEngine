package com.quickersilver.themeengine.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quickersilver.themeengine.ThemeChooserDialogBuilder
import com.quickersilver.themeengine.ThemeEngine
import com.quickersilver.themeengine.ThemeMode
import com.quickersilver.themeengine.hasS
import com.quickersilver.themeengine.sample.databinding.FragmentSettingsBinding

class SettingsFragment : BottomSheetDialogFragment() {

    private lateinit var themeEngine: ThemeEngine

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        themeEngine = ThemeEngine.getInstance(requireContext())
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (hasS()) {
            binding.dynamicGroup.check(if (themeEngine.isDynamicTheme) R.id.dynamic_on else R.id.dynamic_off)
            binding.dynamicGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.dynamic_off -> themeEngine.isDynamicTheme = false
                        R.id.dynamic_on -> themeEngine.isDynamicTheme = true
                    }
                    requireActivity().recreate()
                }
            }
        } else {
            binding.dynamicColorLabel.isVisible = false
            binding.dynamicGroup.isVisible = false
        }
        binding.themeGroup.check(
            when (themeEngine.themeMode) {
                ThemeMode.AUTO -> R.id.auto_theme
                ThemeMode.LIGHT -> R.id.light_theme
                ThemeMode.DARK -> R.id.dark_theme
                else -> R.id.auto_theme
            }
        )

        binding.trueBlackGroup.check(if (themeEngine.isTrueBlack) R.id.true_black_on else R.id.true_black_off)
        binding.trueBlackGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                themeEngine.isTrueBlack = when (checkedId) {
                    R.id.true_black_on -> true
                    else -> false
                }
                requireActivity().recreate()
            }
        }

        binding.themeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                themeEngine.themeMode = when (checkedId) {
                    R.id.auto_theme -> ThemeMode.AUTO
                    R.id.light_theme -> ThemeMode.LIGHT
                    R.id.dark_theme -> ThemeMode.DARK
                    else -> ThemeMode.AUTO
                }
            }
        }
        binding.changeTheme.setOnClickListener {
            ThemeChooserDialogBuilder(requireContext())
                .setTitle(R.string.choose_theme)
                .setPositiveButton("OK") { _, theme ->
                    themeEngine.staticTheme = theme
                    requireActivity().recreate()
                }
                .setNegativeButton("Cancel")
                .setNeutralButton("Default") { _, _ ->
                    themeEngine.resetTheme()
                    requireActivity().recreate()
                }
                .setIcon(R.drawable.ic_round_brush)
                .create()
                .show()
        }
    }
}