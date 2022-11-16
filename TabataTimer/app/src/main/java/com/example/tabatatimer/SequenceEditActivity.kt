package com.example.tabatatimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SequenceEditActivity : ActivityBase() {
    private val sequencesViewModel: SequencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val seqPos: Int = intent.getIntExtra("currentSequencePosition", 0)
        val currSeq = sequencesViewModel.sequencesList.value!![seqPos]

        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequence_edit)

        val phasesRecyclerView: RecyclerView = findViewById(R.id.phases_recyclerview)
        phasesRecyclerView.layoutManager = LinearLayoutManager(this)
        phasesRecyclerView.adapter =
            PhaseRecyclerAdapter(
                currSeq
            )

        val saveButton: Button = findViewById(R.id.save_sequence_button)
        saveButton.setOnClickListener {
            /*val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()*/
            saveCurrentSequence()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun saveCurrentSequence() {

    }
}