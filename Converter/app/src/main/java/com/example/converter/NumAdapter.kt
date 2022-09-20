package com.example.converter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class NumAdapter : RecyclerView.Adapter<NumAdapter.NumberViewHolder>() {
    // buttons for the numpad
    private val list = ('0').rangeTo('9').toList() + listOf<Char>('.', 'C')

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class NumberViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.num_button_item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val layout =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.numpad_item_view, parent, false)

        return NumberViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val item: Char = list[position]
        holder.button.text = item.toString()
        holder.button.setOnClickListener {
            TODO("Handle the numpad button click")
            // val action = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment(item.toString())
            // holder.view.findNavController().navigate(action)
        }
    }
}