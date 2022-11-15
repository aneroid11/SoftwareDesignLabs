package com.example.tabatatimer

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.math.min
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

    private fun countTotalSeconds(seq: Sequence): Int {
        var sum = 0

        for (ph in seq.phasesList) {
            sum += ph.durationSec
        }
        return sum * seq.numRepetitions
    }

    private fun getTotalTimeInMinutes(seconds: Int): String {
        val minutes = seconds / 60
        val restSeconds = seconds - minutes * 60
        return String.format("%d:%d", minutes, restSeconds)
    }

    private fun getPhasesListText(seq: Sequence): String {
        var index = 1
        var retStr = ""

        for (ph in seq.phasesList) {
            retStr += index.toString() + ". " + ph.type + " - " + ph.durationSec + " sec\n"
            index++
        }
        return retStr
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        val seq: Sequence = seqViewModel.sequencesList.value!![position]

        holder.cardView.setBackgroundColor(seq.color)
        holder.seqTitleView.text = seq.title
        holder.numRepetitionsView.text = "Repetitions: " + seq.numRepetitions.toString()
        holder.totalTimeView.text = getTotalTimeInMinutes(countTotalSeconds(seq))
        holder.phasesListView.text = getPhasesListText(seq)
    }

    override fun getItemCount(): Int {
        return seqViewModel.sequencesList.value!!.size
    }
}