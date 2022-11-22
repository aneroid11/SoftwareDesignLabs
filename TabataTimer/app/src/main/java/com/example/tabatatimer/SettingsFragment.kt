package com.example.tabatatimer

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.io.File

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val nightThemePref = findPreference("night_theme_on")
        val fontSizePref = findPreference("font_size")
        val clearDataPref = findPreference("clear_data")
        val selectLangPref = findPreference("select_lang")

        clearDataPref.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                Log.d("SettingsFragment", "Delete all data")
                deleteAllData()
                return@OnPreferenceClickListener true
            }

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

        fontSizePref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, value: Any ->
                Log.d("SettingsFragment", "font size changed to $value")
                updateFontSize(value.toString())
                return@OnPreferenceChangeListener true
            }
    }

    private fun deleteAllData() {
        /*val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor: SharedPreferences.Editor =
            prefs.edit()

        editor.putBoolean("night_theme_on", false)
        editor.putString("select_lang", "en")
        editor.putString("font_size", getString(R.string.medium))
        editor.commit()

        updateTheme()
        updateLanguage("en")*/

        val file = File(requireContext().filesDir, "sequences.json")
        file.delete()
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

    private fun updateFontSize(size: String) {
        val sizeStr =
            if (size == getString(R.string.medium_eng))
                getString(R.string.medium)
            else
                getString(R.string.large)

        Toast.makeText(
            requireContext(),
            getString(R.string.font_size_changed) + " " + sizeStr,
            Toast.LENGTH_SHORT
        ).show()
    }
}