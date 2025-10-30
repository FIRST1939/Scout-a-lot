package com.frcteam1939.scouting2026.android


import com.frcteam1939.scouting2026.Greeting
import com.frcteam1939.scouting2026.TBAAuth
import com.frcteam1939.scouting2026.CompInfo
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.runtime.*

object AppRoutes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val SELECT = "select"
    const val SAVED = "saved"
    const val OPERATOR = "operator"
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ScoutingApp()


                    }
                }
            }
        }
@Composable
fun MatchSelector(
    text: String,
    context: Context = LocalContext.current
) {
    TextButton(
        onClick = {
            makeText(context, "Not implemented 😨😨", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ScoutingApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = AppRoutes.HOME) {
            composable(AppRoutes.HOME) {
                HomeScreen(navController = navController)
            }
            composable(AppRoutes.SETTINGS) {
                SettingsScreen(navController = navController)
            }
            composable(AppRoutes.SELECT) {
                SelectScreen(navController = navController)
            }
            composable(AppRoutes.SAVED) {
                SavedScreen(navController = navController)
            }
            composable(AppRoutes.OPERATOR) {
                OperatorSettings(navController = navController)
            }
        }
    }
}
@Composable
fun OperatorButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
fun OperatorSettings(
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeColumn(navController)
        Text("This is for operators.. One pint! 😂😂", style= MaterialTheme.typography.headlineMedium)
        Button(onClick = {navController.popBackStack() }) {
            Text("Go Back")}
    }
}

@Composable
fun SavedButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SavedScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You can see and manage saved ones . . at one pint", style= MaterialTheme.typography.headlineMedium)
        Button(onClick = {navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}
@Composable
fun SelectButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
        ) {
        TextButton(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary,
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }

@Composable
fun SelectScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You will select here. . at one pint")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}
@Composable
fun HomeScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        SettingsButton(
            text = "Settings",
            modifier = Modifier.align(Alignment.End),
            onClick = {
                navController.navigate(AppRoutes.SETTINGS)
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var text by remember { mutableStateOf("Loading") }
            val context = LocalContext.current

            LaunchedEffect(true) {
                text = try {
                    val tbaAuth = TBAAuth(context)
                    CompInfo(tbaAuth).pullCompInfo()
                } catch (e: Exception) {
                    e.message ?: "error"
                }
            }

            GreetingView(text)
            Button2(text = "Start Scouting")
            MatchSelector(
                text = "Match Selector")


        }
    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style= MaterialTheme.typography.headlineMedium)
        OperatorButton(
            text = "Operator",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {navController.navigate(AppRoutes.OPERATOR)}
        )
        Button(onClick = {navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}
@Composable
fun SettingsButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
    ) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
fun HomeColumn(navController: NavController
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonCounter(text = "Create a Set")
        SelectButton(
            onClick = {navController.navigate(AppRoutes.SELECT)},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Select Set"
        )
        Text(text = "Selected: sigmaboi2026 (match scout)")
        SavedButton(
            onClick = {navController.navigate(AppRoutes.SAVED)},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Saved Sets"
        )

    }
}

@Composable
fun Button2(text: String) {
    TextButton(
        onClick = {},
        modifier = Modifier
            .padding(all = 75.dp),
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
        text = text,
        modifier = Modifier.padding(all = 25.dp),
        style = MaterialTheme.typography.bodyLarge,
    )

}


