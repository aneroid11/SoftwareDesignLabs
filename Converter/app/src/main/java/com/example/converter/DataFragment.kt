package com.example.converter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.converter.databinding.FragmentDataBinding

class DataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels()

    private lateinit var units: Map<String, Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        units = dataViewModel.units

        val spinnerItemLayoutId: Int = android.R.layout.simple_spinner_dropdown_item

        binding.unitCategorySpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            units.keys.toTypedArray()
        )
        binding.unitCategorySpinner.onItemSelectedListener = this

        binding.sourceUnitSpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            units["currency"]!!
        )
        binding.sourceUnitSpinner.onItemSelectedListener = this

        binding.destinationUnitSpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            units["currency"]!!
        )
        binding.destinationUnitSpinner.onItemSelectedListener = this

        dataViewModel.sourceValue.observe(viewLifecycleOwner) {
            binding.sourceValue.text = it.toString()
        }
        dataViewModel.destinationValue.observe(viewLifecycleOwner) {
            binding.destinationValue.text = it.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(
        parentView: AdapterView<*>?,
        selectedItemView: View?,
        position: Int,
        id: Long
    ) {
        if (parentView == null) { return }

        val item = parentView.getItemAtPosition(position)
        val spinnerItemLayoutId: Int = android.R.layout.simple_spinner_dropdown_item

        when (parentView.id) {
            R.id.source_unit_spinner -> {
                Log.d(
                    "DataFragment",
                    "source_unit_spinner: $item"
                )
            }
            R.id.destination_unit_spinner -> {
                Log.d(
                    "DataFragment",
                    "destination_unit_spinner: $item"
                )
            }
            R.id.unit_category_spinner -> {
                Log.d(
                    "DataFragment",
                    "unit_category_spinner: $item"
                )
                Log.d(
                    "DataFragment",
                    "item as String == " + item as String
                )
                dataViewModel.changeUnitsType(item as String)
                Log.d(
                    "DataFragment",
                    "dataViewModel.unitsType == " + dataViewModel.unitsType
                )
                binding.destinationUnitSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    spinnerItemLayoutId,
                    units[dataViewModel.unitsType]!!
                )
                binding.sourceUnitSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    spinnerItemLayoutId,
                    units[dataViewModel.unitsType]!!
                )
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}