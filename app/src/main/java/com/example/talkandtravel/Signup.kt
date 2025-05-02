package com.example.talkandtravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.nl.translate.*

class Signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val phoneInput = findViewById<EditText>(R.id.phninput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginRedirect = findViewById<TextView>(R.id.loginRedirect)

        val langCode = getLanguageFromPreferences()
        if (langCode != null) {
            translateUI(langCode, nameInput, phoneInput, emailInput, passwordInput, signupButton, loginRedirect)
        }

        signupButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(name, phone, email, password)) {
                registerUser(name, phone, email, password)
            }

                Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, HomePage::class.java))



        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getLanguageFromPreferences(): String? {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("language_code", "en")
    }

    private fun validateInputs(name: String, phone: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty() || phone.length != 10) {
            Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerUser(name: String, phone: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = hashMapOf(
                            "name" to name,
                            "phone" to phone,
                            "email" to email,
                            "password" to password
                        )

                        db.collection("users").document(userId).set(user)
                            .addOnSuccessListener {
                                saveUserProfileLocally(name, email, phone)
                                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT)
                                        .show()
                                    startActivity(Intent(this, HomePage::class.java))
                                    finish()

                            }

                    } else {
                        Toast.makeText(this, "Signup failed: user ID is null", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("SignupError", "Signup failed", task.exception)
                    Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserProfileLocally(name: String, email: String, phone: String) {
        val sharedPreferences = getSharedPreferences("ProfileData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.apply()
        Log.d("SignupDebug", "Saved: $name, $email, $phone")
    }

    private fun translateUI(
        langCode: String,
        nameInput: EditText,
        phoneInput: EditText,
        emailInput: EditText,
        passwordInput: EditText,
        signupButton: Button,
        loginRedirect: TextView
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(langCode)
            .build()

        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translateText(translator, "Sign Up", signupButton)
                translateText(translator, "Already have an account? Login", loginRedirect)
                translateHint(translator, "Enter your name", nameInput)
                translateHint(translator, "Enter your phone number", phoneInput)
                translateHint(translator, "Enter your email", emailInput)
                translateHint(translator, "Enter your password", passwordInput)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load translation model", Toast.LENGTH_SHORT).show()
            }
    }

    private fun translateText(translator: Translator, text: String, textView: TextView) {
        translator.translate(text)
            .addOnSuccessListener { translatedText -> textView.text = translatedText }
            .addOnFailureListener { }
    }

    private fun translateText(translator: Translator, text: String, button: Button) {
        translator.translate(text)
            .addOnSuccessListener { translatedText -> button.text = translatedText }
            .addOnFailureListener { }
    }

    private fun translateHint(translator: Translator, hintText: String, editText: EditText) {
        translator.translate(hintText)
            .addOnSuccessListener { translatedHint -> editText.hint = translatedHint }
            .addOnFailureListener { }
    }
}
