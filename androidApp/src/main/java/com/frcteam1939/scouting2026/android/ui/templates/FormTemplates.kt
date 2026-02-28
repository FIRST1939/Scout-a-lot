package com.frcteam1939.scouting2026.android.ui.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.data.structuring.Checkbox
import com.frcteam1939.scouting2026.data.structuring.Counter
import com.frcteam1939.scouting2026.data.structuring.Dropdown
import com.frcteam1939.scouting2026.data.structuring.RadioButton
import com.frcteam1939.scouting2026.data.structuring.RebuiltHubScoring
import com.frcteam1939.scouting2026.data.structuring.Switch
import com.frcteam1939.scouting2026.data.structuring.Textbox
import com.frcteam1939.scouting2026.viewmodels.ScoutingViewModel
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.data.structuring.ButtonBar
import com.frcteam1939.scouting2026.data.structuring.RebuiltStartingPosition

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
    Text(text = fieldContent.label, style = MaterialTheme.typography.headlineSmall)
    UserInputs.CheckboxInput(
        checked = checked,
        onCheckedChange = { checked = it },
        checkboxOptions = fieldContent.options,
        maxSelected = fieldContent.maxSelected,
        minSelected = fieldContent.maxSelected
    )
}

@Composable
fun ButtonBarTemplate(scoutingViewModel: ScoutingViewModel, fieldContent: ButtonBar) {
    var selectedOption: Int? by remember { mutableStateOf(null) }
    Text(text = fieldContent.label, style = MaterialTheme.typography.headlineSmall)
    UserInputs.SingleSelectButtonBarInput(
        options = fieldContent.options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = if (selectedOption == it) null else it }
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
        onOptionSelected = { selectedOption = it },
        text = fieldContent.label
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebuiltHubScoringFieldTemplate(
    scoutingViewModel: ScoutingViewModel,
    fieldContent: RebuiltHubScoring,
    auto: Boolean
) {
    val accuracyOptions = listOf("Low", "Medium", "High", "Very High")
    val speedOptions = listOf("Slow", "Medium", "Fast", "Very Fast")

    var period by remember { mutableIntStateOf(1) }
    var cycle by remember { mutableIntStateOf(1) }
    var accuracy by remember { mutableStateOf<Int?>(null) }
    var speed by remember { mutableStateOf<Int?>(null) }

    // Period contains cycle contains accuracy & speed
    // Store as nullable: null means no selection, >= 0 means selected index
    var periodData: List<List<List<Int?>>> by remember { mutableStateOf(emptyList()) }

    fun saveCycleData() {
        val cycleItem = listOf(accuracy, speed)
        val mutable = periodData.toMutableList()
        val periodIndex = period - 1

        while (mutable.size <= periodIndex) {
            mutable.add(emptyList())
        }

        val periodCycles = mutable[periodIndex].toMutableList()
        val cycleIndex = cycle - 1

        if (cycleIndex < periodCycles.size) {
            periodCycles[cycleIndex] = cycleItem
        } else if (cycleIndex == periodCycles.size) {
            periodCycles.add(cycleItem)
        }

        mutable[periodIndex] = periodCycles
        periodData = mutable
    }

    fun changeCycle(newCycle: Int) {
        saveCycleData()

        val periodIndex = period - 1
        val cycleIndex = newCycle - 1
        if (periodIndex < periodData.size && cycleIndex < periodData[periodIndex].size) {
            val data = periodData[periodIndex][cycleIndex]
            accuracy = data[0]
            speed = data[1]
        } else {
            accuracy = null
            speed = null
        }

        cycle = newCycle
    }

    fun changePeriod(newPeriod: Int) {
        saveCycleData()

        val newPeriodIndex = newPeriod - 1
        if (newPeriodIndex < periodData.size && periodData[newPeriodIndex].isNotEmpty()) {
            val firstCycleData = periodData[newPeriodIndex][0] // Index 0 = cycle 1
            accuracy = firstCycleData[0]
            speed = firstCycleData[1]
            cycle = 1
        } else {
            cycle = 1
            accuracy = null
            speed = null
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
                        minVal = 1,
                        maxVal = 6,
                        onValueChange = { changePeriod(it) }
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
                    minVal = 1,
                    onValueChange = { changeCycle(it) }
                )
            }
        }
        Divider()
        Text(text = "Accuracy", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconResources = listOf(
                R.drawable.accuracy_low_light to "Low Accuracy",
                R.drawable.accuracy_med_light to "Medium Accuracy",
                R.drawable.accuracy_high_light to "High Accuracy",
                R.drawable.accuracy_vehigh_light to "Very High Accuracy"
            )

            iconResources.forEachIndexed { index, (drawableId, contentDesc) ->
                Icon(
                    painter = painterResource(id = drawableId),
                    contentDescription = contentDesc,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(horizontal = 4.dp)
                )

                if (index < iconResources.size - 1) {
                    Divider(
                        modifier = Modifier
                            .height(72.dp)
                            .width(1.dp)
                    )
                }
            }
        }
        UserInputs.SingleSelectButtonBarInput(
            options = accuracyOptions,
            selectedOption = accuracy,
            onOptionSelected = {
                accuracy = if (accuracy == it) null else it
            }
        )
        Divider()
        Text(text = "Speed", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconResources = listOf(
                R.drawable.speed_slow_light to "Slow Speed",
                R.drawable.speed_med_light to "Medium Speed",
                R.drawable.speed_fast_light to "Fast Speed",
                R.drawable.speed_vefast_light to "Very Fast Speed"
            )

            iconResources.forEachIndexed { index, (drawableId, contentDesc) ->
                Icon(
                    painter = painterResource(id = drawableId),
                    contentDescription = contentDesc,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(horizontal = 4.dp)
                )

                if (index < iconResources.size - 1) {
                    Divider(
                        modifier = Modifier
                            .height(72.dp)
                            .width(1.dp)
                    )
                }
            }
        }
        UserInputs.SingleSelectButtonBarInput(
            options = speedOptions,
            selectedOption = speed,
            onOptionSelected = {
                speed = if (speed == it) null else it
            }
        )
    }
}

@Composable
fun RebuiltStartingPositionTemplate(
    scoutingViewModel: ScoutingViewModel,
    fieldContent: RebuiltStartingPosition,
) {
    // Positions default to blue
    var startingPositions = listOf("Outpost-side Trench", "Outpost-side Bump", "Hub", "Depot-side Bump", "Depot-side Trench")
    if (scoutingViewModel.alliance == "Red") {
        startingPositions = listOf("Depot-side Trench", "Depot-side Bump", "Hub", "Outpost-side Bump", "Outpost-side Trench")
    }
    var selectedPosition by remember { mutableStateOf(startingPositions[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(fieldContent.label, style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image defaults to blue
            var imageResource = R.drawable.blue_starting_zones to "Blue Alliance Starting Positions"
            if (scoutingViewModel.alliance == "Red") {
                imageResource = R.drawable.red_starting_zones to "Red Alliance Starting Positions"
            }

            Image(
                painter = painterResource(id = imageResource.first),
                contentDescription = imageResource.second,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            )
            UserInputs.RadioButtonInput(
                radioOptions = startingPositions,
                selectedOption = selectedPosition,
                onOptionSelected = { selectedPosition = it },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
