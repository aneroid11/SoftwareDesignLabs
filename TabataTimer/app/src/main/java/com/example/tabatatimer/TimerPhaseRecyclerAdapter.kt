package com.example.tabatatimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TimerPhaseRecyclerAdapter(
    private var currSequence: Sequence,
    private val activity: TimerActivity
) : RecyclerView.Adapter<TimerPhaseRecyclerAdapter.TimerPhaseViewHolder>() {
    class TimerPhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val phaseButton: Button = itemView.findViewById(R.id.timer_phase_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerPhaseViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_timer_phase_item, parent, false)
        return TimerPhaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimerPhaseViewHolder, position: Int) {
        val currPhaseType: String = currSequence.phasesList[position].type
        val phaseName = activity.getString(activity.resources.getIdentifier(currPhaseType, "string", activity.packageName))
        holder.phaseButton.text = (position + 1).toString() + ". " + phaseName
    }

    override fun getItemCount(): Int {
        return currSequence.phasesList.size
    }
}