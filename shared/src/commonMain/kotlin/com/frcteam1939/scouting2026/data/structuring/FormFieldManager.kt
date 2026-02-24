package com.frcteam1939.scouting2026.data.structuring

object FormFieldManager {

    object FieldTypes {
        // Please add new fields to this list too
        // Uses capital letters because these types are also displayed in-app
        val FIELD_LIST: List<String> = listOf(
            this.TEXTBOX
        )
        const val TEXTBOX = "Textbox"
    }

    fun getTextBoxJson(id: Int, label: String, required: Boolean = false, leadsTo: List<String> = emptyList(), singleLine: Boolean = false, forceNumberInput: Boolean = false, characterLimit: Int = 2500): Textbox {
        return Textbox(id = id, label = label, required = required, leadsTo = leadsTo, singleLine = singleLine, forceNumberInput = forceNumberInput, characterLimit = characterLimit)
    }
}
