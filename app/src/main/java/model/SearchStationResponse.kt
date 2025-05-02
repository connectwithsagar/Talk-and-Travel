package com.example.talkandtravel.model

data class SearchStationResponse(
    val status: Boolean,
    val message: String,
    val data: List<Station>
)

data class Station(
    val stationCode: String,
    val stationName: String,
    val city: String,
    val state: String
)
