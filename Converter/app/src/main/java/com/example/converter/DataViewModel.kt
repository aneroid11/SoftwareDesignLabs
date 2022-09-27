package com.example.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _sourceValue: MutableLiveData<Double> = MutableLiveData<Double>()
    private val _destinationValue: MutableLiveData<Double> = MutableLiveData<Double>()

    private var _units: Map<String, Array<String>> = mapOf(
        "currency" to arrayOf("dollars (US)", "euro", "BYN"),
        "mass" to arrayOf("pounds", "kilograms", "carats"),
        "distance" to arrayOf("meters", "inches", "feet")
    )

    private var _unitsType: String = _units.keys.toTypedArray()[0]
    private var _sourceUnits: String = _units[_unitsType]!![0]
    private var _destinationUnits: String = _units[_unitsType]!![0]

    val units: Map<String, Array<String>> get() =
        _units

    val unitsType: String get() =
        _unitsType
    val sourceUnits: String get() =
        _sourceUnits
    val destinationUnits: String get() =
        _destinationUnits

    val sourceValue: LiveData<Double> get() =
        _sourceValue
    val destinationValue: LiveData<Double> get() =
        _destinationValue

    // maybe there is a better way to do this.
    fun setSourceValue(srcVal: Double) {
        Log.d("DataViewModel", "setSourceValue($srcVal) called")
        _sourceValue.value = srcVal

        updateDestinationValue()
    }

    fun changeUnitsType(unitsType: String) {
        _unitsType = unitsType
        val currentUnits: Array<String> = _units[_unitsType]!!

        _sourceUnits = currentUnits[0]
        _destinationUnits = currentUnits[0]
    }

    private fun updateDestinationValue() {
        _destinationValue.value = _sourceValue.value?.times(34.31289)
    }
}