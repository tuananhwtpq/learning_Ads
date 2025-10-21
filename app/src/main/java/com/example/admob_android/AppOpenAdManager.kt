package com.example.admob_android

import com.google.android.gms.ads.appopen.AppOpenAd

class AppOpenAdManager {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd: Boolean = false
    private var isShowingAd: Boolean = false

    private var loadTime: Long = 0
}