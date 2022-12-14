package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val toast: Toast = Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        )
        toast.show()
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

        //showToast("switched units and values!")
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
            private var react: Boolean = true
            private lateinit var prevText: String
            private var prevCursorPos: Int = 0

            // return the resulting string and the number of deleted zeros
            private fun removeLeadingZeros(s: String): Pair<String, Int> {
                var result: String = s
                var index: Int = 0

                while (
                    result != "0" &&
                    !result.startsWith("0.") &&
                    result.startsWith("0")
                ) {
                    result = result.removePrefix("0")
                    index++
                }

                return Pair(result, index)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
                if (!react) { return }

                Log.d("TextWatcher", "source value before: $s")
                prevText = s.toString()
                prevCursorPos = start
            }

            override fun afterTextChanged(s: Editable) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (!react) { return }

                Log.d("TextWatcher", "source value changed to: $s")

                val sStr: String = s.toString()
                val cursorPos: Int = binding.sourceValue.selectionStart

                if (sStr.startsWith("0")) {
                    if (sStr != "0" && !sStr.startsWith("0.")) {
                        react = false

                        val (strWithoutZeros, numZeros) = removeLeadingZeros(sStr)
                        binding.sourceValue.setText(strWithoutZeros)
                        //binding.sourceValue.setSelection(prevCursorPos)
                        var newCursorPos = cursorPos - numZeros
                        newCursorPos = if (newCursorPos < 0) 0 else newCursorPos

                        binding.sourceValue.setSelection(newCursorPos)

                        react = true

                        showToast("cannot have extra leading zeros!")
                    }
                }
                if (sStr.startsWith(".") && cursorPos != 0) {
                    Log.d("TextWatcher", "starts with . and start != 0")

                    react = false
                    binding.sourceValue.setText("0" + sStr)
                    binding.sourceValue.setSelection(cursorPos + 1)
                    react = true
                }

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
