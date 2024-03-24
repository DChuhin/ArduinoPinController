package com.example.arduinopincontroller.api

import com.example.arduinopincontroller.data.Pin
import com.example.arduinopincontroller.data.PinsService

class ApiPinsService(
    private val pinsApi: PinsApi
) : PinsService {
    override suspend fun listPins(): List<Pin> = pinsApi.getPins()
    override suspend fun enablePin(pinName: String): List<Pin> = pinsApi.enablePin(pinName)
    override suspend fun disablePin(pinName: String): List<Pin> = pinsApi.disablePin(pinName)
}