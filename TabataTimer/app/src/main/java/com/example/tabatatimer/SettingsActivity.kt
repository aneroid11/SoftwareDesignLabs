package com.example.tabatatimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SettingsActivity", "onCreate() started")
        //setTheme(R.style.Theme_TabataTimer_LargeText)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsFragment = SettingsFragment()
        //settingsFragment.activity = this
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, settingsFragment)
            .commit()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }

    private fun handleBackPressed() {
        val context = this
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        finish()
    }
}