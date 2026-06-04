package com.decloudius.composetraining.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decloudius.composetraining.R

/**
 * CameraDialog pops up after the user takes a photo.
 * It shows the Bitmap preview and two actions: Save or Cancel.
 *
 * AlertDialog is a high-level Material3 component that follows platform guidelines
 * (title, content area, confirm/dismiss buttons) without you having to build it from scratch.
 */

@Composable
fun CameraDialog(
    bitmap: Bitmap,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        // Called when the user taps outside the dialog or presses the back button.
        onDismissRequest = onCancel,
        title = {
            Text(text = stringResource(R.string.photo_preview))
        },
        text = {
            // Convert Android Bitmap to Compose ImageBitmap so it can be drawn.
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp)
            )
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
