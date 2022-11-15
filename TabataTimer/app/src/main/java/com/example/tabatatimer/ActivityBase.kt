package com.example.tabatatimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager

abstract class ActivityBase : AppCompatActivity() {
    protected fun setTextTheme() {
        // get text theme from shared settings and set it here
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val mediumStr = getString(R.string.medium_eng)
        val textSize = prefs.getString("font_size", mediumStr)!!

        Log.d("MainActivity", "textSize = $textSize")

        if (textSize == mediumStr) {
            setTheme(R.style.Theme_TabataTimer_MediumText)
        }
        else {
            setTheme(R.style.Theme_TabataTimer_LargeText)
        }
    }
}