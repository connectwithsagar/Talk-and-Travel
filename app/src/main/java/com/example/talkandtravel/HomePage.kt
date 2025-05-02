package com.example.talkandtravel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

import android.widget.Toast
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import java.net.URL

class HomePage : AppCompatActivity() {

    private lateinit var mapsFragment: MapsFragment
    private lateinit var autoCompleteSearch: AutoCompleteTextView

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var destinationLatLng: LatLng? = null
    private var destinationName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        autoCompleteSearch = findViewById(R.id.autoCompleteSearch)
        mapsFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapsFragment

        val trainOption = findViewById<LinearLayout>(R.id.trainOption)
        val busOption = findViewById<LinearLayout>(R.id.busOption)
        val profileOption = findViewById<LinearLayout>(R.id.profileOption)

        trainOption.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        busOption.setOnClickListener {
            val intent = Intent(this, BusActivity::class.java)
            startActivity(intent)
        }

        profileOption.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


        requestLocationPermission()
        setupAutoCompleteSearch()

    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            recreate()
        } else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAutoCompleteSearch() {
        autoCompleteSearch.threshold = 2

        autoCompleteSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.length >= 2) {
                    fetchNominatimSuggestions(query)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchNominatimSuggestions(query: String) {
        val preferredLanguage = LocaleHelper.getPersistedLanguage(this)
        val url = "https://nominatim.openstreetmap.org/search?format=json&q=$query&addressdetails=1&limit=5&accept-language=$preferredLanguage"

        Thread {
            try {
                val connection = URL(url).openConnection()
                connection.setRequestProperty("User-Agent", "TalkAndTravelApp/1.0 (your@email.com)")
                val result = connection.getInputStream().bufferedReader().readText()
                val jsonArray = JSONArray(result)
                val suggestions = mutableListOf<String>()
                val places = mutableListOf<NominatimPlace>()

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val place = NominatimPlace(
                        item.getString("display_name"),
                        item.getString("lat"),
                        item.getString("lon")
                    )
                    suggestions.add(place.display_name)
                    places.add(place)
                }

                runOnUiThread {
                    val adapter = ArrayAdapter(
                        this@HomePage,
                        android.R.layout.simple_dropdown_item_1line,
                        suggestions
                    )
                    autoCompleteSearch.setAdapter(adapter)

                    autoCompleteSearch.setOnItemClickListener { _, _, position, _ ->
                        val selectedPlace = places[position]
                        val latLng = LatLng(selectedPlace.lat.toDouble(), selectedPlace.lon.toDouble())
                        destinationLatLng = latLng
                        destinationName = selectedPlace.display_name

                        mapsFragment.showDestinationOnMap(latLng, selectedPlace.display_name)
                        Toast.makeText(this, "Destination selected", Toast.LENGTH_SHORT).show()

                        Handler().postDelayed({
                            launchGoogleMapsNavigation()
                        }, 2000)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error fetching suggestions: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun launchGoogleMapsNavigation() {
        if (destinationLatLng != null) {
            val uri = Uri.parse("google.navigation:q=${destinationLatLng!!.latitude},${destinationLatLng!!.longitude}&mode=d")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Destination not selected", Toast.LENGTH_LONG).show()
        }
    }

    data class NominatimPlace(
        val display_name: String,
        val lat: String,
        val lon: String
    )
}