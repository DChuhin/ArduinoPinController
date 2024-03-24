package com.example.arduinopincontroller.app

import com.example.arduinopincontroller.api.ApiPinsService
import com.example.arduinopincontroller.api.PinsApi
import com.example.arduinopincontroller.data.PinsService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

// DI container
interface AppContainer {
    val pinsService: PinsService
}

// DI container impl
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.100.46/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        // .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: PinsApi by lazy {
        retrofit.create(PinsApi::class.java)
    }

    /**
     * DI implementation for PinsService
     */
    override val pinsService: PinsService by lazy {
        ApiPinsService(retrofitService)
    }
}