package com.frcteam1939.scouting2026.android

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlin.text.toIntOrNull

class Inputs {

    inner class Textbox(
        val form: Boolean = false,
        val label: String = "",
        val singleLine: Boolean = false,
        val forceNumberInput: Boolean = false,
        val characterLimit: Int = 2500
    ) {
        @Composable
        fun create() {
            var state by remember { mutableStateOf("") }
            TextField(
                value = state,
                onValueChange = {
                    if (it.length <= characterLimit && it.toIntOrNull() != null || it.isEmpty()) state = it },
                singleLine = singleLine,
                label = { Text(label) },
            )
        }
    }

    inner class RadioButton(val form: Boolean = false) {}

    inner class Button(val form: Boolean = true) {}

    inner class Slider(val form: Boolean = true) {}
}