package com.example.tabatatimer

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback

class TimerActivity : ActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

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