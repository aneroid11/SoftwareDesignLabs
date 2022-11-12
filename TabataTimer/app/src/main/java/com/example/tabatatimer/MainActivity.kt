package com.example.tabatatimer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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