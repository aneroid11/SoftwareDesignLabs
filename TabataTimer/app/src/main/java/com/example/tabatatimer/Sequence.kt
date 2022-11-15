package com.example.tabatatimer

data class Phase(
    var type: String,
    var description: String,
    var durationSec: Int
)

data class Sequence(
    var color: Int,
    var title: String,
    var numRepetitions: Int,
    var phasesList: List<Phase>
)
