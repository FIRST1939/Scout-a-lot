package com.frcteam1939.scouting2026.android.ui.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.data.structuring.Checkbox
import com.frcteam1939.scouting2026.data.structuring.Counter
import com.frcteam1939.scouting2026.data.structuring.Dropdown
import com.frcteam1939.scouting2026.data.structuring.FormField
import com.frcteam1939.scouting2026.data.structuring.RadioButton
import com.frcteam1939.scouting2026.data.structuring.RebuiltHubScoring
import com.frcteam1939.scouting2026.data.structuring.Switch
import com.frcteam1939.scouting2026.data.structuring.Textbox
import com.frcteam1939.scouting2026.viewmodels.EditFormsViewModel
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel

@Composable
fun TextboxFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: Textbox) {
    var value by remember { mutableStateOf("") }
    UserInputs.TextboxInput(
        value = value,
        onValueChange = { value = it },
        label = fieldContent.label,
        singleLine = fieldContent.singleLine,
        forceNumberInput = fieldContent.forceNumberInput,
        characterLimit = fieldContent.characterLimit
    )
}

@Composable
fun RadioButtonFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: RadioButton) {
    var selectedOption by remember { mutableStateOf(fieldContent.options[0]) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = fieldContent.label, style = MaterialTheme.typography.bodyLarge)
        UserInputs.RadioButtonInput(
            radioOptions = fieldContent.options,
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )
    }
}

@Composable
fun SwitchFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: Switch) {
    var checked by remember { mutableStateOf(false) }
    UserInputs.SwitchTextBarInput(
        checked = checked,
        onCheckedChange = { checked = it },
        text = fieldContent.label
    )
}

@Composable
fun CheckboxFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: Checkbox) {
}

@Composable
fun CounterFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: Counter) {
    var value by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = fieldContent.label, style = MaterialTheme.typography.bodyLarge)
        UserInputs.CounterInput(
            value = value,
            minVal = fieldContent.minVal,
            maxVal = fieldContent.maxVal,
            step = fieldContent.step,
            onValueChange = { value = it },
        )
    }
}

@Composable
fun DropdownFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: Dropdown) {
    var selectedOption by remember { mutableStateOf(fieldContent.options[0]) }
    UserInputs.DropdownTextBarInput(
        options = fieldContent.options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it},
        text = fieldContent.label
    )
}

@Composable
fun RebuiltHubScoringFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: RebuiltHubScoring) {

}