package com.example.arduinopincontroller.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arduinopincontroller.R
import com.example.arduinopincontroller.data.Pin
import com.example.arduinopincontroller.ui.theme.ArduinoPinControllerTheme

@Composable
fun PinsScreen(
    pinsUiState: PinsUiState,
    togglePinAction: (pinName: String, enabled: Boolean) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (pinsUiState) {
        is PinsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PinsUiState.Success -> PinsGridScreen(
            pinsUiState.pins,
            togglePinAction,
            contentPadding = contentPadding,
            modifier = modifier.fillMaxWidth()
        )

        is PinsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Loading...",
        modifier = modifier
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PinsGridScreen(
    pins: List<Pin>,
    togglePinAction: (pinName: String, enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding,
    ) {
        items(items = pins, key = { pin -> pin.pinName }) { pin ->
            PinCard(
                pin,
                togglePinAction,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun PinCard(
    pin: Pin,
    togglePinAction: (pinName: String, enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = pin.pinName)
            Switch(checked = pin.enabled, onCheckedChange = { togglePinAction(pin.pinName, it) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PinsGridScreenPreview() {
    ArduinoPinControllerTheme {
        val mockData = List(8) {
            Pin("D$it", it, false)
        }
        PinsGridScreen(mockData, { pinName, enabled ->
            // Do nothing, as this is just a preview
        })
    }
}