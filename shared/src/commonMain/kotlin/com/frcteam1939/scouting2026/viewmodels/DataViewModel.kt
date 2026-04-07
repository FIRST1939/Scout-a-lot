package com.frcteam1939.scouting2026.viewmodels

import androidx.lifecycle.ViewModel
import com.frcteam1939.scouting2026.api.models.TBAMatch
import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlin.collections.emptyList
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

    private val _currentMatch = MutableStateFlow("")
    val currentMatch: StateFlow<String> = _currentMatch.asStateFlow()
    fun changeCurrentMatch(newCurrentMatch: String) {
        _currentMatch.value = newCurrentMatch
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

    suspend fun getMatches(): List<String> {
        val event = eventsRepository.getEventByName(settings.value?.currentEvent ?: "")
        val jsonString = event.first()?.matchInfo
        if (jsonString.isNullOrEmpty()) return emptyList()
        val matches = Json.decodeFromString<List<TBAMatch>>(jsonString)

        val levelOrder = mapOf(
            "qm" to 0,
            "sf" to 1,
            "f" to 2
        )

        return matches
            .sortedWith(
                compareBy<TBAMatch> { levelOrder[it.compLevel] ?: Int.MAX_VALUE }
                    .thenBy { it.matchNumber }
            )
            .map { match ->
                val type = when (match.compLevel) {
                    "qm" -> "Qualification"
                    "sf" -> "Semifinals"
                    "f" -> "Finals"
                    else -> "Unknown"
                }

                "$type ${match.matchNumber}"
            }
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
        val rawPitData = scoutedDataRepository.getPitData(team, "OKC Pit Form 2026", currentEvent).first()?.scoutedData ?: "[]"
        val rawMatchData = scoutedDataRepository.getAllMatchesForTeam(team, "OKC Match Form 2026", currentEvent).first()

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
        val data = currentAutoData.value
        if (data.isEmpty()) return listOf("Unknown", "Unknown")

        return try {
            val accuracyList = mutableListOf<Int>()
            val speedList = mutableListOf<Int>()

            data.forEach { scoutedData ->
                // Use isNotBlank() to avoid processing empty/whitespace strings
                if (scoutedData.getOrNull(4)?.fieldContent?.isNotBlank() == true) {
                    val scoring = parseRebuiltHubData(scoutedData[4].fieldContent)
                    val flattened = scoring.flatten()
                    println("scoring: $scoring")
                    println("flattened: $flattened")

                    accuracyList.addAll(flattened.mapNotNull { it[0] })
                    speedList.addAll(flattened.mapNotNull { it[1] })
                }
            }

            if (accuracyList.isEmpty() || speedList.isEmpty()) return listOf("Unknown", "Unknown")

            // Use average() and format properly
            val avgAcc = accuracyList.average()
            val avgSpeed = speedList.average()

            // Formats to 1 decimal place (e.g., "85.2")
            listOf(avgAcc.toString().take(4), avgSpeed.toString().take(4))
        } catch (e: Exception) {
            listOf("Unknown", "Unknown")
        }
    }

    fun getAvgSpeedAndAccuracyTeleop(): List<String> {
        val data = currentTeleopData.value
        if (data.isEmpty()) return listOf("Unknown", "Unknown")

        return try {
            val accuracyList = mutableListOf<Int>()
            val speedList = mutableListOf<Int>()

            data.forEach { scoutedData ->
                // Use isNotBlank() to avoid processing empty/whitespace strings
                if (scoutedData.getOrNull(6)?.fieldContent?.isNotBlank() == true) {
                    val scoring = parseRebuiltHubData(scoutedData[6].fieldContent)
                    val flattened = scoring.flatten()
                    println("scoring: $scoring")
                    println("flattened: $flattened")

                    accuracyList.addAll(flattened.mapNotNull { it[0] })
                    speedList.addAll(flattened.mapNotNull { it[1] })
                }
            }

            if (accuracyList.isEmpty() || speedList.isEmpty()) return listOf("Unknown", "Unknown")

            // Use average() and format properly
            val avgAcc = accuracyList.average()
            val avgSpeed = speedList.average()

            // Formats to 1 decimal place (e.g., "85.2")
            listOf(avgAcc.toString().take(4), avgSpeed.toString().take(4))
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

    fun avgAutoCycleCount(): String {
        val data = currentAutoData.value
        if (data.isEmpty()) return "Unknown"

        return try {
            val cycleCountList = mutableListOf<Int>()

            data.forEach { scoutedData ->
                var cycles = 0

                if (scoutedData.getOrNull(5)?.fieldContent?.isNotBlank() == true) {
                    val scoring = parseRebuiltHubData(scoutedData[5].fieldContent)
                    scoring.forEach { period ->
                        period.forEach { cycle ->
                            if (cycle.any { it != null }) {
                                cycles += 1
                            }
                        }
                    }
                }

                cycleCountList.add(cycles)
            }

            if (cycleCountList.isEmpty()) return "Unknown"

            val avgCycles = cycleCountList.average()
            avgCycles.toString().take(4)
        } catch (e: Exception) {
            return "Unknown"
        }
    }

    fun avgTeleopCycleCount(): String {
        val data = currentTeleopData.value
        if (data.isEmpty()) return "Unknown"

        return try {
            val cycleCountList = mutableListOf<Int>()

            data.forEach { scoutedData ->
                var cycles = 0

                if (scoutedData.getOrNull(6)?.fieldContent?.isNotBlank() == true) {
                    val scoring = parseRebuiltHubData(scoutedData[6].fieldContent)
                    scoring.forEach { period ->
                        period.forEach { cycle ->
                            if (cycle.any { it != null }) {
                                cycles += 1
                            }
                        }
                    }
                }

                cycleCountList.add(cycles)
            }

            if (cycleCountList.isEmpty()) return "Unknown"

            val avgCycles = cycleCountList.average()
            avgCycles.toString().take(4)
        } catch (e: Exception) {
            return "Unknown"
        }
    }

    fun getPlayedDefense(): Boolean {
        if (currentTeleopData.value.isEmpty()) return false
        var playsDefense = false

        currentTeleopData.value.forEach { scoutedData ->
            if (scoutedData[2].fieldContent == "true") {
                playsDefense = true
            }
        }

        return playsDefense
    }

    fun getTrenchTravel(): Boolean {
        if (currentTeleopData.value.isEmpty()) return false
        var trench = false

        currentTeleopData.value.forEach { scoutedData ->
            val travelList = scoutedData[0].fieldContent
                .removeSurrounding("[", "]")
                .split(",")
                .map { it.trim() }
            println("Travel List trench: $travelList")
            if (travelList.getOrNull(0)?.toBoolean() ?: false) { trench = true }
        }

        return trench
    }

    fun getBumpTravel(): Boolean {
        if (currentTeleopData.value.isEmpty()) return false
        var bump = false

        currentTeleopData.value.forEach { scoutedData ->
            val travelList = scoutedData[0].fieldContent
                .removeSurrounding("[", "]")
                .split(",")
                .map { it.trim() }
            println("Travel List bump: $travelList")
            if (travelList.getOrNull(1)?.toBoolean() ?: false) { bump = true }
        }

        return bump
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
                averageList(valueList).toString().take(4)
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    //fun getClimbAbilities(): List<String> {

    //}

    fun getDeadMatchCount(): Int {
        if (currentEndgameData.value.isEmpty()) return 0
        var deadMatchCount = 0

        currentEndgameData.value.forEach { scoutedData ->
            if (scoutedData[1].fieldContent == "true") {
                deadMatchCount += 1
            }
        }

        return deadMatchCount
    }

    fun getQualitativeComments(): List<String> {
        if (currentEndgameData.value.isEmpty()) return listOf("Unknown")
        val commentList = mutableListOf<String>()

        currentEndgameData.value.forEach { scoutedData ->
            if (scoutedData[2].fieldContent.isNotBlank()) {
                commentList.add(scoutedData[2].fieldContent)
            }
        }

        return commentList
    }

    suspend fun getTeamList(): List<String> {
        return eventsRepository.getEventByName(currentEvent).first()?.eventTeamList
            ?.mapNotNull { it.toIntOrNull() }
            ?.sorted()
            ?.map { it.toString() }
            ?: emptyList()
    }
}
