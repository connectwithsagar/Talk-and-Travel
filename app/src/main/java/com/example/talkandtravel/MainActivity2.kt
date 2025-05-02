package com.example.talkandtravel


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.talkandtravel.Network.RetrofitClient
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    private lateinit var etFromStation: EditText
    private lateinit var etToStation: EditText
    private lateinit var etTrainNumber: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnStationDetails: Button
    private lateinit var btnSearch: Button
    private lateinit var btnCurrentStatus: Button
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link XML views to variables
        etFromStation = findViewById(R.id.etFromStation)
        etToStation = findViewById(R.id.etToStation)
        etTrainNumber = findViewById(R.id.etTrainNumber)
        tvResult = findViewById(R.id.tvResult)
        progressBar = findViewById(R.id.progressBar) // Make sure to add a ProgressBar in your XML

        btnStationDetails = findViewById(R.id.btnStationDetails)
        btnSearch = findViewById(R.id.btnSearch)
        btnCurrentStatus = findViewById(R.id.btnCurrentStatus)

        // Station details (based on fromStation)
        btnStationDetails.setOnClickListener {
            val stationCode = etFromStation.text.toString().trim()
            if (stationCode.isNotEmpty()) {
                fetchStationDetails(stationCode)
            } else {
                tvResult.text = "Enter a valid station code"
            }
        }

        // Train between stations
        btnSearch.setOnClickListener {
            val from = etFromStation.text.toString().trim()
            val to = etToStation.text.toString().trim()
            if (from.isNotEmpty() && to.isNotEmpty()) {
                fetchTrainsBetweenStations(from, to)
            } else {
                tvResult.text = "Please enter both 'From' and 'To' station codes"
            }
        }

        // Train schedule by number
        btnCurrentStatus.setOnClickListener {
            val trainNumber = etTrainNumber.text.toString().trim()
            if (trainNumber.isNotEmpty()) {
                fetchTrainSchedule(trainNumber)
            } else {
                tvResult.text = "Enter a valid train number"
            }
        }
    }

    private fun fetchStationDetails(query: String) {
        lifecycleScope.launch {
            // Show the ProgressBar
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            try {
                val response = RetrofitClient.apiService.getStationDetails(query)
                if (response.isSuccessful) {
                    val stations = response.body()?.data ?: emptyList()
                    val resultText = if (stations.isNotEmpty()) {
                        stations.joinToString("\n\n") { station ->
                            "Station: ${station.stationName}\n" +
                                    "Code: ${station.stationCode}\n" +
                                    "City: ${station.city}, ${station.state}"
                        }
                    } else {
                        "No matching stations found"
                    }
                    tvResult.text = resultText
                } else {
                    tvResult.text = "Failed: ${response.code()}"
                }
            } catch (e: Exception) {
                tvResult.text = "Error: ${e.message}"
            } finally {
                // Hide the ProgressBar after the operation completes
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
    }

    private fun fetchTrainsBetweenStations(from: String, to: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getTrainsBetweenStations(from, to)
                if (response.isSuccessful) {
                    val trainsData = response.body()
                    val trainList = trainsData?.data ?: emptyList()

                    val resultText = if (trainList.isNotEmpty()) {
                        trainList.joinToString("\n\n") { train ->
                            "Train No: ${train.trainNo}\n" +
                                    "Name: ${train.trainName}\n" +
                                    "From: ${train.fromStationCode} at ${train.departureTime}\n" +
                                    "To: ${train.toStationCode} at ${train.arrivalTime}\n" +
                                    "Travel Time: ${train.travelTime}"
                        }
                    } else {
                        "No trains found"
                    }
                    tvResult.text = resultText
                } else {
                    tvResult.text = "Failed: ${response.code()}"
                }
            } catch (e: Exception) {
                tvResult.text = "Error: ${e.message}"
            }
        }
    }


    private fun fetchTrainSchedule(trainNo: String) {
        lifecycleScope.launch {
            // Show the ProgressBar
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            try {
                val response = RetrofitClient.apiService.getTrainSchedule(trainNo)
                if (response.isSuccessful) {
                    val schedule = response.body()?.data
                    val routeDetails = schedule?.route?.joinToString("\n\n") { stop ->
                        "Station: ${stop.stationName} (${stop.stationCode})\n" +
                                "Arrival: ${stop.arrivalTime}, Departure: ${stop.departureTime}\n" +
                                "Day: ${stop.day}, Distance: ${stop.distance} km"
                    } ?: "No schedule data"

                    val resultText = "Train: ${schedule?.trainName} (${schedule?.trainNo})\n\nRoute:\n$routeDetails"
                    tvResult.text = resultText
                } else {
                    tvResult.text = "Failed: ${response.code()}"
                }
            } catch (e: Exception) {
                tvResult.text = "Error: ${e.message}"
            } finally {
                // Hide the ProgressBar after the operation completes
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
    }


    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
