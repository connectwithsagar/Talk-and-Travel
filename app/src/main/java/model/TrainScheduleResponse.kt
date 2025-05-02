package com.example.talkandtravel.model


data class TrainScheduleResponse(
    val status: Boolean,
    val message: String,
    val data: ScheduleData
)

data class ScheduleData(
    val trainNo: String,
    val trainName: String,
    val runDays: List<String>,
    val route: List<Route>
)

data class Route(
    val stationCode: String,
    val stationName: String,
    val arrivalTime: String,
    val departureTime: String,
    val day: Int,
    val distance: String
)
