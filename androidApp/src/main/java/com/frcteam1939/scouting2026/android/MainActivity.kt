package com.frcteam1939.scouting2026.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.frcteam1939.scouting2026.Greeting
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingView(Greeting().greet())
                    Column{
                        ButtonCounter()
                        ButtonCounter()
                        ButtonCounter()
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonCounter() {
    TextButton(
        onClick = {},
        modifier = Modifier
            .padding(25.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            Text(text = "small", style = MaterialTheme.typography.bodySmall)
            Text(text = "medium", style = MaterialTheme.typography.bodyMedium)
            Text(text = "large", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
