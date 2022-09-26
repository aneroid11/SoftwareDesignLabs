package com.example.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _sourceValue: MutableLiveData<Double> = MutableLiveData<Double>()
    private val _destinationValue: MutableLiveData<Double> = MutableLiveData<Double>()

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

    private fun updateDestinationValue() {
        _destinationValue.value = _sourceValue.value?.times(34.31289)
    }
}