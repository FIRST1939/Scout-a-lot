package com.frcteam1939.scouting2026.android

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlin.text.toIntOrNull

class DataInputs {
    @Composable
    fun TextboxInput(
        label: String = "",
        singleLine: Boolean = false,
        forceNumberInput: Boolean = false,
        characterLimit: Int = 2500
    ) {
        var state by remember { mutableStateOf("") }
        TextField(
            value = state,
            onValueChange = { if (it.length <= characterLimit && (!forceNumberInput || it.toIntOrNull() != null) || it.isEmpty()) state = it },
            singleLine = singleLine,
            label = { Text(label) },
        )
    }

    @Composable
    fun RadioButton(

    ) {

    }

    @Composable
    fun Button(

    ) {

    }

    @Composable
    fun Slider(

    ) {

    }
}