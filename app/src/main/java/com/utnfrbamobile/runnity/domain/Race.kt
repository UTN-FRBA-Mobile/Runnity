package com.utnfrbamobile.runnity.domain

data class Race(
    var id: String = "",
    var category: Long = 0L,
    var user1: RaceParticipant,
    var user2: RaceParticipant
)