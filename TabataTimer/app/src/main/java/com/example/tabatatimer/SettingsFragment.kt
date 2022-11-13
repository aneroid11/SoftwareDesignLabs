package com.example.tabatatimer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val nightThemePref = findPreference("night_theme_on")
        val fontSizePref = findPreference("font_size")
        val clearDataPref = findPreference("clear_data")
        val selectLangPref = findPreference("select_lang")

        nightThemePref.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                Log.d("SettingsFragment", "Changed the app theme!")
                updateTheme()
                return@OnPreferenceClickListener true
            }

        selectLangPref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, value: Any ->
                Log.d("SettingsFragment", "Language changed to $value")
                updateLanguage(value.toString())
                return@OnPreferenceChangeListener true
            }
    }

    private fun updateTheme() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val nightTheme: Boolean =
            sharedPreferences.getBoolean("night_theme_on", false)

        if (nightTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateLanguage(lang: String) {
        Log.d("SettingsFragment", "lang = $lang")

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}