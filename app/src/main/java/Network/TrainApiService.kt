package com.example.talkandtravel.Network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import com.example.talkandtravel.model.SearchStationResponse
import com.example.talkandtravel.model.TrainBetweenStationsResponse
import com.example.talkandtravel.model.TrainScheduleResponse

interface  TrainApiService {

    // Search Station API with the new API key
    @GET("api/v1/searchStation")
    @Headers(
        "x-rapidapi-host: irctc1.p.rapidapi.com",
        "x-rapidapi-key: 99e25efdcbmsh5aeeeae953d885fp1e095ejsneb8b70ef2771"
    )
    suspend fun getStationDetails(
        @Query("query") query: String
    ): Response<SearchStationResponse>

    // Search Train API with the new API key
    @GET("api/v1/searchTrain")
    @Headers(
        "x-rapidapi-host: irctc1.p.rapidapi.com",
        "x-rapidapi-key: 99e25efdcbmsh5aeeeae953d885fp1e095ejsneb8b70ef2771"
    )
    suspend fun getTrainDetails(
        @Query("query") query: String
    ): Response<TrainScheduleResponse>

    // Train Between Stations API with the new API key
    @GET("api/v2/trainBetweenStations")
    @Headers(
        "x-rapidapi-host: irctc1.p.rapidapi.com",
        "x-rapidapi-key: 99e25efdcbmsh5aeeeae953d885fp1e095ejsneb8b70ef2771"
    )
    suspend fun getTrainsBetweenStations(
        @Query("fromStationCode") fromStationCode: String,
        @Query("toStationCode") toStationCode: String
    ): Response<TrainBetweenStationsResponse>

    // Train Schedule API (same as before) with the new API key
    @GET("api/v1/getTrainSchedule")
    @Headers(
        "x-rapidapi-host: irctc1.p.rapidapi.com",
        "x-rapidapi-key: 99e25efdcbmsh5aeeeae953d885fp1e095ejsneb8b70ef2771"
    )
    suspend fun getTrainSchedule(
        @Query("trainNo") trainNo: String
    ): Response<TrainScheduleResponse>
}
