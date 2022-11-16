package com.example.tabatatimer

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView

class PhaseRecyclerAdapter(
    private var currSequence: Sequence,
    private val activity: SequenceEditActivity
) : RecyclerView.Adapter<PhaseRecyclerAdapter.PhaseViewHolder>() {

    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeRadioGroup: RadioGroup = itemView.findViewById(R.id.phase_type_selector)
        val descrEditText: EditText = itemView.findViewById(R.id.phase_descr_edittext)
        val durationEditText: EditText = itemView.findViewById(R.id.phase_duration_sec_edittext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhaseViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_phase_item, parent, false)
        return PhaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhaseViewHolder, position: Int) {
        val phase = currSequence.phasesList[position]
        holder.descrEditText.setText(phase.description)
        holder.durationEditText.setText(phase.durationSec.toString())

        val count: Int = holder.typeRadioGroup.childCount
        for (i in 0 until count) {
            val currButton = holder.typeRadioGroup.getChildAt(i) as RadioButton
            val id: Int = activity.resources.getIdentifier(phase.type, "string", activity.packageName)
            currButton.isChecked = currButton.text == activity.getString(id)
        }
    }

    override fun getItemCount(): Int {
        return currSequence.phasesList.size
    }
}