package com.frcteam1939.scouting2026.feature.forms.model

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

@Serializable
@SerialName("RadioButton")
data class RadioButton(
    override val id: Int,
    override val type: String = "RadioButton",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val options: List<String> = emptyList()
) : FormField()

@Serializable
@SerialName("Switch")
data class Switch(
    override val id: Int,
    override val type: String = "Switch",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
) : FormField()

@Serializable
@SerialName("Checkbox")
data class Checkbox(
    override val id: Int,
    override val type: String = "Checkbox",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val options: List<String> = emptyList(),
    val maxSelected: Int = 10000,
    val minSelected: Int = 0
) : FormField()

@Serializable
@SerialName("ButtonBar")
data class ButtonBar(
    override val id: Int,
    override val type: String = "ButtonBar",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val options: List<String> = emptyList()
) : FormField()

@Serializable
@SerialName("Counter")
data class Counter(
    override val id: Int,
    override val type: String = "Counter",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val minVal: Int = 0,
    val maxVal: Int = 10000,
    val step: Int = 1
) : FormField()

@Serializable
@SerialName("Dropdown")
data class Dropdown(
    override val id: Int,
    override val type: String = "Dropdown",
    override val label: String,
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
    val options: List<String> = emptyList()
) : FormField()

@Serializable
@SerialName("RebuiltHubScoring")
data class RebuiltHubScoring(
    override val id: Int,
    override val type: String = "RebuiltHubScoring",
    override val label: String = "Cycle Result",
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
) : FormField()

@Serializable
@SerialName("RebuiltStartingPosition")
data class RebuiltStartingPosition(
    override val id: Int,
    override val type: String = "RebuiltStartingPosition",
    override val label: String = "Starting Position",
    override val required: Boolean = false,
    override val leadsTo: List<String> = emptyList(),
) : FormField()
