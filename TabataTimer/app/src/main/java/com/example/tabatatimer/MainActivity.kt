package com.example.tabatatimer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seqRecyclerView: RecyclerView = findViewById(R.id.sequences_recycler_view)
        seqRecyclerView.layoutManager = LinearLayoutManager(this)
        seqRecyclerView.adapter = SequenceRecyclerAdapter(
            listOf(
                "hard training",
                "medium training",
                "easy training",
                "very easy training",
                "very hard training",
                "very very easy training",
                "hard training",
                "medium training",
                "easy training",
                "very easy training",
                "very hard training",
                "very very easy training",
                "hard training",
                "medium training",
                "easy training",
                "very easy training",
                "very hard training",
                "very very easy training"
            )
        )

        updateTheme()  // this is normal
        //updateLanguage() // this is not
    }

    private fun setTextTheme() {
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

    /*private fun updateLanguage() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val lang: String =
            sharedPreferences.getString("select_lang", "en")!!
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_item) {
            Log.d("MainActivity","settings button pressed. start the settings activity")

            val context: Context = this
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}