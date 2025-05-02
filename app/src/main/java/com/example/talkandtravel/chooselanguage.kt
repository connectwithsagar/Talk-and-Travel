package com.example.talkandtravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class chooselanguage : AppCompatActivity() {

    private val languages = listOf(
        "Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali",
        "Catalan", "Czech", "Welsh", "Danish", "German",
        "Greek", "English", "Esperanto", "Spanish", "Estonian",
        "Persian", "Finnish", "French", "Irish", "Galician",
        "Gujarati", "Hebrew", "Hindi", "Croatian", "Haitian",
        "Hungarian", "Indonesian", "Icelandic", "Italian", "Japanese",
        "Georgian", "Kannada", "Korean", "Lithuanian", "Latvian",
        "Macedonian", "Marathi", "Malay", "Maltese", "Dutch",
        "Norwegian", "Polish", "Portuguese", "Romanian", "Russian",
        "Slovak", "Slovenian", "Albanian", "Swedish", "Swahili",
        "Tamil", "Telugu", "Thai", "Tagalog", "Turkish",
        "Ukrainian", "Urdu", "Vietnamese", "Chinese"
    )

    private val languageMap = mapOf(
        "Afrikaans" to "af", "Arabic" to "ar", "Belarusian" to "be", "Bulgarian" to "bg", "Bengali" to "bn",
        "Catalan" to "ca", "Czech" to "cs", "Welsh" to "cy", "Danish" to "da", "German" to "de",
        "Greek" to "el", "English" to "en", "Esperanto" to "eo", "Spanish" to "es", "Estonian" to "et",
        "Persian" to "fa", "Finnish" to "fi", "French" to "fr", "Irish" to "ga", "Galician" to "gl",
        "Gujarati" to "gu", "Hebrew" to "he", "Hindi" to "hi", "Croatian" to "hr", "Haitian" to "ht",
        "Hungarian" to "hu", "Indonesian" to "id", "Icelandic" to "is", "Italian" to "it", "Japanese" to "ja",
        "Georgian" to "ka", "Kannada" to "kn", "Korean" to "ko", "Lithuanian" to "lt", "Latvian" to "lv",
        "Macedonian" to "mk", "Marathi" to "mr", "Malay" to "ms", "Maltese" to "mt", "Dutch" to "nl",
        "Norwegian" to "no", "Polish" to "pl", "Portuguese" to "pt", "Romanian" to "ro", "Russian" to "ru",
        "Slovak" to "sk", "Slovenian" to "sl", "Albanian" to "sq", "Swedish" to "sv", "Swahili" to "sw",
        "Tamil" to "ta", "Telugu" to "te", "Thai" to "th", "Tagalog" to "tl", "Turkish" to "tr",
        "Ukrainian" to "uk", "Urdu" to "ur", "Vietnamese" to "vi", "Chinese" to "zh"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooselanguage)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.language_search)
        val letsgo = findViewById<Button>(R.id.letsgo)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        autoCompleteTextView.setAdapter(adapter)

        letsgo.setOnClickListener {
            val selectedLanguage = autoCompleteTextView.text.toString()
            val langCode = languageMap[selectedLanguage]

            if (langCode != null) {
                saveLanguage(this, langCode)

                val intent = Intent(this, MainPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun saveLanguage(context: Context, langCode: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language_code", langCode).apply()
    }
}
