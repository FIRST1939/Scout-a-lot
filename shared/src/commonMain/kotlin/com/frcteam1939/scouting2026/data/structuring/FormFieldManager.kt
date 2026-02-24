package com.frcteam1939.scouting2026.data.structuring

object FormFieldManager {

    object FieldTypes {
        // Please add new fields to this list too
        // Uses capital letters because these types are also displayed in-app
        val FIELD_LIST: List<String> = listOf(
            this.TEXTBOX,
            this.RADIOBUTTON,
            this.SWITCH,
            this.CHECKBOX,
            this.COUNTER,
            this.DROPDOWN,
            this.REBUILT_HUB_SCORING
        )
        const val TEXTBOX: String = "Textbox"
        const val RADIOBUTTON: String = "RadioButton"
        const val SWITCH: String = "Switch"
        const val CHECKBOX: String = "Checkbox"
        const val COUNTER: String = "Counter"
        const val DROPDOWN: String = "Dropdown"
        const val REBUILT_HUB_SCORING: String = "RebuiltHubScoring"

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
}
