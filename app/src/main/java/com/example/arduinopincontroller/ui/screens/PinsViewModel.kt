package com.example.arduinopincontroller.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.arduinopincontroller.data.Pin
import com.example.arduinopincontroller.data.PinsService
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.arduinopincontroller.ArduinoPinControllerApplication
import kotlinx.coroutines.launch

/**
 * UI state for the Home screen
 */
sealed interface PinsUiState {
    data class Success(val pins: List<Pin>) : PinsUiState
    data object Error : PinsUiState
    data object Loading : PinsUiState
}

class PinsViewModel(private val pinsService: PinsService) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var pinsUiState: PinsUiState by mutableStateOf(PinsUiState.Loading)
        private set

    init {
        getPins()
    }

    fun getPins() {
        doRequest { pinsService.listPins() }
    }

    fun togglePin(pinName: String, enabled: Boolean) {
        doRequest {
            if (enabled) {
                pinsService.enablePin(pinName)
            } else {
                pinsService.disablePin(pinName)
            }
        }
    }

    private fun doRequest(fetchAction: suspend () -> List<Pin>) {
        viewModelScope.launch {
            pinsUiState = PinsUiState.Loading
            pinsUiState = try {
                PinsUiState.Success(fetchAction())
            } catch (e: IOException) {
                Log.e("MYAPP", "exception", e);
                PinsUiState.Error
            } catch (e: HttpException) {
                Log.e("MYAPP", "exception", e);
                PinsUiState.Error
            }
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ArduinoPinControllerApplication)
                PinsViewModel(pinsService = application.container.pinsService)
            }
        }
    }
}