package com.example.converter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.converter.databinding.FragmentNumpadBinding

class NumpadFragment : Fragment() {
    private var _binding: FragmentNumpadBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumpadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.numpadRecyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}