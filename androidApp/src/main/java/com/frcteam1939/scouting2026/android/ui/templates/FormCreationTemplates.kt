package com.frcteam1939.scouting2026.android.ui.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.viewmodels.EditFormsViewModel
import kotlin.collections.emptyList

@Composable
fun FieldTopbar(
    text: String
) {
    UserInputs.TripleOutlinedButtonTextBarInput(
        text,
        iconOne = Icons.Default.Delete,
        onButtonOneClick = {},
        iconTwo = Icons.Default.KeyboardArrowUp,
        onButtonTwoClick = {},
        buttonTwoText = "Move Up",
        iconThree = Icons.Default.KeyboardArrowDown,
        onButtonThreeClick = {},
        buttonThreeText = "Move Down"
    )
}

@Composable
fun TextboxFieldCTemplate(editFormsViewModel: EditFormsViewModel, id: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        FieldTopbar(text = "Textbox")
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInputs.TextboxInput(
                value = "Unnamed Textbox",
                onValueChange = {},
                label = "Label:",
                singleLine = true,
                characterLimit = 30
            )
            UserInputs.TextboxInput(
                value = "2500",
                onValueChange = {},
                label = "Character Limit:",
                singleLine = true,
                forceNumberInput = true,
                characterLimit = 4
            )
        }
        Divider()
        UserInputs.SwitchTextBarInput(
            checked = false,
            onCheckedChange = {},
            text = "Single Line:"
        )
        Divider()
        UserInputs.SwitchTextBarInput(
            checked = false,
            onCheckedChange = {},
            text = "Force Number Input:"
        )
        Divider()
        UserInputs.SwitchTextBarInput(
            checked = false,
            onCheckedChange = {},
            text = "Required:"
        )
        Divider()
        UserInputs.DropdownTextBarInput(
            options = emptyList(),
            selectedOption = "None Selected",
            onOptionSelected = {},
            text = "Leads to:"
        )
    }
}