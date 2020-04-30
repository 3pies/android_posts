package com.trespies.posts.api.clients

import com.google.gson.GsonBuilder
import com.trespies.posts.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


abstract class AbstractRestClient<IService> {

    protected abstract val urlBase: String

    protected abstract val iClassService: Class<IService>

    val client: IService
        get() {

            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setLenient()
                    .create()

            val builder = Retrofit.Builder()
                    .baseUrl(urlBase)
                    .addConverterFactory(
                            GsonConverterFactory.create(gson)
                    )
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())

            val retrofit = builder
                    .client(
                            httpClient
                    )
                    .build()

            var iClient: IService = retrofit.create(iClassService)

            return iClient
        }

    protected val httpClient: OkHttpClient
        get() {
            val httpClient = OkHttpClient.Builder()
            addInterceptors(httpClient)
            addLoggingInterceptor(httpClient)
            return httpClient.build()
        }

    protected val loggingLevel: HttpLoggingInterceptor.Level
        get() = HttpLoggingInterceptor.Level.BODY


    protected open fun addInterceptors(httpClient: OkHttpClient.Builder) {
        //override with custom interceptors
    }

    protected fun addLoggingInterceptor(httpClient: OkHttpClient.Builder) {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.setLevel(loggingLevel)
        httpClient.addInterceptor(logging)
    }


}
