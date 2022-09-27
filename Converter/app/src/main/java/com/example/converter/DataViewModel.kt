package com.example.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _sourceValue: MutableLiveData<Double> = MutableLiveData<Double>(0.0)
    private val _destinationValue: MutableLiveData<Double> = MutableLiveData<Double>(0.0)

    private val _unitsCoefficients: Map<String, Map<String, Double>> = mapOf(
        "currency" to mapOf(
            "dollars (US)" to 1.0,
            "euro" to 0.96,
            "BYN" to 0.396
        ),
        "mass" to mapOf(
            "kilograms" to 1.0,
            "pounds" to 0.4535924,
            "ounces" to 0.02834952
        ),
        "distance" to mapOf(
            "meters" to 1.0,
            "inches" to 0.0254,
            "yards" to 0.9144
        )
    )

    private fun unitsCoefficientsToUnits(): Map<String, Array<String>> {
        val unitsMap = mutableMapOf<String, Array<String>>()

        for (unitsType in _unitsCoefficients.keys) {
            val unitsArray = _unitsCoefficients[unitsType]!!.keys.toTypedArray()
            unitsMap[unitsType] = unitsArray
        }

        return unitsMap
    }

    private var _units: Map<String, Array<String>> = unitsCoefficientsToUnits()

    private var _unitsType: String = _units.keys.toTypedArray()[0]
    private var _sourceUnits: String = _units[_unitsType]!![0]
    private var _destinationUnits: String = _units[_unitsType]!![0]

    val units: Map<String, Array<String>> get() =
        _units

    val unitsType: String get() =
        _unitsType

    val sourceValue: LiveData<Double> get() =
        _sourceValue
    val destinationValue: LiveData<Double> get() =
        _destinationValue

    // maybe there is a better way to do this.
    fun setSourceValue(srcVal: Double) {
        _sourceValue.value = srcVal

        updateDestinationValue()
    }

    fun changeUnitsType(unitsType: String) {
        _unitsType = unitsType
        val currentUnits: Array<String> = _units[_unitsType]!!

        _sourceUnits = currentUnits[0]
        _destinationUnits = currentUnits[0]
    }

    fun changeSourceUnits(sourceUnits: String) {
        _sourceUnits = sourceUnits
        updateDestinationValue()
    }

    fun changeDestinationUnits(destinationUnits: String) {
        _destinationUnits = destinationUnits
        updateDestinationValue()
    }

    private fun updateDestinationValue() {
        // Updates the destination value using source units and destination units.
        val coefSource = _unitsCoefficients[_unitsType]!![_sourceUnits]!!
        val coefDest = _unitsCoefficients[_unitsType]!![_destinationUnits]!!
        val temp = _sourceValue.value!!.times(coefSource)

        _destinationValue.value = temp / coefDest
    }
}