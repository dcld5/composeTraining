package com.decloudius.composetraining.ui.settings

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decloudius.composetraining.R

@Composable
fun SettingsScreen(
    themeMode: String,
    onThemeChange: (String) -> Unit,
    languageCode: String,
    onLanguageChange: (String) -> Unit
) {
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

        Text(
            text = stringResource(R.string.theme),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWrapped {
            ThemeChip(
                label = stringResource(R.string.light),
                selected = themeMode == "light",
                onClick = { onThemeChange("light") }
            )
            ThemeChip(
                label = stringResource(R.string.dark),
                selected = themeMode == "dark",
                onClick = { onThemeChange("dark") }
            )
            ThemeChip(
                label = stringResource(R.string.system_default),
                selected = themeMode == "system",
                onClick = { onThemeChange("system") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.language),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWrapped {
            ThemeChip(
                label = stringResource(R.string.english),
                selected = languageCode == "en",
                onClick = { onLanguageChange("en") }
            )
            ThemeChip(
                label = stringResource(R.string.indonesian),
                selected = languageCode == "in",
                onClick = { onLanguageChange("in") }
            )
        }
    }
}

@Composable
private fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

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
