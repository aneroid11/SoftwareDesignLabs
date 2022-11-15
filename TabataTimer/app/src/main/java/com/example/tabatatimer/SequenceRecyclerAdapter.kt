package com.example.tabatatimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SequenceRecyclerAdapter(private val names: List<String>) :
    RecyclerView.Adapter<SequenceRecyclerAdapter.SequenceViewHolder>() {

    class SequenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_text)
        val bodyTextView: TextView = itemView.findViewById(R.id.body_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SequenceViewHolder {
        val itemView: View =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_sequence_item, parent, false)
        return SequenceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        holder.bodyTextView.text = "(a sequence)"
        holder.titleTextView.text = names[position]
    }

    override fun getItemCount(): Int {
        return names.size
    }
}