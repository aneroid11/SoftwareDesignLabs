package com.example.tabatatimer

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhaseRecyclerAdapter(
    private var currSequence: Sequence,
    private val activity: SequenceEditActivity
) : RecyclerView.Adapter<PhaseRecyclerAdapter.PhaseViewHolder>() {

    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeRadioGroup: RadioGroup = itemView.findViewById(R.id.phase_type_selector)
        //val descrEditText: EditText = itemView.findViewById(R.id.phase_descr_edittext)
        val durationTextView: TextView = itemView.findViewById(R.id.phase_duration_sec_textview)

        /*fun setListenersForEditTexts() {
            descrEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d("PhaseRecyclerAdapter", "description changed in position: $absoluteAdapterPosition")
                    //phase.description = s.toString()
                }
            })
            durationEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    /*try {
                        phase.durationSec = s.toString().toInt()
                    }
                    catch (e: Exception) {
                        phase.durationSec = 1
                    }

                    if (phase.durationSec == 0) { phase.durationSec = 1 }*/
                }
            })
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhaseViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_phase_item, parent, false)
        val phaseViewHolder = PhaseViewHolder(itemView)
        //phaseViewHolder.setListenersForEditTexts()
        return phaseViewHolder
    }

    override fun onBindViewHolder(holder: PhaseViewHolder, position: Int) {
        val phase = currSequence.phasesList[position]

        holder.durationTextView.setText(phase.durationSec.toString())

        val phasesTypes = listOf("warmup", "work", "rest", "cooldown")

        val count: Int = holder.typeRadioGroup.childCount
        for (i in 0 until count) {
            val currButton = holder.typeRadioGroup.getChildAt(i) as RadioButton
            val id: Int = activity.resources.getIdentifier(phase.type, "string", activity.packageName)
            currButton.isChecked = currButton.text == activity.getString(id)

            currButton.setOnClickListener {
                currSequence.phasesList[position].type = phasesTypes[i]
            }
        }
    }

    override fun getItemCount(): Int {
        return currSequence.phasesList.size
    }
}