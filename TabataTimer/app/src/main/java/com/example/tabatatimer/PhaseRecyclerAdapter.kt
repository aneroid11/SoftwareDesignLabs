package com.example.tabatatimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PhaseRecyclerAdapter(
    private val currSequence: Sequence
) : RecyclerView.Adapter<PhaseRecyclerAdapter.PhaseViewHolder>() {

    class PhaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhaseViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_phase_item, parent, false)
        return PhaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhaseViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return currSequence.phasesList.size
    }
}