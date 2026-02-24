package com.frcteam1939.scouting2026.data.structuring

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FormField {
    abstract val id: Int
    abstract val type: String
    abstract val label: String
    abstract val required: Boolean
    abstract val leadsTo: List<String>
}

@Serializable
@SerialName("Textbox")
data class Textbox(
    override val id: Int,
    override val type: String = "Textbox",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val singleLine: Boolean = false,
    val forceNumberInput: Boolean = false,
    val characterLimit: Int = 2500
) : FormField()

