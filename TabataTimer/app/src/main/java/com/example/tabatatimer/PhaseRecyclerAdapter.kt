package com.example.tabatatimer

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PhaseRecyclerAdapter(
    private var currSequence: Sequence,
    private val activity: SequenceEditActivity
) : RecyclerView.Adapter<PhaseRecyclerAdapter.PhaseViewHolder>() {

    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeRadioGroup: RadioGroup = itemView.findViewById(R.id.phase_type_selector)
        val durationTextView: TextView = itemView.findViewById(R.id.phase_duration_sec_textview)
        val increaseDurationButton: Button = itemView.findViewById(R.id.increase_duration_button)
        val decreaseDurationButton: Button = itemView.findViewById(R.id.decrease_duration_button)
        val deletePhaseButton: Button = itemView.findViewById(R.id.delete_phase_button)
        val moveUpButton: Button = itemView.findViewById(R.id.move_phase_up_button)
        val moveDownButton: Button = itemView.findViewById(R.id.move_phase_down_button)
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

    private fun swapItems(pos1: Int, pos2: Int) {
        Collections.swap(currSequence.phasesList, pos1, pos2)
        //notifyItemMoved(pos1, pos2)
        val minPos = kotlin.math.min(pos1, pos2)
        notifyItemRangeChanged(minPos, currSequence.phasesList.size)
        //notifyItemRangeChanged(pos1, currSequence.phasesList.size)
    }

    override fun onBindViewHolder(holder: PhaseViewHolder, position: Int) {
        val phase = currSequence.phasesList[position]

        holder.durationTextView.text = phase.durationSec.toString()

        holder.increaseDurationButton.setOnClickListener {
            phase.durationSec++
            if (phase.durationSec > 999) { phase.durationSec = 999 }

            holder.durationTextView.text = phase.durationSec.toString()
        }
        holder.decreaseDurationButton.setOnClickListener {
            phase.durationSec--
            if (phase.durationSec < 1) {
                phase.durationSec = 1
                Toast.makeText(activity, activity.getString(R.string.cannot_set_duration_less), Toast.LENGTH_SHORT)
                    .show()
            }

            holder.durationTextView.text = phase.durationSec.toString()
        }

        holder.deletePhaseButton.setOnClickListener {
            if (currSequence.phasesList.size < 2) {
                Toast.makeText(activity, activity.getString(R.string.cannot_delete_all_phases), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currSequence.phasesList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, currSequence.phasesList.size)
        }

        holder.moveUpButton.setOnClickListener {
            Log.d("PhaseRecyclerAdapter", "move up from $position")

            if (position < 1) {
                Toast.makeText(activity, activity.getString(R.string.already_on_top), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            swapItems(position, position - 1)
        }

        holder.moveDownButton.setOnClickListener {
            Log.d("PhaseRecyclerAdapter", "move down from $position")

            if (position + 1 >= currSequence.phasesList.size) {
                Toast.makeText(activity, activity.getString(R.string.already_on_bottom), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            swapItems(position, position + 1)
        }

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