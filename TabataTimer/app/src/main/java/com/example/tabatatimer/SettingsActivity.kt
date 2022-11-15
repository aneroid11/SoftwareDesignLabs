package com.example.tabatatimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.Theme_TabataTimer_LargeText)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsFragment = SettingsFragment()
        //settingsFragment.activity = this
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, settingsFragment)
            .commit()
    }
}