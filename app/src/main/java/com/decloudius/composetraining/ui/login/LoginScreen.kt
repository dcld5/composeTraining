package com.decloudius.composetraining.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
// Preview is an annotation that tells Android Studio to render this composable
// in the design/split view so you can see the UI without running the app on a device.
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decloudius.composetraining.R
import com.decloudius.composetraining.ui.theme.ComposeTrainingTheme

/**
 * LoginScreen is a Jetpack Compose function that describes WHAT the login UI looks like.
 * Think of it like a recipe: "put a top bar here, a text field there, a button below".
 * When any state inside changes (e.g., the user types), Compose automatically redraws
 * ONLY the parts that changed. This is called "recomposition".
 *
 * Scaffold is the main structural layout in Material3. It provides slots for:
 * - topBar      -> the bar at the very top
 * - bottomBar   -> bar at the bottom (not used here)
 * - floatingActionButton -> round button that floats above content (not used here)
 * - content     -> the main body (gives you padding values to avoid system bars)
 */

// ExperimentalMaterial3Api is needed for Scaffold and TopAppBar.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onBiometricClick: () -> Unit = {}
) {
    // Collect states from the ViewModel as Compose State objects.
    // Every time the ViewModel updates these values, the UI recomposes automatically.
    val pin by viewModel.pin.collectAsState()
    val isPinSet by viewModel.isPinSet.collectAsState()
    val error by viewModel.error.collectAsState()

    // Instead of drawing the UI directly, we forward everything to LoginScreenContent.
    // This keeps the "wiring" (ViewModel) separate from the "drawing" (UI),
    // which makes it possible to preview the screen without creating a real ViewModel.
    LoginScreenContent(
        pin = pin,
        isPinSet = isPinSet,
        error = error,
        onPinChange = viewModel::onPinChange,
        onLoginClick = viewModel::onLoginClick,
        onCreatePinClick = viewModel::onCreatePinClick,
        onBiometricClick = onBiometricClick
    )
}

/**
 * Stateless version of the login UI.
 * All data comes in as plain function parameters, so we can preview it
 * in Android Studio without needing a real ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    pin: String,
    isPinSet: Boolean,
    error: String?,
    onPinChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreatePinClick: () -> Unit,
    onBiometricClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            // TopAppBar is the standard Material3 header bar.
            TopAppBar(
                title = { Text(stringResource(R.string.login)) }
            )
        }
    ) { innerPadding ->
        // innerPadding is IMPORTANT: it tells us how much space the system bars/status bar take,
        // so our content doesn't get hidden behind the top bar.

        // Column is the main container for the login screen.
        Column(
            // Modifier is used to apply styles and behaviors to the Column.
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Headline text changes depending on whether the user already has a PIN.

            // Text is a Composable that displays a string.
            Text(
                text = if (isPinSet) {
                    // stringResource returns a localized string based on the app's current language.
                    stringResource(R.string.enter_pin)
                } else {
                    // "Create a 6-digit PIN" is a hardcoded string.
                    "Create a 6-digit PIN"
                },
                // Style is used to apply visual properties to the Text.
                // You can set style properties like color, fontSize,
                // fontWeight, etc. in MaterialTheme.typography.
                style = MaterialTheme.typography.headlineMedium
            )

            // Spacer is a Composable that takes up space.
            Spacer(modifier = Modifier.height(32.dp))

            /**
             * OutlinedTextField is a text box with a border.
             * - value / onValueChange = controlled input (ViewModel owns the text).
             * - keyboardType = NumberPassword shows a numeric keypad.
             * - visualTransformation = PasswordVisualTransformation hides digits with dots.
             * - singleLine = true keeps it on one row.
             * - isError = highlights the border red when error is not null.
             */
            OutlinedTextField(
                value = pin,
                onValueChange = onPinChange,
                label = { Text("PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = error != null,
                modifier = Modifier.fillMaxWidth()
            )

            // Show an error message directly below the text field if something went wrong.
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isPinSet) {
                // Returning user: show Login button.
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.login))
                }
            } else {
                // First-time user: show Save PIN button instead.
                Button(
                    onClick = onCreatePinClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save PIN")
                }
            }
        }
    }
}

/**
 * @Preview makes Android Studio render this composable in the design/split view.
 * - showBackground = true draws a light background so the UI is visible.
 * - name is the label you see in the preview panel.
 *
 * Because LoginScreenContent is stateless (it only receives plain values),
 * we can call it with fake data. The empty lambdas `{}` are stub callbacks;
 * nothing needs to happen when the user taps buttons in a preview.
 */
@Preview(showBackground = true, name = "First time - create PIN")
@Composable
fun PreviewLoginScreenFirstTime() {
    // MaterialTheme provides colors, typography, and shapes so the preview
    // looks the same as inside the real app.
    MaterialTheme {
        LoginScreenContent(
            pin = "",
            isPinSet = false,
            error = null,
            onPinChange = {},
            onLoginClick = {},
            onCreatePinClick = {}
        )
    }
}

// Another preview with different fake data so you can see the returning-user state.
@Preview(
    showBackground = true,
    name = "Returning user")
@Composable
fun PreviewLoginScreenReturningUser() {
    ComposeTrainingTheme (darkTheme = true) {
        LoginScreenContent(
            pin = "123456",
            isPinSet = true,
            error = "Wrong PIN",
            onPinChange = {},
            onLoginClick = {},
            onCreatePinClick = {}
        )
    }
}
