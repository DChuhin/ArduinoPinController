package com.example.arduinopincontroller

import android.app.Application
import com.example.arduinopincontroller.app.AppContainer
import com.example.arduinopincontroller.app.DefaultAppContainer

class ArduinoPinControllerApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}