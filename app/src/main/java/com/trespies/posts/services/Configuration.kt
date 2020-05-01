package com.trespies.posts.services

import android.app.Application
import com.trespies.posts.R

class Configuration constructor(private val application: Application) {

    val urlApiWS : String
        get() = application.getString(R.string.url_api_ws)

    val databaseName: String
        get() = "posts.db"
}