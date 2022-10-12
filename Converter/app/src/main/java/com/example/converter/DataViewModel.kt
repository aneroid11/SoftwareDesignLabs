package com.example.converter

import android.util.Log
import android.view.inputmethod.InputConnection
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class DataViewModel : ViewModel() {
    lateinit var sourceInputConnection: InputConnection

    private val _sourceValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>(
        BigDecimal("0")
    )
    private val _destinationValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>(
        BigDecimal("0")
    )

    private val _unitsCoefficients: Map<String, Map<String, Double>> = mapOf(
        "mass" to mapOf(
            "grams" to 1.0,
            "kilograms" to 1000.0,
            "tons" to 1000000.0
        ),
        "distance" to mapOf(
            "meters" to 1.0,
            "kilometers" to 1000.0,
            "centimeters" to 0.01
        ),
        "time" to mapOf(
            "years" to 1.0,
            "centuries" to 100.0,
            "millennium" to 1000.0
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
    val sourceUnits: String get() =
        _sourceUnits
    val destinationUnits: String get() =
        _destinationUnits

    val unitsType: String get() =
        _unitsType

    val sourceValue: LiveData<BigDecimal> get() =
        _sourceValue
    val destinationValue: LiveData<BigDecimal> get() =
        _destinationValue

    // maybe there is a better way to do this.
    fun setSourceValue(srcVal: BigDecimal) {
        Log.d("DataViewModel", "changing sourceValue to $srcVal")

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

        val convertCoef = coefSource / coefDest
        Log.d("DataViewModel", "convertCoef (double) == $convertCoef")

        var destValue: BigDecimal =
            _sourceValue.value!! * BigDecimal(convertCoef.toString())
            //BigDecimal(_sourceValueStr.value!!) * BigDecimal(convertCoef.toString())

        val zero: BigDecimal = BigDecimal.ZERO
        if (destValue.compareTo(zero) == 0) {
            destValue = zero
        } else {
            destValue = destValue.stripTrailingZeros()
        }

        _destinationValue.value = destValue

        Log.d(
            "DataViewModel",
            "updateDestinationValue() finished"
        )
    }
}