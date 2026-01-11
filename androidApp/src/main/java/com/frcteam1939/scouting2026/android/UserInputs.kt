package com.frcteam1939.scouting2026.android

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlin.text.toIntOrNull

object UserInputs {
    @Composable
    fun TextboxInput(
        value: String,
        onValueChange: (String) -> Unit,
        label: String = "",
        singleLine: Boolean = false,
        forceNumberInput: Boolean = false,
        characterLimit: Int = 2500
    ) {
        TextField(
            value = value,
            onValueChange = { input ->
                //state can't be negative if forceNumberInput is true
                var validUpdate = true
                if (input.length > characterLimit) validUpdate = false
                if (forceNumberInput && input.toIntOrNull() == null) validUpdate = false
                if (input.isEmpty()) validUpdate = true
                if (validUpdate) onValueChange(input) },
            singleLine = singleLine,
            label = { Text(label) },
        )
    }

    @Composable
    fun RadioButtonInput(

    ) {

    }

    @Composable
    fun ButtonInput(

    ) {

    }

    @Composable
    fun SliderInput(

    ) {

    }
    @Composable
    fun CounterInput() {
    }

    @Composable
    fun SwitchInput() {
    }

}