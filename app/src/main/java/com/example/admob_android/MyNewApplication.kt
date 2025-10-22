package com.example.admob_android

import android.app.Application
import com.example.admob_android.service.AppOpenAdManager
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNewApplication: Application() {

    companion object{
        const val APP_ID = "ca-app-pub-3940256099942544~3347511713"
    }

    private lateinit var appOpenAdManager: AppOpenAdManager

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MyNewApplication)
        }

        appOpenAdManager = AppOpenAdManager()
    }
}