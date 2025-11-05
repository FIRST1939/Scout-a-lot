package com.frcteam1939.scouting2026.android


import com.frcteam1939.scouting2026.Greeting
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.widget.*
import android.widget.Toast.makeText
import androidx.compose.runtime.*
import kotlin.text.toIntOrNull
import androidx.compose.foundation.selection.*
object AppRoutes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val SELECT = "select"
    const val SAVED = "saved"
    const val OPERATOR = "operator"
}

@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier) {
    val radioOptions = listOf("at", "One", "Pint 😂😂✅")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
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
fun MatchSelecto2() {
    var state by remember { mutableStateOf("") }
    TextField(
        value = state,
        onValueChange = {
            if (it.length <= 3 && it.toIntOrNull() != null || it.isEmpty()) state = it
                        },
        singleLine = true,
        label = { Text("Match Number") },

    )
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
//navigation controller
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
//button that goes to operator settings
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
//operator settings menu
fun OperatorSettings(
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is for operators.. One pint! 😂😂", style= MaterialTheme.typography.headlineMedium)
        HomeColumn(navController)
        Button(onClick = {navController.popBackStack() }) {
            Text("Go Back")}
    }
}

@Composable
//goes to saved sets menu
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
//shows all created sets to manage
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
//goes to select set screen
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
//this is where you select a set that the scouter will use
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
//this is home screen
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
            GreetingView(Greeting().greet())
            Button2(text = "Start Scouting")
            MatchSelecto2()


        }
    }
}

@Composable
//main settings
fun SettingsScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style= MaterialTheme.typography.headlineMedium)
        var isChecked by remember { mutableStateOf( false ) }
        Switch(checked = isChecked, onCheckedChange = {isChecked = it})
        RadioButtonSingleSelection()
        OperatorButton(
            text = "Operator",
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(all = 30.dp),
            onClick = {navController.navigate(AppRoutes.OPERATOR)}
        )
        Button(onClick = {navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}
@Composable
//button that goes to settings
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
//this is actually the operator's column now
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
//SCOUNt BUTTON!!!! BUTTON!!!! BUTTON!!!! 😂😂 accession
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
//create a set button
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
//main text on the home screen
fun GreetingView(
    text: String
) {
    Text(
        text = "FRC Scout Maker",
        modifier = Modifier.padding(all = 25.dp),
        style = MaterialTheme.typography.bodyLarge,
    )

}


