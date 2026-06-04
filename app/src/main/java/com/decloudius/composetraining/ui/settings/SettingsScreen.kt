package com.decloudius.composetraining.ui.settings

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decloudius.composetraining.R

/**
 * SettingsScreen lets the user change:
 * 1. Theme (Light / Dark / System Default)
 * 2. Language (English / Indonesian)
 * 3. Change PIN
 *
 * FilterChip is a nice Material3 component: it looks like a toggleable pill button.
 * The selected chip gets highlighted automatically.
 */
@Composable
fun SettingsScreen(
    themeMode: String,
    onThemeChange: (String) -> Unit,
    languageCode: String,
    onLanguageChange: (String) -> Unit,
    onChangePin: (Int) -> Unit,
    onResetPin: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- THEME SECTION ---
        Text(
            text = stringResource(R.string.theme), style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWrapped {
            ThemeChip(
                label = stringResource(R.string.light),
                selected = themeMode == "light",
                onClick = { onThemeChange("light") })
            ThemeChip(
                label = stringResource(R.string.dark),
                selected = themeMode == "dark",
                onClick = { onThemeChange("dark") })
            ThemeChip(
                label = stringResource(R.string.system_default),
                selected = themeMode == "system",
                onClick = { onThemeChange("system") })
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- LANGUAGE SECTION ---
        Text(
            text = stringResource(R.string.language), style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWrapped {
            ThemeChip(
                label = stringResource(R.string.english),
                selected = languageCode == "en",
                onClick = { onLanguageChange("en") })
            ThemeChip(
                label = stringResource(R.string.indonesian),
                selected = languageCode == "in",
                onClick = { onLanguageChange("in") })
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- CHANGE PIN SECTION ---
        Text(
            text = stringResource(R.string.change_pin), style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        // mutableStateOf is a Compose function that creates a state variable.
        // It's like a variable that can change, but Compose knows about it.

        // remember is a Compose function that remembers the value of a variable.
        var pinText by remember { mutableStateOf("") }

        LimitedOutlinedTextField(
            value = pinText,
            onValueChange = { pinText = it },
            maxChar = 6
        )

        Spacer(modifier = Modifier.height(8.dp))
        RowWrapped {
            ThemeChip(
                label = stringResource(R.string.change_pin),
                selected = false,
                onClick = {
                    onChangePin(pinText.toIntOrNull() ?: 0)
                    Toast.makeText(context, "PIN changed!", Toast.LENGTH_SHORT).show()
                })
            ThemeChip(
                label = stringResource(R.string.reset_pin),
                selected = false,
                onClick = {
                    onResetPin()
                    Toast.makeText(context, "PIN reset!", Toast.LENGTH_SHORT).show()
                })
        }
    }
}

/**
 * A tiny helper chip used for both theme and language toggles.
 * onClick is for handling the user's selection.
 * the "()" is for the label of the chip.
 * Unit is the return type of the onClick lambda, which does not return anything.
 * 
 * this means that when the user clicks on the chip, the onClick lambda will be called.
 */

@Composable
private fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected, onClick = onClick, label = { Text(label) })
}

/**
 * A simple custom layout that wraps chips horizontally and wraps to next line if needed.
 * FlowRow is part of Compose Foundation Layout (available since 1.4+).
 * We mark it as @OptIn because in some versions it is still experimental.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RowWrapped(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
private fun LimitedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxChar: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            if (newText.length <= maxChar) {
                onValueChange(newText)
            }
        },
        label = { Text(stringResource(R.string.change_pin)) },
        supportingText = {
            Text(
                text = "${value.length} / $maxChar",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = Modifier.fillMaxWidth()
    )
}
