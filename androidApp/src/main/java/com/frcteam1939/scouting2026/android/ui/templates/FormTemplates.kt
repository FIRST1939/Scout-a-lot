package com.frcteam1939.scouting2026.android.ui.templates

import com.frcteam1939.scouting2026.android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    var checked by remember { mutableStateOf(List(fieldContent.options.size) { false }) }
    UserInputs.CheckboxInput(
        checked = checked,
        onCheckedChange = { checked = it },
        checkboxOptions = fieldContent.options,
        maxSelected = fieldContent.maxSelected,
        minSelected = fieldContent.maxSelected
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebuiltHubScoringFieldTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: RebuiltHubScoring, auto: Boolean) {
    val accuracyOptions = listOf("Low", "Medium", "High", "Very High")
    val speedOptions = listOf("Slow", "Medium", "Fast", "Very Fast")

    var period by remember { mutableIntStateOf(0) }
    var cycle by remember { mutableIntStateOf(0) }
    var accuracy by remember { mutableIntStateOf(0) }
    var speed by remember { mutableIntStateOf(0) }
    var periodData: List<List<Int>> by remember { mutableStateOf(emptyList()) }

    fun changePeriod(newPeriod: Int) {
        val newItem = listOf(cycle, accuracy, speed)
        val mutable = periodData.toMutableList()

        if (period < mutable.size) {
            mutable[period] = newItem
        } else if (period == mutable.size) {
            mutable.add(newItem)
        }

        periodData = mutable

        if (newPeriod < periodData.size) {
            val data = periodData[newPeriod]
            cycle = data[0]
            accuracy = data[1]
            speed = data[2]
        } else {
            cycle = 0
            accuracy = 0
            speed = 0
        }

        period = newPeriod
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!auto) {
                    Text(text = "Period", style = MaterialTheme.typography.bodyLarge)
                    UserInputs.CounterInput(
                        value = period,
                        maxVal = 7,
                        onValueChange = {
                            changePeriod(it)
                        }
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Cycle", style = MaterialTheme.typography.bodyLarge)
                UserInputs.CounterInput(
                    value = cycle,
                    onValueChange = { cycle = it }
                )
            }
        }
        Divider()
        Text(text = "Accuracy", style = MaterialTheme.typography.bodyLarge)
        UserInputs.SingleSelectButtonBarInput(
            options = accuracyOptions,
            selectedOption = accuracy,
            onOptionSelected = { accuracy = it }
        )
        Divider()
        Text(text = "Speed", style = MaterialTheme.typography.bodyLarge)
        UserInputs.SingleSelectButtonBarInput(
            options = speedOptions,
            selectedOption = speed,
            onOptionSelected = { speed = it }
        )
    }
}