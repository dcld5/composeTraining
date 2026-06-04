package com.decloudius.composetraining.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.decloudius.composetraining.R
import java.io.File

/**
 * HomeScreen is the first tab.
 * It shows a welcome headline and a scrollable list of all photos taken so far.
 *
 * LazyColumn is Compose's version of RecyclerView: it only draws items
 * that are visible on screen, making it super efficient for long lists.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    // Collect the photos list. Every time a new photo is saved in the database,
    // this State automatically updates and the UI recomposes.
    val photos by viewModel.photos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome headline at the top.
        Text(
            text = stringResource(R.string.welcome),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.my_photos),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (photos.isEmpty()) {
            // If there are no photos yet, show a friendly empty state.
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_photos_yet),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // LazyColumn is like a vertical RecyclerView but much simpler.
            // We pass a key so Compose knows which item is which when the list changes.
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(photos, key = { it.id }) { photo ->
                    PhotoCard(photoFilePath = photo.filePath)
                }
            }
        }
    }
}

/**
 * PhotoCard displays a single image from internal storage.
 * Coil's AsyncImage loads the file automatically and caches it in memory.
 */
@Composable
private fun PhotoCard(photoFilePath: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = File(photoFilePath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )
    }
}
