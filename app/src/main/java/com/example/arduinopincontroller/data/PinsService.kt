package com.example.arduinopincontroller.data

interface PinsService {
    suspend fun listPins(): List<Pin>
    suspend fun enablePin(pinName: String): List<Pin>
    suspend fun disablePin(pinName: String): List<Pin>
}