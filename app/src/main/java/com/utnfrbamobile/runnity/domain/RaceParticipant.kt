package com.utnfrbamobile.runnity.domain

data class RaceParticipant (
    var email: String = "",
    var name: String = "",
    var duration: Long = 0,
    var distance: Float = 0F
)