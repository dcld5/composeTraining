package com.decloudius.composetraining.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.decloudius.composetraining.R
import java.io.File

/**
 * ProfileScreen lets the user edit their display name and profile picture.
 * The image is picked from the gallery, copied into the app's private storage,
 * and the path is saved in Room via the ViewModel.
 */
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val name by viewModel.name.collectAsState()
    val imagePath by viewModel.imagePath.collectAsState()
    val saved by viewModel.saved.collectAsState()
    val context = LocalContext.current

    // Launcher that opens the system photo picker (gallery).
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Copy the picked image into our app's private files so it survives reboots.
            val input = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")
            input?.use { inp ->
                file.outputStream().use { out ->
                    inp.copyTo(out)
                }
            }
            viewModel.onImagePicked(file.absolutePath)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile image (circle) or placeholder.
        if (imagePath != null) {
            AsyncImage(
                model = File(imagePath!!),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to pick an image from the gallery.
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text(stringResource(R.string.pick_image))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Name text field.
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::onNameChange,
            label = { Text(stringResource(R.string.name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save button.
        Button(
            onClick = viewModel::saveProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }

        // Little confirmation text after saving.
        if (saved) {
            Toast.makeText(context,
                "Saved!",
                Toast.LENGTH_SHORT).show()
        }
    }
}
