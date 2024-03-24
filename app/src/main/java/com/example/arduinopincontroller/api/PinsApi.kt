package com.example.arduinopincontroller.api

import com.example.arduinopincontroller.data.Pin
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PinsApi {
    @GET("pins")
    suspend fun getPins(): List<Pin>
    @POST("pins/{pinName}/enable")
    suspend fun enablePin(@Path("pinName") pinName: String): List<Pin>
    @POST("pins/{pinName}/disable")
    suspend fun disablePin(@Path("pinName") pinName: String): List<Pin>
}