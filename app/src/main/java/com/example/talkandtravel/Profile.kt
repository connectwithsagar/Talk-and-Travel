package com.example.talkandtravel

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Profile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)

        val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)

        btnEditProfile.setOnClickListener {
            // Handle edit profile
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("ProfileData", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "Anuj Sir")
        val email = sharedPreferences.getString("email", "anuj123@gmail.com")
        val phone = sharedPreferences.getString("phone", "1234567891")

        tvName.text = "Name: $name"
        tvEmail.text = "Email: $email"
        tvPhone.text = "Phone: $phone"
    }
}
