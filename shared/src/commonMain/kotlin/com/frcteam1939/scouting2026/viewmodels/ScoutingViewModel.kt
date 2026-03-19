package com.frcteam1939.scouting2026.viewmodels

// android ViewModel import can be replaced with a general ViewModel class later, im too lazy
import androidx.collection.DoubleList
import androidx.lifecycle.ViewModel

import com.frcteam1939.scouting2026.data.entities.AppSettings
import com.frcteam1939.scouting2026.data.entities.ScoutedData
import com.frcteam1939.scouting2026.data.repositories.EventsRepository
import com.frcteam1939.scouting2026.data.repositories.ScoutedDataRepository
import com.frcteam1939.scouting2026.data.repositories.SettingsRepository
import com.frcteam1939.scouting2026.showAlert
import com.frcteam1939.scouting2026.tba.models.TBAMatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Field(
    val id: Int,
    val fieldName: String,
    val fieldType: String,
    val fieldContent: String
)

class ScoutingViewModel(
    private val settingsRepository: SettingsRepository,
    private val eventsRepository: EventsRepository,
    private val scoutedDataRepository: ScoutedDataRepository,
) : ViewModel() {
    // Can be "Auto", "Teleop", "Endgame", or "Pit"
    private val _currentPage = MutableStateFlow("Auto")
    val currentPage: StateFlow<String> = _currentPage.asStateFlow()
    fun changeCurrentPage(newCurrentPage: String) {
        _currentPage.value = newCurrentPage
    }

    val DEFAULT_FORM_MATCH_AUTO_JSON = """
[
  { "id": 0, "fieldName": "Scouter Name", "fieldType": "textbox", "fieldContent": "" },
  { "id": 1, "fieldName": "Starting Position", "fieldType": "starting_position", "fieldContent": "null" },
  { "id": 2, "fieldName": "Robot is preloaded?", "fieldType": "switch", "fieldContent": "false" },
  { "id": 3, "fieldName": "Robot shoots?", "fieldType": "switch", "fieldContent": "false" },
  { "id": 4, "fieldName": "Robot picks up from...", "fieldType": "checkbox", "fieldContent": "" },
  { "id": 5, "fieldName": "Hub Scoring", "fieldType": "hub_scoring_auto", "fieldContent": "null" },
  { "id": 6, "fieldName": "Robot climbs tower?", "fieldType": "switch", "fieldContent": "false" }
]
""".trimIndent()
    val DEFAULT_FORM_MATCH_TELEOP_JSON = """
[
  { "id": 0, "fieldName": "Travel", "fieldType": "checkbox", "fieldContent": "" },
  { "id": 1, "fieldName": "Difficulty travelling", "fieldType": "switch", "fieldContent": "false" },
  { "id": 2, "fieldName": "Plays defense", "fieldType": "switch", "fieldContent": "false" },
  { "id": 3, "fieldName": "Robot picks up from...", "fieldType": "checkbox", "fieldContent": "" },
  { "id": 4, "fieldName": "Pushes fuel into outpost", "fieldType": "switch", "fieldContent": "false" },
  { "id": 5, "fieldName": "Ferry volume", "fieldType": "button_bar", "fieldContent": "null" },
  { "id": 6, "fieldName": "Hub Scoring", "fieldType": "hub_scoring_teleop", "fieldContent": "null" }
]
""".trimIndent()
    val DEFAULT_FORM_MATCH_ENDGAME_JSON = """
[
  { "id": 0, "fieldName": "Tower level", "fieldType": "dropdown", "fieldContent": "" },
  { "id": 1, "fieldName": "Robot died during match?", "fieldType": "switch", "fieldContent": "false" },
  { "id": 2, "fieldName": "Qualitative notes", "fieldType": "textbox", "fieldContent": "" }
]
""".trimIndent()
    val DEFAULT_FORM_PIT_JSON = """
[
  { "id": 0, "fieldName": "Scouter Names", "fieldType": "textbox", "fieldContent": "" },
  { "id": 1, "fieldName": "Robot picks up from...", "fieldType": "checkbox", "fieldContent": "[]" },
  { "id": 2, "fieldName": "Approximate Fuel Capacity", "fieldType": "textbox", "fieldContent": "" },
  { "id": 3, "fieldName": "Where can the robot travel", "fieldType": "checkbox", "fieldContent": "[]" },
  { "id": 4, "fieldName": "Robot's Drivetrain", "fieldType": "textbox", "fieldContent": "" },
  { "id": 5, "fieldName": "What levels can it climb?", "fieldType": "checkbox", "fieldContent": "[]" },
  { "id": 6, "fieldName": "Auto Capabilities?", "fieldType": "textbox", "fieldContent": "" },
  { "id": 7, "fieldName": "Any other notes go here:", "fieldType": "textbox", "fieldContent": "" }
]
    """.trimIndent()

    private val _matchAutoJson = MutableStateFlow(Json.decodeFromString<List<Field>>(DEFAULT_FORM_MATCH_AUTO_JSON))
    val matchAutoJson: StateFlow<List<Field>> = _matchAutoJson.asStateFlow()
    fun changeMatchAutoJson(newMatchAutoJson: List<Field>) {
        _matchAutoJson.value = newMatchAutoJson
    }

    private val _matchTeleopJson = MutableStateFlow(Json.decodeFromString<List<Field>>(DEFAULT_FORM_MATCH_TELEOP_JSON))
    val matchTeleopJson: StateFlow<List<Field>> = _matchTeleopJson.asStateFlow()
    fun changeMatchTeleopJson(newMatchTeleopJson: List<Field>) {
        _matchTeleopJson.value = newMatchTeleopJson
    }

    private val _matchEndgameJson = MutableStateFlow(Json.decodeFromString<List<Field>>(DEFAULT_FORM_MATCH_ENDGAME_JSON))
    val matchEndgameJson: StateFlow<List<Field>> = _matchEndgameJson.asStateFlow()
    fun changeMatchEndgameJson(newMatchEndgameJson: List<Field>) {
        _matchEndgameJson.value = newMatchEndgameJson
    }

    private val _pitJson = MutableStateFlow(Json.decodeFromString<List<Field>>(DEFAULT_FORM_PIT_JSON))
    val pitJson: StateFlow<List<Field>> = _pitJson.asStateFlow()
    fun changePitJson(newPitJson: List<Field>) {
        _pitJson.value = newPitJson
    }

    fun resetFormJsons() {
        _matchAutoJson.value = Json.decodeFromString(DEFAULT_FORM_MATCH_AUTO_JSON)
        _matchTeleopJson.value = Json.decodeFromString(DEFAULT_FORM_MATCH_TELEOP_JSON)
        _matchEndgameJson.value = Json.decodeFromString(DEFAULT_FORM_MATCH_ENDGAME_JSON)
        _pitJson.value = Json.decodeFromString(DEFAULT_FORM_PIT_JSON)
    }

    fun saveFieldValue(id: Int, fieldContent: String) {
        val updateList: (List<Field>) -> List<Field> = { list ->
            list.map { field ->
                if (field.id == id) field.copy(fieldContent = fieldContent) else field
            }
        }

        when (currentPage.value) {
            "Auto" -> _matchAutoJson.value = updateList(_matchAutoJson.value)
            "Teleop" -> _matchTeleopJson.value = updateList(_matchTeleopJson.value)
            "Endgame" -> _matchEndgameJson.value = updateList(_matchEndgameJson.value)
            "Pit" -> _pitJson.value = updateList(_pitJson.value)
            else -> println("ERROR: Invalid page, could not save field data!")
        }
    }

    fun getFieldJson(): StateFlow<List<Field>> {
        return when (currentPage.value) {
            "Auto" -> _matchAutoJson
            "Teleop" -> _matchTeleopJson
            "Endgame" -> _matchEndgameJson
            "Pit" -> _pitJson
            else -> _matchAutoJson // default
        }
    }

    val settings: StateFlow<AppSettings?> = settingsRepository.settings
    val alliance: String?
        get() = settings.value?.deviceRobotAssignment?.dropLast(2)
    val robotNumber: Int?
        get() = settings.value?.deviceRobotAssignment?.takeLast(1)?.toIntOrNull()
    private val _matchNumber = MutableStateFlow("")
    val matchNumber: StateFlow<String> = _matchNumber.asStateFlow()
    fun changeMatchNumber(newMatchNumber: String) {
        _matchNumber.value = newMatchNumber
    }

    private val _selectedTeam = MutableStateFlow("")
    val selectedTeam: StateFlow<String> = _selectedTeam.asStateFlow()
    fun changeSelectedTeam(newTeam: String) {
        _selectedTeam.value = newTeam
    }

    suspend fun getPitTeamList(): List<String> {
        val deviceName = settings.value?.deviceRobotAssignment
        var deviceTag = 0
        val eventName = settings.value?.currentEvent
        if (eventName == null || eventName == "") {
            showAlert("Error getting event teams:", "Current event not found. Make sure you select an event!")
            return emptyList()
        }
        var teamList = eventsRepository.getEventByName(eventName).first()?.eventTeamList ?: emptyList()
        // sorts the teamList to make it consistent and smallest to largest
        teamList = teamList.mapNotNull { it.toIntOrNull() }.sorted().map { it.toString() }
        val deviceTeamList = mutableListOf<String>()
        when(deviceName) {
            "Red 1" -> deviceTag = 0
            "Red 2" -> deviceTag = 1
            "Red 3" -> deviceTag = 2
            "Blue 1" -> deviceTag = 3
            "Blue 2" -> deviceTag = 4
            "Blue 3" -> deviceTag = 5
        }
        while (deviceTag < teamList.size) {
            deviceTeamList.add(teamList[deviceTag])
            deviceTag += 6
        }
        return (deviceTeamList)
    }

    suspend fun getCurrentForm(currentEvent: String, formType: String): String {
        val event = eventsRepository.getEventByName(currentEvent).first() ?: return "None Selected"
        return when (formType) {
            "pit" -> event.pitForm
            "match" -> event.matchForm
            else -> "ERROR: Invalid form type"
        }
    }

    // returns "Not Started" for no data, "Incomplete" for incomplete data, "Complete" for complete data
    suspend fun getPitDataStatus(team: String): String {
        val rawPitData = scoutedDataRepository.getPitData(team, "OKC Pit Form 2026").first()?.scoutedData
            ?: return "Not Started"

        val pitData = Json.decodeFromString<List<Field>>(rawPitData)

        val isIncomplete = pitData.any { field ->
            field.fieldContent.isBlank() || field.fieldContent == "[]" || field.fieldContent == "null"
        }

        return if (isIncomplete) "Incomplete" else "Complete"
    }

    suspend fun getCurrentTeam(): String {
        val event = eventsRepository.getEventByName(settings.value?.currentEvent ?: "")
        val jsonString = event.first()?.matchInfo
        val matches = Json.decodeFromString<List<TBAMatch>>(jsonString ?: "")

        val currentAlliance = this.alliance ?: return "ERROR: COULD NOT FIND TEAM"
        val currentRobotNumber = this.robotNumber ?: return "ERROR: COULD NOT FIND TEAM"

        for (match in matches) {
            val isPlayoff = match.compLevel != "qm"

            // check if the match is the one we're looking for: type AND number
            if (isPlayoff == (settings.value?.playoffsActive ?: false) && match.matchNumber.toString() == matchNumber.value) {
                if (currentAlliance == "Red") {
                    return match.alliances.red.teamKeys[currentRobotNumber - 1].substring(3)
                }
                if (currentAlliance == "Blue") {
                    return match.alliances.blue.teamKeys[currentRobotNumber - 1].substring(3)
                }
            }
        }
        return "ERROR: COULD NOT FIND TEAM"
    }

    fun savePitData(team: String) {
        scoutedDataRepository.insertPitData(
            ScoutedData(
                id = 0,
                team = team,
                matchNumber = "0",
                formName = "OKC Pit Form 2026",
                formType = "pit",
                scoutedData = Json.encodeToString(pitJson.value)
            ),
            team = team
        )
    }

    suspend fun loadPreviousPitData() {
        val oldData = scoutedDataRepository.getPitData(selectedTeam.value, "OKC Pit Form 2026").first()?.scoutedData
        if (oldData != null) {
            changePitJson(newPitJson = Json.decodeFromString(oldData))
        }
    }

    fun saveMatchData() {
        scoutedDataRepository.insertMatchData(
            ScoutedData(
                id = 0,
                team = selectedTeam.value,
                matchNumber = matchNumber.value,
                formName = "OKC Match Form 2026",
                formType = "match",
                scoutedData = listOf(
                    Json.encodeToString(matchAutoJson.value),
                    Json.encodeToString(matchTeleopJson.value),
                    Json.encodeToString(matchEndgameJson.value)
                ).toString()
            )
        )
    }

    // STUFF ADDED BELOW THIS LINE MUST BE MOVED TO A DEDICATED SYNC VIEWMODEL BEFORE GKC

    suspend fun getPitDataForExport(): String {
        val eventName = settings.value?.currentEvent ?: ""
        val teamList = eventsRepository.getEventByName(eventName).first()?.eventTeamList ?: emptyList()

        val pitDataList: List<Pair<String, String>> = teamList.map { team ->
            val pitData = scoutedDataRepository.getPitData(team, "OKC Pit Form 2026").first()
            val teamName = pitData?.team ?: ""
            val scoutedData = pitData?.scoutedData ?: ""
            teamName to scoutedData
        }

        return pitDataList.joinToString("\n") { (team, data) ->
            "$team,$data"
        }
    }

    suspend fun getMatchDataForExport(): String {
        val matchData = scoutedDataRepository.getMatchData("OKC Match Form 2026").first()

        return matchData.joinToString("\n") { data ->
            "${data.team},${data.matchNumber},${data.scoutedData}"
        }
    }

    fun importPitData(importedDataString: String) {
        val eventName = settings.value?.currentEvent ?: ""
        val lines = importedDataString.lines().filter { it.isNotBlank() }

        lines.forEach { line ->
            val firstCommaIndex = line.indexOf(",")

            if (firstCommaIndex > 0) {
                val team = line.substring(0, firstCommaIndex).trim()
                val scoutedData = line.substring(firstCommaIndex + 1).trim()

                if (team.isNotBlank()) {
                    scoutedDataRepository.insertPitData(
                        scoutedData = ScoutedData(id = 0, team = team, matchNumber = "0", formName = "OKC Pit Form 2026", formType = "pit", scoutedData),
                        team = team
                    )
                }
            }
        }
    }

    suspend fun importMatchData(importedDataString: String) {
        val lines = importedDataString.lines().filter { it.isNotBlank() }

        lines.forEach { line ->
            val firstCommaIndex = line.indexOf(",")
            val secondCommaIndex = if (firstCommaIndex != -1) line.indexOf(",", firstCommaIndex + 1) else -1

            if (firstCommaIndex != -1 && secondCommaIndex != -1) {
                val team = line.substring(0, firstCommaIndex).trim()
                val matchNumber = line.substring(firstCommaIndex + 1, secondCommaIndex).trim()
                val scoutedData = line.substring(secondCommaIndex + 1).trim()

                if (team.isNotBlank() && matchNumber.isNotBlank()) {
                    if (scoutedDataRepository.getMatchByTeamAndNumber(team, matchNumber.toInt(), "OKC Match Form 2026").first() == null) {
                        scoutedDataRepository.insertMatchData(
                            ScoutedData(
                                id = 0,
                                team = team,
                                matchNumber = matchNumber,
                                formName = "OKC Match Form 2026",
                                formType = "match",
                                scoutedData = scoutedData
                            )
                        )
                    } else {
                        println("Match data already exists for team $team and match number $matchNumber, so not submitted!")
                    }
                }
            }
        }
    }
}