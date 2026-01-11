package com.frcteam1939.scouting2026.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
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
        radioOptions: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun TextButtonInput(
        onClick: () -> Unit,
        text: String,
        modifier: Modifier = Modifier,
        containerColor: Color = MaterialTheme.colorScheme.tertiary,
        contentColor: Color = MaterialTheme.colorScheme.secondary,
        shape: Shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = { onClick() },
            modifier = modifier,
            colors = ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = shape
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }

    @Composable
    fun SliderInput(

    ) {

    }

    @Composable
    fun CounterInput(

    ) {

    }

    @Composable
    fun SwitchInput(

    ) {

    }
}