package com.example.talkandtravel


import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base, LocaleHelper.getPersistedLanguage(base)))
    }
}
