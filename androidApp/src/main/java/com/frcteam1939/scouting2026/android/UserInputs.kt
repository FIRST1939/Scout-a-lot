package com.frcteam1939.scouting2026.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
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
        var keyboardOptions: KeyboardOptions = KeyboardOptions.Default
        if (forceNumberInput) keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        TextField(
            value = value,
            onValueChange = { input ->
                //state can't be negative if forceNumberInput is true
                var validUpdate = true
                if (input.length > characterLimit) validUpdate = false
                if (forceNumberInput && input.toIntOrNull() == null) validUpdate = false
                if (input.isEmpty()) validUpdate = true
                if (validUpdate) onValueChange(input) },
            keyboardOptions = keyboardOptions,
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
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }

    @Composable
    fun SwitchTextBarInput(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        text: String
    ) {
        Divider(thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        Divider(thickness = 1.dp)
    }

    @Composable
    fun OutlinedButtonTextBarInput(
        text: String,
        buttonText: String,
        icon: ImageVector,
        onClick: () -> Unit
    ) {
        Divider(thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            OutlinedButton(
                onClick = { onClick },
            ) {
                Text(text = buttonText)
                Icon(
                    imageVector = icon,
                    contentDescription = "Dropdown arrow"
                )
            }
        }
        Divider(thickness = 1.dp)
    }

    @Composable
    fun DropdownTextBarInput(
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit,
        text: String
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(selectedOption) }

        Divider(thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                ) {
                    Text(text = selectedOption)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown arrow"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    content = {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onOptionSelected(option)
                                    selectedOption = option
                                    expanded = false // Close the menu
                                },
                            )
                        }
                    }
                )
            }
        }
        Divider(thickness = 1.dp)
    }
}