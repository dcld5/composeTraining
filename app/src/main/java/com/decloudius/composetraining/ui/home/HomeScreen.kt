package com.decloudius.composetraining.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.decloudius.composetraining.R
import com.decloudius.composetraining.domain.model.Photo
import java.io.File

/**
 * HomeScreen is the first tab.
 * It shows a welcome headline and a scrollable grid of all photos taken so far.
 *
 * LazyVerticalGrid displays items in a 2-column grid, similar to a RecyclerView
 * with GridLayoutManager but much simpler in Compose.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    // Collect the photos list. Every time a new photo is saved in the database,
    // this State automatically updates and the UI recomposes.
    val photos by viewModel.photos.collectAsState()

    // Holds the photo the user tapped to review. When non-null the review dialog shows.
    var selectedPhoto by remember { mutableStateOf<Photo?>(null) }

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
            // LazyVerticalGrid is like a grid RecyclerView but much simpler.
            // We pass a key so Compose knows which item is which when the list changes.
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(photos, key = { it.id }) { photo ->
                    PhotoCard(
                        photo = photo,
                        onClick = { selectedPhoto = photo }
                    )
                }
            }
        }
    }

    // Review dialog — shows when the user taps a grid item.
    // It uses the same AlertDialog pattern as CameraDialog for consistency.
    selectedPhoto?.let { photo ->
        AlertDialog(
            onDismissRequest = { selectedPhoto = null },
            title = {
                Text(text = stringResource(R.string.photo_preview))
            },
            text = {
                AsyncImage(
                    model = File(photo.filePath),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            },
            confirmButton = {
                // selectedPhoto is null when the user taps Save because the dialog is dismissed.
                TextButton(onClick = { selectedPhoto = null }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePhoto(photo)
                        selectedPhoto = null
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        )
    }
}

/**
 * PhotoCard displays a single image from internal storage.
 * It is clickable to open the review dialog.
 * Coil's AsyncImage loads the file automatically and caches it in memory.
 */
@Composable
private fun PhotoCard(photo: Photo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = File(photo.filePath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )
    }
}
