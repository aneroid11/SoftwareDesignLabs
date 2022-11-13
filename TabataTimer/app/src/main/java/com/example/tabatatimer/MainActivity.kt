package com.example.tabatatimer

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateTheme()
        updateLocale()
    }

    private fun updateTheme() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val nightTheme: Boolean =
            sharedPreferences.getBoolean("night_theme_on", false)

        if (nightTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun updateLocale() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val lang: String =
            sharedPreferences.getString("select_lang", "en")!!
        val loc = Locale(lang)
        Locale.setDefault(loc)
        val conf = Configuration()
        conf.setLocale(loc)

        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_item) {
            Log.d("MainActivity","settings button pressed. start the settings activity")

            val context: Context = this
            val intent: Intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}