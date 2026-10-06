package com.frcteam1939.scouting2026.feature.scouting

import com.frcteam1939.scouting2026.feature.forms.model.ButtonBar
import com.frcteam1939.scouting2026.feature.forms.model.Checkbox
import com.frcteam1939.scouting2026.feature.forms.model.Counter
import com.frcteam1939.scouting2026.feature.forms.model.Dropdown
import com.frcteam1939.scouting2026.feature.forms.model.RadioButton
import com.frcteam1939.scouting2026.feature.forms.model.RebuiltHubScoring
import com.frcteam1939.scouting2026.feature.forms.model.RebuiltStartingPosition
import com.frcteam1939.scouting2026.feature.forms.model.Switch
import com.frcteam1939.scouting2026.feature.forms.model.Textbox

object FormFieldManager {

    object FieldTypes {
        // Please add new fields to this list too
        // Uses capital letters because these types are also displayed in-app
        val FIELD_LIST: List<String> = listOf(
            this.TEXTBOX,
            this.RADIOBUTTON,
            this.SWITCH,
            this.CHECKBOX,
            this.BUTTONBAR,
            this.COUNTER,
            this.DROPDOWN,
            this.REBUILT_HUB_SCORING,
            this.REBUILT_STARTING_POSITION
        )
        const val TEXTBOX: String = "Textbox"
        const val RADIOBUTTON: String = "RadioButton"
        const val SWITCH: String = "Switch"
        const val CHECKBOX: String = "Checkbox"
        const val BUTTONBAR: String = "ButtonBar"
        const val COUNTER: String = "Counter"
        const val DROPDOWN: String = "Dropdown"
        const val REBUILT_HUB_SCORING: String = "RebuiltHubScoring"
        const val REBUILT_STARTING_POSITION: String = "RebuiltStartingPosition"

    }

    fun getTextBoxJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), singleLine: Boolean = false, forceNumberInput: Boolean = false, characterLimit: Int = 2500): Textbox {
        return Textbox(id = id, label = label, required = required, leadsTo = leadsTo, singleLine = singleLine, forceNumberInput = forceNumberInput, characterLimit = characterLimit)
    }

    fun getRadioButtonJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), options: List<String>): RadioButton {
        return RadioButton(id = id, label = label, required = required, leadsTo = leadsTo, options = options)
    }

    fun getSwitchJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList()): Switch {
        return Switch(id = id, label = label, required = required, leadsTo = leadsTo)
    }

    fun getCheckboxJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), options: List<String> = emptyList(), maxSelected: Int = 10000, minSelected: Int = 0): Checkbox {
        return Checkbox(id = id, label = label, required = required, leadsTo = leadsTo, options = options, maxSelected = maxSelected, minSelected = minSelected)
    }

    fun getCounterJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), minVal: Int = 0, maxVal: Int = 10000, step: Int = 1): Counter {
        return Counter(id = id, label = label, required = required, leadsTo = leadsTo, minVal = minVal, maxVal = maxVal, step = step)
    }

    fun getDropdownJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), options: List<String> = emptyList()): Dropdown {
        return Dropdown(id = id, label = label, required = required, leadsTo = leadsTo, options = options)
    }

    fun getRebuiltHubScoringJson(id: Int, label: String = "Cycle Result", required: Boolean = false, leadsTo: List<String> = emptyList()): RebuiltHubScoring {
        return RebuiltHubScoring(id = id, label = label, required = required, leadsTo = leadsTo)
    }

    fun getRebuiltStartingPositionJson(id: Int, label: String = "Starting Position", required: Boolean = false, leadsTo: List<String> = emptyList()): RebuiltStartingPosition {
        return RebuiltStartingPosition(id = id, label = label, required = required, leadsTo = leadsTo)
    }
}
