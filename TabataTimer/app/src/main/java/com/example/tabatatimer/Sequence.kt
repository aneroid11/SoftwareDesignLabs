package com.example.tabatatimer

data class Phase(
    var type: String,
    var durationSec: Int
)

data class Sequence(
    var color: Int,
    var title: String,
    var numRepetitions: Int,
    var phasesList: MutableList<Phase>
)
