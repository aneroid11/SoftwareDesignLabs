package com.example.tabatatimer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class SequenceEditActivity : ActivityBase() {
    private val sequencesViewModel: SequencesViewModel by viewModels()
    private var seqPos: Int = 0
    private lateinit var currSeq: Sequence

    override fun onCreate(savedInstanceState: Bundle?) {
        seqPos = intent.getIntExtra("currentSequencePosition", 0)

        // this is just cloning a sequence
        currSeq = Gson().fromJson(
            Gson().toJson(sequencesViewModel.sequencesList.value!![seqPos], Sequence::class.java),
            Sequence::class.java
        )

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
            saveCurrentSequence()
            onBackPressedDispatcher.onBackPressed()

            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        val sequenceTitleEdittext: EditText = findViewById(R.id.sequence_title_edittext)
        sequenceTitleEdittext.setText(currSeq.title)

        sequenceTitleEdittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                currSeq.title = s.toString()
            }
        })
    }

    private fun saveCurrentSequence() {
        sequencesViewModel.updateSequence(seqPos, currSeq)
        sequencesViewModel.writeSequencesToFile()
    }
}