package com.example.arduinopincontroller.data

import kotlinx.serialization.Serializable

@Serializable
data class Pin(
    val pinName: String,
    val gpio: Int,
    var enabled: Boolean
)