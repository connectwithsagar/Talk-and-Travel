package com.example.talkandtravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.nl.translate.*

import java.util.Locale

class MainPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mainpage)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val googleLogin = findViewById<Button>(R.id.googleLogin)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val signupText = findViewById<TextView>(R.id.signupText)

        // Load saved language and apply translations
        val langCode = getLanguageFromPreferences()
        translateUI(langCode, emailInput, passwordInput, loginButton, googleLogin, forgotPassword, signupText)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Enter a valid email"
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                passwordInput.error = "Password must be at least 6 characters"
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        googleLogin.setOnClickListener {
            Toast.makeText(this, "Google Login Clicked", Toast.LENGTH_SHORT).show()
        }

        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

        signupText.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val langCode = prefs.getString("language_code", "en") ?: "en"
        super.attachBaseContext(setLocale(newBase, langCode))
    }

    private fun setLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    private fun getLanguageFromPreferences(): String {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("language_code", "en") ?: "en"
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePage::class.java) // Replace with your next screen
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun translateUI(
        langCode: String,
        emailInput: EditText,
        passwordInput: EditText,
        loginButton: Button,
        googleLogin: Button,
        forgotPassword: TextView,
        signupText: TextView
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(langCode)
            .build()

        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translateHint(translator, "Enter your email", emailInput)
                translateHint(translator, "Enter your password", passwordInput)
                translateText(translator, "Login", loginButton)
                translateText(translator, "Login with Google", googleLogin)
                translateText(translator, "Forgot Password?", forgotPassword)
                translateText(translator, "Don't have an account? Sign up", signupText)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Translation model download failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun translateText(translator: Translator, text: String, textView: TextView) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                textView.text = translatedText
            }
    }

    private fun translateText(translator: Translator, text: String, button: Button) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                button.text = translatedText
            }
    }

    private fun translateHint(translator: Translator, hintText: String, editText: EditText) {
        translator.translate(hintText)
            .addOnSuccessListener { translatedHint ->
                editText.hint = translatedHint
            }
    }
}
