package com.example.converter

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.converter.databinding.FragmentNumpadBinding

class NumpadFragment : Fragment(), NumpadClickHandler {
    private var _binding: FragmentNumpadBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val dataViewModel: DataViewModel by activityViewModels()

    private var dotPressed: Boolean = false

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
        if (pressedKey.isDigit()) {
            val digitValue: Int = pressedKey.toString().toInt()
            var nextSourceValue: Double = dataViewModel.sourceValue.value!!

            if (!dotPressed) {
                nextSourceValue = nextSourceValue.times(10)
                nextSourceValue = nextSourceValue.plus(digitValue)
            } else {
                nextSourceValue = digitValue.toDouble()
            }

            dataViewModel.setSourceValue(nextSourceValue)
        }
        else if (pressedKey == '.') {
            dotPressed = true
        }
        else if (pressedKey == 'C') {
            dataViewModel.setSourceValue(0.0)
            dotPressed = false
        }
    }
}