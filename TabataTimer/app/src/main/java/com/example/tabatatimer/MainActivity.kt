package com.example.tabatatimer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ActivityBase() {
    private val sequencesViewModel: SequencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seqRecyclerView: RecyclerView = findViewById(R.id.sequences_recycler_view)
        seqRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = SequenceRecyclerAdapter(sequencesViewModel, this, this)
        seqRecyclerView.adapter = adapter

        val addSequenceButton: Button = findViewById(R.id.add_sequence_button)
        addSequenceButton.setOnClickListener {
            val numSequences = sequencesViewModel.sequencesList.value!!.size
            sequencesViewModel.sequencesList.value!!.add(
                Sequence(
                    Color.parseColor("#FF8FB391"),
                    getString(R.string.training),
                    1,
                    mutableListOf(
                        Phase("warmup", 20),
                        Phase("work", 15),
                        Phase("rest", 15),
                        Phase("cooldown", 30)
                    )
                )
            )
            sequencesViewModel.writeSequencesToFile()

            val intent = Intent(this, SequenceEditActivity::class.java)
            intent.putExtra("currentSequencePosition", numSequences)
            startActivity(intent)
            finish()
        }

        updateTheme()  // this is normal
        //updateLanguage() // this is not
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