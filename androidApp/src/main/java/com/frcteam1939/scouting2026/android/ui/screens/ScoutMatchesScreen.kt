package com.frcteam1939.scouting2026.android.ui.screens

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.android.ui.navigation.AppRoutes
import com.frcteam1939.scouting2026.android.ui.UserInputs
import com.frcteam1939.scouting2026.android.utils.getTeamAvatar
import com.frcteam1939.scouting2026.android.utils.getTeamImage
import com.frcteam1939.scouting2026.feature.events.getTeamNameByNumber
import com.frcteam1939.scouting2026.feature.scouting.ScoutingViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlin.text.ifEmpty

@Composable
// This is the first scouting screen
fun ScoutMatchesScreen(navController: NavController, scoutingViewModel: ScoutingViewModel) {
    val settingsState by scoutingViewModel.settings.collectAsState()
    val currentSettings = settingsState ?: return
    val currentEvent = currentSettings.currentEvent
    val matchNumber = scoutingViewModel.matchNumber
    var currentForm by remember { mutableStateOf("") }

    LaunchedEffect(currentEvent) {
        if (currentEvent.isNotEmpty()) {
            currentForm = scoutingViewModel.getCurrentForm(currentEvent, formType = "match")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        // for silly bunny spin
        val mutableInteractionSource = remember { MutableInteractionSource() }
        val isPressed by mutableInteractionSource.collectIsPressedAsState()
        val flipCount = remember { mutableIntStateOf(0) }
        val isBunnyAngry = flipCount.intValue >= 4

        LaunchedEffect(isPressed) {
            if (isPressed && !isBunnyAngry && (1..20).random() == 1) {
                flipCount.intValue++
                when (flipCount.intValue) {
                    1 -> makeText(context, "🐰Hey!", Toast.LENGTH_SHORT).show()
                    2 -> makeText(context, "🐰Don't do that!", Toast.LENGTH_SHORT).show()
                    3 -> makeText(context, "🐰I'm warning you...", Toast.LENGTH_SHORT).show()
                    4 -> makeText(context, "👿BARK! BINT! EVIL EVIL BINT!👿", Toast.LENGTH_SHORT).show()
                }
            }
            if (isPressed && (flipCount.intValue >= 4)) {
                val intimidatingMessage = (1..20).random()
                when (intimidatingMessage) {
                    1 -> makeText(context, "👿NO MORE!👿", Toast.LENGTH_SHORT).show()
                    2 -> makeText(context, "👿IT'S OVER!👿", Toast.LENGTH_SHORT).show()
                    3 -> makeText(context, "👿YOU WENT TOO FAR!👿", Toast.LENGTH_SHORT).show()
                    4 -> makeText(context, "👿JUST GET BACK TO SCOUTING!👿", Toast.LENGTH_SHORT).show()
                    5 -> makeText(context, "👿DON'T YOU HAVE SCOUTING TO DO???👿", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Text(
            text = "Current Event: ${currentEvent.ifEmpty { "None Selected" }}",
            modifier = Modifier.padding(all = 8.dp)
        )
        Text(
            text = "Current Form: ${currentForm.ifEmpty { "None Selected" }}",
            modifier = Modifier.padding(all = 8.dp)
        )

        // logic for silly bunny movement
        val targetOffsetY = if (isPressed && !isBunnyAngry) (-24).dp else 0.dp
        val bunnyOffsetY by animateDpAsState(
            targetValue = targetOffsetY,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
            ),
            label = "bunny hop"
        )

        val rotation by animateFloatAsState(
            targetValue = flipCount.intValue * 360f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            label = "backflip"
        )

        if (flipCount.intValue >= 4) {
            Text("👿 Don't play with me. 👿", color = MaterialTheme.colorScheme.tertiary)
        }

        Image(
            painter = painterResource(id = R.drawable.bunny),
            contentDescription = "bunny",
            colorFilter = if (isBunnyAngry) ColorFilter.tint(Color.Red, BlendMode.SrcIn) else null,
            modifier = Modifier
                .offset(y = bunnyOffsetY)
                .graphicsLayer(rotationZ = rotation)
                .clickable(
                    interactionSource = mutableInteractionSource,
                    indication = null
                ) { }
        )
        UserInputs.TextButtonInput(
            onClick = {
                // Check if scouting is allowed to happen
                var canStartScouting = true
                if (currentEvent.isEmpty()) {
                    canStartScouting = false
                    makeText(context, "Please select an event to start scouting!", Toast.LENGTH_SHORT).show()
                } else if (currentForm.isEmpty()) {
                    // canStartScouting = false
                    makeText(context, "Please select a match form to start scouting!", Toast.LENGTH_SHORT).show()
                } else if (matchNumber.value.toIntOrNull() == null) {
                    canStartScouting = false
                    makeText(context, "Please enter a valid match number!", Toast.LENGTH_SHORT).show()
                }

                if (canStartScouting) {
                    navController.navigate(AppRoutes.MatchScouting.TEAM_CONFIRMATION)
                    scoutingViewModel.resetFormJsons()
                }
            },
            text = "Scout Match",
        )

        //input for match selector
        UserInputs.TextboxInput(
            value = matchNumber.collectAsState().value,
            onValueChange = { scoutingViewModel.changeMatchNumber(it) },
            label = "Match Number",
            singleLine = true,
            forceNumberInput = true,
            characterLimit = 3
        )
    }
}


@Composable
fun TeamConfirmationScreen(navController: NavController, scoutingViewModel: ScoutingViewModel) {
    val matchNumber by scoutingViewModel.matchNumber.collectAsState()
    var team by remember { mutableStateOf("") }
    var teamName by remember { mutableStateOf("") }

    LaunchedEffect(matchNumber) {
        team = scoutingViewModel.getCurrentTeam()
        teamName = getTeamNameByNumber(team)
    }

    var noShowPopup by remember { mutableStateOf(false) }

    if (noShowPopup) {
        AlertDialog(
            onDismissRequest = { noShowPopup = false },
            title = {
                Text(
                    "Why did the robot not show up?",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            containerColor = MaterialTheme.colorScheme.primary,

            // Robot no-showed
            confirmButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Robot no-show",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },

            // Robot not part of this match
            dismissButton = {
                UserInputs.TextButtonInput(
                    onClick = {
                        noShowPopup = false
                        navController.navigate(AppRoutes.MatchScouting.SCOUT_MATCHES)
                    },
                    text = "Team not in match",
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },
        )
    }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberDrawablePainter(getTeamImage(team)),
            contentDescription = "robot image",
            modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 25.dp, top = 50.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = getTeamAvatar(team),
                contentDescription = "Team Avatar",
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "$team - $teamName",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(all = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(text = "Can you locate this robot on the field?")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            // Yes button
            UserInputs.TextButtonInput(
                onClick = {
                    scoutingViewModel.changeSelectedTeam(team)
                    navController.navigate(AppRoutes.MatchScouting.SCOUT_AUTO)
                    scoutingViewModel.changeCurrentPage(newCurrentPage = "Auto")
                },
                text = "Yes",
                modifier = Modifier.padding(all = 25.dp),
                containerColor = MaterialTheme.colorScheme.primary
            )

            // No button
            UserInputs.TextButtonInput(
                onClick = { noShowPopup = true },
                text = "No",
                modifier = Modifier.padding(all = 25.dp),
            )
        }
    }
}
