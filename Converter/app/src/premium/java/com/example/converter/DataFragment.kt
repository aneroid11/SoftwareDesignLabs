package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.converter.databinding.FragmentDataBinding

class DataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels()

    private lateinit var units: Map<String, Array<String>>

    private var sourceUnitsIdInSpinner: Int = 0
    private var destUnitsIdInSpinner: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun copyStrToClipboard(str: String) {
        val clipboardManager: ClipboardManager =
            activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        val clipData: ClipData = ClipData.newPlainText(
            "text",
            str
        )
        clipboardManager.setPrimaryClip(clipData)
    }

    private fun copySourceValueToClipboard() {
        copyStrToClipboard(binding.sourceValue.text.toString())
    }

    private fun copyDestValueToClipboard() {
        copyStrToClipboard(binding.destinationValue.text.toString())
    }

    private fun switchSourceDest() {
        val oldDestUnitsId = destUnitsIdInSpinner
        val oldSourceUnitsId = sourceUnitsIdInSpinner

        binding.sourceUnitSpinner.setSelection(oldDestUnitsId)
        binding.destinationUnitSpinner.setSelection(oldSourceUnitsId)

        dataViewModel.setSourceValueStr(
            dataViewModel.destinationValue.value!!.toPlainString()
        )

        Log.d(
            "DataFragment",
            "switchSourceDest() finished"
        )
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
            units["mass"]!!
        )
        binding.sourceUnitSpinner.onItemSelectedListener = this

        binding.destinationUnitSpinner.adapter = ArrayAdapter(
            requireContext(),
            spinnerItemLayoutId,
            units["mass"]!!
        )
        binding.destinationUnitSpinner.onItemSelectedListener = this

        binding.sourceValue.movementMethod = ScrollingMovementMethod()

        dataViewModel.sourceValueStr.observe(viewLifecycleOwner) {
            binding.sourceValue.text = it
        }
        dataViewModel.destinationValue.observe(viewLifecycleOwner) {
            binding.destinationValue.text = it.toPlainString()
        }

        binding.copySourceValueButton.setOnClickListener { copySourceValueToClipboard() }
        binding.copyDestValueButton.setOnClickListener { copyDestValueToClipboard() }
        binding.switchButton.setOnClickListener { switchSourceDest() }
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
                dataViewModel.changeSourceUnits(item as String)
                sourceUnitsIdInSpinner = position
            }
            R.id.destination_unit_spinner -> {
                dataViewModel.changeDestinationUnits(item as String)
                destUnitsIdInSpinner = position
            }
            R.id.unit_category_spinner -> {
                dataViewModel.changeUnitsType(item as String)

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