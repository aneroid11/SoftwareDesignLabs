package com.example.converter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
// import androidx.lifecycle.ViewModelProvider
// import androidx.fragment.app.viewModels
import com.example.converter.databinding.FragmentDataBinding

class DataFragment : Fragment() {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    //private val dataViewModel: DataViewModel by viewModels()
    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerItemLayoutId: Int = android.R.layout.simple_spinner_dropdown_item

        binding.unitCategorySpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            arrayOf("distance", "mass", "currency")
        )
        binding.sourceUnitSpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            arrayOf("meters", "feet", "inches")
        )
        binding.destinationUnitSpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            arrayOf("meters", "feet", "inches")
        )

        dataViewModel.sourceValue.observe(viewLifecycleOwner) {
            Log.d("DataFragment", "sourceValue observer lambda called")
            binding.sourceValue.text = it.toString()
        }
        // dataViewModel.destinationValue.observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}