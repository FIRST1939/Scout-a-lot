package com.frcteam1939.scouting2026.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color,
    contentColor: Color
    ) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

object TagsAndWarnings {
    @Composable
    fun RobotAssignmentTag(
        deviceRobotAssignment: String?
    ) {
        var robotAssignmentPillColor = MaterialTheme.colorScheme.surfaceVariant
        var robotAssignmentPillContentColor = MaterialTheme.colorScheme.outline
        when (deviceRobotAssignment?.dropLast(2)) {
            "Red" -> {
                robotAssignmentPillColor = MaterialTheme.scoutColors.allianceRed
                robotAssignmentPillContentColor = MaterialTheme.scoutColors.onAllianceRed
            }
            "Blue" -> {
                robotAssignmentPillColor = MaterialTheme.scoutColors.allianceBlue
                robotAssignmentPillContentColor = MaterialTheme.scoutColors.onAllianceBlue
            }
        }

        Tag(
            text = deviceRobotAssignment ?: "Unassigned",
            color = robotAssignmentPillColor,
            contentColor = robotAssignmentPillContentColor
        )
    }
}