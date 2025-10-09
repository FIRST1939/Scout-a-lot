package com.frcteam1939.scouting2026.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.frcteam1939.scouting2026.Greeting
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                        {
                        SettingsButton(
                            text = "settings",
                            modifier = Modifier.align(Alignment.End)
                        )
                        GreetingView(Greeting().greet())
                        Button2(text = "SCOUNt")
                        HomeColumn()
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsButton(
    text: String,
    modifier: Modifier,
    ) {
    TextButton(
        onClick = {},
        modifier = modifier
            .padding(vertical = 30.dp, horizontal = 10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.secondary,
            ),
        shape = MaterialTheme.shapes.small
        ) {
            Text("settings", style = MaterialTheme.typography.bodyLarge)
        }
}
@Composable
fun HomeColumn() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonCounter(text = "+ create")
        ButtonCounter(text = "select")
        Text(text = "Selected: sigmaboi2026")
        ButtonCounter(text = "saved")

    }
}

@Composable
fun Button2(text: String) {
    TextButton(
        onClick = {},
        modifier = Modifier
            .padding(all = 100.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ButtonCounter(text: String) {
    TextButton(
        onClick = {},
        modifier = Modifier
            .padding(all = 10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun GreetingView(
    text: String
) {
    Text(
        text = "Match Scouting Collector Creator",
        modifier = Modifier.padding(all = 25.dp),
        style = MaterialTheme.typography.bodyLarge,
    )

}


