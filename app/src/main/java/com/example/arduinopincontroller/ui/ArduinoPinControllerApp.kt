package com.example.arduinopincontroller.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arduinopincontroller.ui.screens.PinsScreen
import com.example.arduinopincontroller.ui.screens.PinsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArduinoPinControllerApp() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val pinsViewModel: PinsViewModel = viewModel(factory = PinsViewModel.Factory)
        PinsScreen(
            pinsUiState = pinsViewModel.pinsUiState,
            retryAction = pinsViewModel::getPins,
            togglePinAction = pinsViewModel::togglePin,
        )
    }
}
