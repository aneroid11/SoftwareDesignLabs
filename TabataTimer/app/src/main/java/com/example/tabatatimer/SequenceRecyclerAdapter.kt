package com.example.tabatatimer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class SequenceRecyclerAdapter(private val seqViewModel: SequencesViewModel) :
    RecyclerView.Adapter<SequenceRecyclerAdapter.SequenceViewHolder>() {

    class SequenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.card)
        val seqTitleView: TextView = itemView.findViewById(R.id.sequence_title)
        val numRepetitionsView: TextView = itemView.findViewById(R.id.num_of_repetitions)
        val totalTimeView: TextView = itemView.findViewById(R.id.total_time)
        val phasesListView: TextView = itemView.findViewById(R.id.list_of_phases)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SequenceViewHolder {
        val itemView: View =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_sequence_item, parent, false)
        return SequenceViewHolder(itemView)
    }

    private fun getRandomColor(): Int {
        val red = Random.nextInt() % 50 + 130
        val green = Random.nextInt() % 50 + 130
        val blue = Random.nextInt() % 50 + 130
        return Color.rgb(red, green, blue)
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        val color: Int = getRandomColor()
        holder.cardView.setBackgroundColor(color)
        holder.seqTitleView.text = seqViewModel.sequencesList.value!!.get(position).title
        holder.numRepetitionsView.text = "Repetitions: 4"
        holder.totalTimeView.text = "Total time: 04:00"
        holder.phasesListView.text = "1. warm-up\n2. work\n3. rest\n4. cool down"
    }

    override fun getItemCount(): Int {
        return seqViewModel.sequencesList.value!!.size
    }
}