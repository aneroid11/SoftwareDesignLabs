package com.example.tabatatimer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlin.math.min
import kotlin.random.Random

class SequenceRecyclerAdapter(
    private val seqViewModel: SequencesViewModel,
    private val activityContext: Context,
    private val parentActivity: MainActivity
) : RecyclerView.Adapter<SequenceRecyclerAdapter.SequenceViewHolder>() {

    class SequenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.card)
        val seqTitleView: TextView = itemView.findViewById(R.id.sequence_title)
        val numRepetitionsView: TextView = itemView.findViewById(R.id.num_of_repetitions)
        val totalTimeView: TextView = itemView.findViewById(R.id.total_time)
        val phasesListView: TextView = itemView.findViewById(R.id.list_of_phases)
        val deleteButton: Button = itemView.findViewById(R.id.delete_sequence_button)
        val editButton: Button = itemView.findViewById(R.id.edit_sequence_button)
        val runButton: Button = itemView.findViewById(R.id.run_sequence_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SequenceViewHolder {
        val itemView: View =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_sequence_item, parent, false)
        return SequenceViewHolder(itemView)
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
        return String.format("%02d:%02d", minutes, restSeconds)
    }

    private fun getPhasesListText(seq: Sequence): String {
        var index = 1
        var retStr = ""

        for (ph in seq.phasesList) {
            val phaseType = when(ph.type) {
                "warmup" -> activityContext.getString(R.string.warmup)
                "cooldown" -> activityContext.getString(R.string.cooldown)
                "rest" -> activityContext.getString(R.string.rest)
                else -> activityContext.getString(R.string.work)
            }

            retStr += index.toString() + ". " + phaseType + " - " + ph.durationSec +
                    " " + activityContext.getString(R.string.sec) + "\n"
            index++
        }
        return retStr
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        val seqList = seqViewModel.sequencesList.value!!
        val seq: Sequence = seqList[position]

        holder.cardView.setBackgroundColor(seq.color)
        holder.seqTitleView.text = seq.title
        holder.numRepetitionsView.text =
            activityContext.getString(R.string.repetitions) + " " + seq.numRepetitions.toString()
        holder.totalTimeView.text = getTotalTimeInMinutes(countTotalSeconds(seq))
        holder.phasesListView.text = getPhasesListText(seq)

        holder.deleteButton.setOnClickListener {
            seqViewModel.deleteSequence(position)
            seqViewModel.writeSequencesToFile()
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, seqList.size)
        }
        holder.editButton.setOnClickListener {
            val intent = Intent(activityContext, SequenceEditActivity::class.java)
            intent.putExtra("currentSequencePosition", position)
            activityContext.startActivity(intent)
            parentActivity.finish()
        }
        holder.runButton.setOnClickListener {
            Log.d("SequenceRecyclerAdapter", "start timer")
            val intent = Intent(activityContext, TimerActivity::class.java)
            intent.putExtra("currentSequencePosition", position)
            activityContext.startActivity(intent)
            parentActivity.finish()
        }
    }

    override fun getItemCount(): Int {
        return seqViewModel.sequencesList.value!!.size
    }
}