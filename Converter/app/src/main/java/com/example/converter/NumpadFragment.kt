package com.example.converter

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.converter.databinding.FragmentNumpadBinding

class NumpadFragment : Fragment(), NumpadClickHandler {
    private var _binding: FragmentNumpadBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    //private val dataViewModel: DataViewModel by viewModels()
    private val dataViewModel: DataViewModel by activityViewModels()

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
        // this also can be important
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = NumAdapter(this) // IMPORTANT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleClick(pressedKey: Char) {
        Log.d("NumpadFragment", "handleClick(\'$pressedKey\')")

        if (pressedKey.isDigit()) {
            val value = pressedKey.toString().toDouble()
            Log.d("NumpadFragment", "setSourceValue($value)")

            dataViewModel.setSourceValue(value)
        }
    }
}