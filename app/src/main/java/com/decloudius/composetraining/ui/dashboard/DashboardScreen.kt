package com.decloudius.composetraining.ui.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decloudius.composetraining.R
import com.decloudius.composetraining.ui.components.CameraDialog
import com.decloudius.composetraining.ui.home.HomeScreen
import com.decloudius.composetraining.ui.home.HomeViewModel
import com.decloudius.composetraining.ui.profile.ProfileScreen
import com.decloudius.composetraining.ui.profile.ProfileViewModel
import com.decloudius.composetraining.ui.settings.SettingsScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * DashboardScreen is the main container for the three tabs.
 *
 * KEY COMPOSE CONCEPTS SHOWN HERE:
 * - Scaffold: gives you slots for topBar, bottomBar, FAB, and main content with safe padding.
 * - HorizontalPager: swipe left/right to switch pages; we sync it with the bottom nav.
 * - NavigationBar (BottomNavigation in old Material): clickable tabs at the bottom.
 * - rememberLauncherForActivityResult: Jetpack Compose way to start an external Intent
 *   (here the system camera) and receive a result inside a Composable.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel,
    onLogout: () -> Unit
) {
    // Pager state remembers which page is currently visible (0, 1, or 2).
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    // Observe the captured photo preview. If non-null, we show the save/cancel dialog.
    val previewBitmap by dashboardViewModel.previewBitmap.collectAsState()

    // Observe theme and language so we can pass them down to Settings.
    val themeMode by dashboardViewModel.themeMode.collectAsState()
    val languageCode by dashboardViewModel.languageCode.collectAsState()

    // This launcher opens the system camera app and returns a small preview Bitmap.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // If the user actually took a photo, send it to the ViewModel.
        bitmap?.let { dashboardViewModel.onPhotoTaken(it) }
    }

    // We create child ViewModels here (they are scoped to the Activity).
    val homeViewModel: HomeViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()

    // Labels and icons for the bottom navigation.
    val navItems = listOf(
        Triple(stringResource(R.string.home), Icons.Default.Home, 0),
        Triple(stringResource(R.string.profile), Icons.Default.Person, 1),
        Triple(stringResource(R.string.settings), Icons.Default.Settings, 2)
    )

    Scaffold(
        // --- TOP BAR (header) ---
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    // Logout icon in the top-right corner.
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.logout)
                        )
                    }
                }
            )
        },
        // --- BOTTOM BAR (navigation) ---
        bottomBar = {
            NavigationBar {
                navItems.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        // Selected if the current pager page matches this index.
                        selected = pagerState.currentPage == index,
                        onClick = {
                            // Launch a coroutine to animate the page change.
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = { Icon(imageVector = icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        },
        // --- FLOATING ACTION BUTTON ---
        // Only show the camera FAB when the user is on the Home tab (page 0).
        floatingActionButton = {
            if (pagerState.currentPage == 0) {
                FloatingActionButton(
                    onClick = { cameraLauncher.launch(null) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.take_photo)
                    )
                }
            }
        }
    ) { innerPadding ->
        // --- MAIN CONTENT (pager) ---
        // HorizontalPager lets the user swipe between pages.
        // We sync its state with the bottom nav above.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> HomeScreen(viewModel = homeViewModel)
                1 -> ProfileScreen(viewModel = profileViewModel)
                2 -> SettingsScreen(
                    themeMode = themeMode,
                    onThemeChange = dashboardViewModel::setThemeMode,
                    languageCode = languageCode,
                    onLanguageChange = dashboardViewModel::setLanguage
                )
            }
        }

        // --- CAMERA PREVIEW DIALOG ---
        // If a photo was just taken, show it in a dialog with Save and Cancel.
        previewBitmap?.let { bitmap ->
            CameraDialog(
                bitmap = bitmap,
                onSave = dashboardViewModel::savePhoto,
                onCancel = dashboardViewModel::clearPreview
            )
        }
    }
}
