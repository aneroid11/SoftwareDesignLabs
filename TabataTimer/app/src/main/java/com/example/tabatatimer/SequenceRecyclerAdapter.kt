package com.example.tabatatimer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class SequenceRecyclerAdapter(private val names: List<String>) :
    RecyclerView.Adapter<SequenceRecyclerAdapter.SequenceViewHolder>() {

    class SequenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.card)
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

    private fun getRandomColor(): Int {
        val red = Random.nextInt() % 50 + 130
        val green = Random.nextInt() % 50 + 130
        val blue = Random.nextInt() % 50 + 130
        return Color.rgb(red, green, blue)
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        holder.bodyTextView.text = "(a sequence)"
        holder.titleTextView.text = names[position]
        val color: Int = getRandomColor()
        holder.cardView.setBackgroundColor(color)
    }

    override fun getItemCount(): Int {
        return names.size
    }
}