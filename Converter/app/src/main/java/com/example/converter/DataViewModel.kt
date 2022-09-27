package com.example.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class DataViewModel : ViewModel() {
    private val _sourceValueStr: MutableLiveData<String> = MutableLiveData<String>("0")
    private val _destinationValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>(
        BigDecimal("0.0")
    )

    private val _unitsCoefficients: Map<String, Map<String, BigDecimal>> = mapOf(
        "currency" to mapOf(
            "dollars (US)" to BigDecimal("1"),
            "euro" to BigDecimal("0.96"),
            "BYN" to BigDecimal("0.396")
        ),
        "mass" to mapOf(
            "kilograms" to BigDecimal("1"),
            "pounds" to BigDecimal("0.4535924"),
            "ounces" to BigDecimal("0.02834952")
        ),
        "distance" to mapOf(
            "meters" to BigDecimal("1"),
            "inches" to BigDecimal("0.0254"),
            "yards" to BigDecimal("0.9144")
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

    //val sourceValue: LiveData<Double> get() =
    //    _sourceValue
    val sourceValueStr: LiveData<String> get() =
        _sourceValueStr
    val destinationValue: LiveData<BigDecimal> get() =
        _destinationValue

    // maybe there is a better way to do this.
    fun setSourceValueStr(srcValStr: String) {
        _sourceValueStr.value = srcValStr

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
        //val temp = _sourceValueStr.value!!.toDouble() * coefSource
        val temp = BigDecimal(_sourceValueStr.value!!).times(coefSource)

        _destinationValue.value = temp.divide(coefDest)
    }
}