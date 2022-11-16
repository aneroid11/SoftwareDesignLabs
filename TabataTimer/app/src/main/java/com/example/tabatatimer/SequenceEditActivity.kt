package com.example.tabatatimer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SequenceEditActivity : ActivityBase() {
    private val sequencesViewModel: SequencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTextTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequence_edit)

        val phasesRecyclerView: RecyclerView = findViewById(R.id.phases_recyclerview)
        phasesRecyclerView.layoutManager = LinearLayoutManager(this)
        phasesRecyclerView.adapter =
            PhaseRecyclerAdapter(
                sequencesViewModel.sequencesList.value!![0]
            )
    }
}