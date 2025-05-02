package com.example.talkandtravel.model

data class TrainBetweenStationsResponse(
    val status: Boolean,
    val message: String,
    val data: List<TrainInfo>
)

data class TrainInfo(
    val trainNo: String,
    val trainName: String,
    val fromStationCode: String,
    val toStationCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val travelTime: String,
    val runningDays: List<String>
)