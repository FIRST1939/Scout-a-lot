package com.frcteam1939.scouting2026.data.demo

import com.frcteam1939.scouting2026.api.models.Alliance
import com.frcteam1939.scouting2026.api.models.Alliances
import com.frcteam1939.scouting2026.api.models.TBAMatch
import com.frcteam1939.scouting2026.data.entities.Event
import kotlinx.serialization.json.Json

/**
 * A fully synthetic event, so the app can be exercised end to end with no network
 * access and no TBA / FRC Events API keys.
 *
 * Everything is generated deterministically, which means two devices that seed the
 * demo event end up with the same teams and the same schedule. That makes it usable
 * for testing Bluetooth and file sync as well as scouting.
 */
object DemoEvent {
    const val EVENT_NAME = "Demo Event 2026"
    const val EVENT_CODE = "demo"
    const val EVENT_YEAR = 2026

    // The scouting and reporting paths still hard-code these two form names, so the
    // demo event points at them to keep the "Current Form" display honest.
    const val PIT_FORM_NAME = "OKC Pit Form 2026"
    const val MATCH_FORM_NAME = "OKC Match Form 2026"

    const val QUALS_COUNT = 40
    const val SEMIFINAL_COUNT = 13
    const val FINAL_COUNT = 3

    private const val ALLIANCE_SIZE = 3
    private const val TEAMS_PER_MATCH = ALLIANCE_SIZE * 2

    // An arbitrary early-2026 epoch, plus 7 minutes between matches.
    private const val FIRST_MATCH_TIME = 1_771_600_000
    private const val MATCH_INTERVAL_SECONDS = 420

    /**
     * Team numbers that ship with a robot photo in the androidApp assets, so the team
     * confirmation screen shows real images instead of the missing-image placeholder.
     */
    val TEAM_NUMBERS: List<String> = listOf(
        "1108", "1208", "1710", "1723", "1730", "1764", "1785", "1802", "1806", "1825",
        "1827", "1939", "1982", "1987", "1997", "2001", "2040", "2333", "2410", "2560",
        "3184", "4213", "4329", "4455", "4522", "4766", "5098", "5268", "5801", "5809",
        "5968", "6817", "7064", "8112", "8770", "9031", "9428", "9445", "9570", "11171",
        "11195"
    )

    // Names must never contain a comma: Converters stores string lists comma-joined.
    val TEAM_NAMES: List<String> = TEAM_NUMBERS.map { "Demo Team $it" }

    fun buildEvent(): Event = Event(
        id = 0,
        customEventName = EVENT_NAME,
        eventCode = EVENT_CODE,
        eventYear = EVENT_YEAR,
        matchInfo = buildMatchInfoJson(),
        eventTeamList = TEAM_NUMBERS,
        eventTeamNameList = TEAM_NAMES,
        pitForm = PIT_FORM_NAME,
        matchForm = MATCH_FORM_NAME
    )

    fun buildMatchInfoJson(): String = Json.encodeToString(buildMatches())

    /**
     * Quals plus a playoff bracket.
     *
     * Note the playoff matches are numbered sf1..sf13 / f1..f3 rather than using TBA's
     * real double-elimination keys (sf1m1, sf2m1, ...). That is deliberate: the app
     * looks up a playoff match purely by [TBAMatch.matchNumber], so this is the shape
     * it can actually consume.
     */
    fun buildMatches(): List<TBAMatch> {
        val matches = mutableListOf<TBAMatch>()

        for (matchNumber in 1..QUALS_COUNT) {
            matches += buildMatch("qm", setNumber = 1, matchNumber = matchNumber, scheduleIndex = matches.size)
        }
        for (matchNumber in 1..SEMIFINAL_COUNT) {
            matches += buildMatch("sf", setNumber = matchNumber, matchNumber = matchNumber, scheduleIndex = matches.size)
        }
        for (matchNumber in 1..FINAL_COUNT) {
            matches += buildMatch("f", setNumber = 1, matchNumber = matchNumber, scheduleIndex = matches.size)
        }

        return matches
    }

    private fun buildMatch(
        compLevel: String,
        setNumber: Int,
        matchNumber: Int,
        scheduleIndex: Int
    ): TBAMatch {
        val teams = teamsForMatch(scheduleIndex)
        val redScore = 40 + (scheduleIndex * 7) % 60
        val blueScore = 40 + (scheduleIndex * 11) % 60
        val matchTime = FIRST_MATCH_TIME + scheduleIndex * MATCH_INTERVAL_SECONDS

        return TBAMatch(
            key = "$EVENT_YEAR${EVENT_CODE}_$compLevel$matchNumber",
            compLevel = compLevel,
            setNumber = setNumber,
            matchNumber = matchNumber,
            alliances = Alliances(
                red = buildAlliance(teams.take(ALLIANCE_SIZE), redScore),
                blue = buildAlliance(teams.drop(ALLIANCE_SIZE), blueScore)
            ),
            winningAlliance = if (redScore >= blueScore) "red" else "blue",
            eventKey = "$EVENT_YEAR$EVENT_CODE",
            time = matchTime,
            predictedTime = matchTime,
            actualTime = matchTime
        )
    }

    private fun buildAlliance(teamNumbers: List<String>, score: Int) = Alliance(
        score = score,
        teamKeys = teamNumbers.map { "frc$it" },
        surrogateTeamKeys = emptyList(),
        dqTeamKeys = emptyList()
    )

    /**
     * Six distinct teams per match, walking the roster so every team plays a similar
     * number of matches. The roster size is coprime with [TEAMS_PER_MATCH], so the
     * window keeps shifting instead of repeating the same six groupings.
     */
    private fun teamsForMatch(scheduleIndex: Int): List<String> {
        val offset = (scheduleIndex * TEAMS_PER_MATCH) % TEAM_NUMBERS.size
        return List(TEAMS_PER_MATCH) { TEAM_NUMBERS[(offset + it) % TEAM_NUMBERS.size] }
    }
}
