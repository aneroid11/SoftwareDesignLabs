package com.example.converter

import android.util.Log
import android.view.inputmethod.InputConnection
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class DataViewModel : ViewModel() {
    lateinit var sourceInputConnection: InputConnection

    private val _sourceValueStr: MutableLiveData<String> = MutableLiveData<String>("")
    private val _destinationValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>(
        BigDecimal("0.0")
    )

    private val _unitsCoefficients: Map<String, Map<String, Double>> = mapOf(
        "mass" to mapOf(
            "kilograms" to 1.0,
            "tons" to 1000.0,
            "pounds" to 0.45
        ),
        "distance" to mapOf(
            "meters" to 1.0,
            "kilometers" to 1000.0,
            "inches" to 0.0254
        ),
        "time" to mapOf(
            "seconds" to 1.0,
            "minutes" to 60.0,
            "hours" to 3600.0
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
        /*val coefSource = _unitsCoefficients[_unitsType]!![_sourceUnits]!!
        val coefDest = _unitsCoefficients[_unitsType]!![_destinationUnits]!!

        val convertCoef = coefSource / coefDest
        Log.d("DataViewModel", "convertCoef (double) == $convertCoef")

        var destValue: BigDecimal =
            BigDecimal(_sourceValueStr.value!!) * BigDecimal(convertCoef.toString())

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
        )*/
    }
}