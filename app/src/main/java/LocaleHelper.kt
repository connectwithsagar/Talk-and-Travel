package com.example.talkandtravel

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*;
object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config) // For Android N and above
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics) // For older versions
            context
        }
    }

    fun persistLanguage(context: Context, lang: String) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putString("language_code", lang)
            .apply()
    }

    fun getPersistedLanguage(context: Context): String {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language_code", "en") ?: "en"
    }
}

