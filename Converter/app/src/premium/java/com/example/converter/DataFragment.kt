package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.converter.databinding.FragmentDataBinding
import java.math.BigDecimal

class DataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels()

    private lateinit var units: Map<String, Array<String>>

    private var sourceUnitsIdInSpinner: Int = 0
    private var destUnitsIdInSpinner: Int = 0

    private fun showToast(msg: String) {
        val switchToast: Toast = Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        )
        switchToast.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )*/

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
        showToast("copied original value!")
    }

    private fun copyDestValueToClipboard() {
        copyStrToClipboard(binding.destinationValue.text.toString())
        showToast("copied converted value!")
    }

    private fun switchSourceDest() {
        val oldDestUnitsId = destUnitsIdInSpinner
        val oldSourceUnitsId = sourceUnitsIdInSpinner

        binding.sourceUnitSpinner.setSelection(oldDestUnitsId)
        binding.destinationUnitSpinner.setSelection(oldSourceUnitsId)

        dataViewModel.sourceInputConnection.setSelection(
            0, binding.sourceValue.text!!.length
        )
        dataViewModel.sourceInputConnection.commitText(
            dataViewModel.destinationValue.value!!.toPlainString(),
            1
        )

        showToast("switched units and values!")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataViewModel.sourceInputConnection = binding.sourceValue.onCreateInputConnection(EditorInfo())!!

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

        binding.sourceValue.showSoftInputOnFocus = false
        binding.sourceValue.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                Log.d("TextWatcher", "source value changed to: $s")

                val sStr = s.toString()

                val convertedNumber =
                    if (sStr.isEmpty() || sStr == ".")
                        BigDecimal("0")
                    else
                        BigDecimal(sStr)
                dataViewModel.setSourceValue(convertedNumber)
            }
        })

        binding.destinationValue.movementMethod = ScrollingMovementMethod()

        /*dataViewModel.sourceValueStr.observe(viewLifecycleOwner) {
            binding.sourceValue.setText(it)
        }*/
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
