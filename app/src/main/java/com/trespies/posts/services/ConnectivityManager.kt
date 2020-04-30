package com.trespies.posts.services

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Connectivity @Inject
constructor(private val app: Application) {

    val connectivityManager : ConnectivityManager
        get() = app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork: NetworkInfo?
        get() = connectivityManager.activeNetworkInfo

    public fun hasInternetConnection(): Boolean {
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

    public fun hasWifiConnection() : Boolean {
        if (!hasInternetConnection()) { return false }
        val isWiFi: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_WIFI
        return isWiFi
    }

}