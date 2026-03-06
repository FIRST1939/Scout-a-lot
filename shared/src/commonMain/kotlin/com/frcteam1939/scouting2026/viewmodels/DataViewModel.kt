package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlin.collections.emptyList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class DataViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository
) : ViewModel() {
    val settings: StateFlow<AppSettings?> = settingsRepository.settings
    val currentEvent = settings.value?.currentEvent ?: ""
    private val _currentTeam = MutableStateFlow("")
    val currentTeam: StateFlow<String> = _currentTeam.asStateFlow()
    fun changeCurrentTeam(newCurrentTeam: String) {
        _currentTeam.value = newCurrentTeam
    }

    private val _currentPitData = MutableStateFlow<List<Field>>(emptyList())
    val currentPitData: StateFlow<List<Field>> = _currentPitData.asStateFlow()
    fun changeCurrentPitData(newCurrentPitData: List<Field>) {
        _currentPitData.value = newCurrentPitData
    }

    private val _currentAutoData = MutableStateFlow<List<List<Field>>>(emptyList())
    val currentAutoData: StateFlow<List<List<Field>>> = _currentAutoData.asStateFlow()
    fun changeCurrentAutoData(newCurrentAutoData: List<List<Field>>) {
        _currentAutoData.value = newCurrentAutoData
    }

    private val _currentTeleopData = MutableStateFlow<List<List<Field>>>(emptyList())
    val currentTeleopData: StateFlow<List<List<Field>>> = _currentTeleopData.asStateFlow()
    fun changeCurrentTeleopData(newCurrentTeleopData: List<List<Field>>) {
        _currentTeleopData.value = newCurrentTeleopData
    }

    private val _currentEndgameData = MutableStateFlow<List<List<Field>>>(emptyList())
    val currentEndgameData: StateFlow<List<List<Field>>> = _currentEndgameData.asStateFlow()
    fun changeCurrentEndgameData(newCurrentEndgameData: List<List<Field>>) {
        _currentEndgameData.value = newCurrentEndgameData
    }

    fun averageList(list: List<Int>): Double {
        if (list.isEmpty()) return 0.0
        var total = 0.0
        list.forEach {
            total += it
        }
        return total / list.size
    }

    suspend fun updateRawData(team: String) {
        val rawPitData = scoutedDataRepository.getPitData(team, "OKC Pit Form 2026").first()?.scoutedData ?: ""
        val rawMatchData = scoutedDataRepository.getAllMatchesForTeam(team, "OKC Match Form 2026").first()

        val matches = rawMatchData.map { match ->
            Json.decodeFromString<List<List<Field>>>(match.scoutedData)
        }

        changeCurrentPitData(Json.decodeFromString<List<Field>>(rawPitData))
        changeCurrentAutoData(matches.mapNotNull { it.getOrNull(0) })
        changeCurrentTeleopData(matches.mapNotNull { it.getOrNull(1) })
        changeCurrentEndgameData(matches.mapNotNull { it.getOrNull(2) })
    }

    fun parseRebuiltHubData(input: String): List<List<List<Int?>>> {
        fun parseCycle(cycleStr: String): List<Int?> {
            val cleaned = cycleStr.trim().removePrefix("[").removeSuffix("]").trim()
            return cleaned.split(",").map {
                it.trim().let { value ->
                    if (value == "null") null else value.toIntOrNull()
                }
            }
        }

        fun parsePeriod(periodStr: String): List<List<Int?>> {
            val cleaned = periodStr.trim().removePrefix("[").removeSuffix("]").trim()
            if (cleaned.isEmpty()) return emptyList()

            val cycles = mutableListOf<List<Int?>>()
            var depth = 0
            var currentCycle = StringBuilder()

            for (char in cleaned) {
                when (char) {
                    '[' -> {
                        depth++
                        currentCycle.append(char)
                    }
                    ']' -> {
                        depth--
                        currentCycle.append(char)
                        if (depth == 0) {
                            cycles.add(parseCycle(currentCycle.toString()))
                            currentCycle.clear()
                        }
                    }
                    ',' -> {
                        if (depth == 0) {
                            continue
                        } else {
                            currentCycle.append(char)
                        }
                    }
                    else -> currentCycle.append(char)
                }
            }

            return cycles
        }

        if (input.isBlank()) return emptyList()

        return try {
            // Remove outer brackets and whitespace
            val cleaned = input.trim().removePrefix("[").removeSuffix("]").trim()
            if (cleaned.isEmpty()) return emptyList()

            val result = mutableListOf<List<List<Int?>>>()
            var depth = 0
            var currentSegment = StringBuilder()

            for (char in cleaned) {
                when (char) {
                    '[' -> {
                        depth++
                        currentSegment.append(char)
                    }
                    ']' -> {
                        depth--
                        currentSegment.append(char)
                        // When we close a period (depth becomes 0)
                        if (depth == 0) {
                            val periodStr = currentSegment.toString()
                            result.add(parsePeriod(periodStr))
                            currentSegment.clear()
                        }
                    }
                    ',' -> {
                        if (depth == 0) {
                            // Skip commas between periods
                            continue
                        } else {
                            currentSegment.append(char)
                        }
                    }
                    else -> currentSegment.append(char)
                }
            }

            result
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getAvgSpeedAndAccuracyAuto(): List<String> {
        if (currentAutoData.value.isEmpty()) return listOf("Unknown", "Unknown")
        return try {
            val accuracyList = mutableListOf<Int>()
            val speedList = mutableListOf<Int>()
            var accuracy: Number = 0
            var speed: Number = 0

            currentAutoData.value.forEach { scoutedData ->
                if (scoutedData[5].fieldContent.isNotEmpty()) {
                    val scoring = parseRebuiltHubData(scoutedData[5].fieldContent)
                    val accuracyListInMatch = scoring.flatten().mapNotNull {
                        it[0]
                    }
                    val speedListInMatch = scoring.flatten().mapNotNull {
                        it[1]
                    }

                    println("Acc list in match: $accuracyListInMatch")
                    println("Speed list in match: $speedListInMatch")

                    accuracyList.addAll(accuracyListInMatch)
                    speedList.addAll(speedListInMatch)
                } else {
                    listOf("Unknown", "Unknown")
                }
            }

            println(accuracyList)
            println(speedList)
            accuracy = averageList(accuracyList)
            speed = averageList(speedList)
            listOf(accuracy.toString().take(4), speed.toString().take(4))
        } catch (e: Exception) {
            listOf("Unknown", "Unknown")
        }
    }

    fun getAvgSpeedAndAccuracyTeleop(): List<String> {
        if (currentTeleopData.value.isEmpty()) return listOf("Unknown", "Unknown")
        return try {
            val accuracyList = mutableListOf<Int>()
            val speedList = mutableListOf<Int>()
            var accuracy: Number = 0
            var speed: Number = 0

            currentTeleopData.value.forEach { scoutedData ->
                if (scoutedData[5].fieldContent.isNotEmpty()) {
                    val scoring = parseRebuiltHubData(scoutedData[5].fieldContent)
                    val accuracyListInMatch = scoring.flatten().mapNotNull {
                        it[0]
                    }
                    val speedListInMatch = scoring.flatten().mapNotNull {
                        it[1]
                    }

                    println("Acc list in match: $accuracyListInMatch")
                    println("Speed list in match: $speedListInMatch")

                    accuracyList.addAll(accuracyListInMatch)
                    speedList.addAll(speedListInMatch)
                } else {
                    listOf("Unknown", "Unknown")
                }
            }

            println(accuracyList)
            println(speedList)
            accuracy = averageList(accuracyList)
            speed = averageList(speedList)
            listOf(accuracy.toString().take(4), speed.toString().take(4))
        } catch (e: Exception) {
            listOf("Unknown", "Unknown")
        }
    }

    fun getApproximateCapacity(): String? {
        if (currentPitData.value.isEmpty()) return "Unknown"
        return try {
            if (currentPitData.value[2].fieldContent == "") {
                "Unknown"
            } else {
                currentPitData.value[2].fieldContent
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun getDrivetrain(): String? {
        if (currentPitData.value.isEmpty()) return "Unknown"
        return try {
            if (currentPitData.value[4].fieldContent == "") {
                "Unknown"
            } else {
                currentPitData.value[4].fieldContent
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun getAvgFerryVolume(): String {
        if (currentTeleopData.value.isEmpty()) return "Unknown"
        return try {
            val valueList = mutableListOf<Int>()

            currentTeleopData.value.forEach { scoutedData ->
                if (scoutedData[5].fieldContent.toIntOrNull() != null) {
                    valueList.add(scoutedData[5].fieldContent.toInt())
                }
            }

            if (valueList.isEmpty()) {
                "Unknown"
            } else {
                averageList(valueList).toString()
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    suspend fun getTeamList(): List<String> {
        return eventsRepository.getEventByName(currentEvent).first()?.eventTeamList
            ?.mapNotNull { it.toIntOrNull() }
            ?.sorted()
            ?.map { it.toString() }
            ?: emptyList()
    }
}


//Json.decodeFromString<List<Field>>