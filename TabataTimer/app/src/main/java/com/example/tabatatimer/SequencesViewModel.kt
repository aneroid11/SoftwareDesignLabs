package com.example.tabatatimer

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SequencesViewModel : ViewModel() {
    private val _sequencesList: MutableLiveData<MutableList<Sequence>> =
        MutableLiveData<MutableList<Sequence>>(mutableListOf())

    val sequencesList: LiveData<MutableList<Sequence>> get() =
        _sequencesList

    private fun getRandomColor(): Int {
        val red = Random.nextInt() % 50 + 130
        val green = Random.nextInt() % 50 + 130
        val blue = Random.nextInt() % 50 + 130
        return Color.rgb(red, green, blue)
    }

    init {
        // load the sequences list from .json file
        for (i in 1..10) {
            _sequencesList.value!!.add(
                Sequence(
                    getRandomColor(),
                    "hard training",
                    2,
                    listOf(
                        Phase("warmup","", 20),
                        Phase("work", "", 10),
                        Phase("rest", "", 10),
                        Phase("work", "", 10),
                        Phase("rest", "", 10),
                        Phase("work", "", 10),
                        Phase("rest", "", 10),
                        Phase("cooldown", "", 20)
                    )
                )
            )
        }
    }
}