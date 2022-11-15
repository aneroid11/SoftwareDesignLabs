package com.example.tabatatimer

import android.os.Bundle

class SequenceEditActivity : ActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequence_edit)
    }
}