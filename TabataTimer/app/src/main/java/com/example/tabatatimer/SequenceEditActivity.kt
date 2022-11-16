package com.example.tabatatimer

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.NonCancellable.children


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

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })

        setCurrentColor()

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

    private fun setCurrentColor() {
        val colorPickerGroup: RadioGroup = findViewById(R.id.color_picker)
        val count: Int = colorPickerGroup.childCount
        Log.d("SequenceEditActivity", "count = $count")
        Log.d("SequenceEditActivity", "currSeq.color = ${currSeq.color}")

        val listOfButtons = ArrayList<RadioButton>()

        for (i in 0 until count) {
            listOfButtons.add(colorPickerGroup.getChildAt(i) as RadioButton)
        }

        for (button in listOfButtons) {
            val clDrawable: ColorDrawable = button.background as ColorDrawable
            val color: Int = clDrawable.color
            Log.d("SequenceEditActivity", "current button color: $color")
            button.isChecked = currSeq.color == color
        }
    }

    private fun handleBackPressed() {
        val context = this
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        finish()
    }

    private fun saveCurrentSequence() {
        val colorPickerGroup: RadioGroup = findViewById(R.id.color_picker)
        val selectedColor: RadioButton = colorPickerGroup.findViewById(colorPickerGroup.checkedRadioButtonId)
        val clDrawable: ColorDrawable = selectedColor.background as ColorDrawable
        val color: Int = clDrawable.color
        currSeq.color = color

        sequencesViewModel.updateSequence(seqPos, currSeq)
        sequencesViewModel.writeSequencesToFile()
    }
}