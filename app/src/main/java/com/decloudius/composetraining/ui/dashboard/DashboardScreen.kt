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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel,
    onLogout: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val previewBitmap by dashboardViewModel.previewBitmap.collectAsState()

    val themeMode by dashboardViewModel.themeMode.collectAsState()
    val languageCode by dashboardViewModel.languageCode.collectAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        bitmap?.let { dashboardViewModel.onPhotoTaken(it) }
    }

    val homeViewModel: HomeViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()

    val navItems = listOf(
        Triple(stringResource(R.string.home), Icons.Default.Home, 0),
        Triple(stringResource(R.string.profile), Icons.Default.Person, 1),
        Triple(stringResource(R.string.settings), Icons.Default.Settings, 2)
    )

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },

        bottomBar = {
            NavigationBar {
                navItems.forEach { (label, icon, index) ->
                    NavigationBarItem(

                        selected = pagerState.currentPage == index,
                        onClick = {

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
                    onLanguageChange = dashboardViewModel::setLanguage,
                    onChangePin = dashboardViewModel::changePin,
                    onResetPin = dashboardViewModel::resetPin
                )
            }
        }

        previewBitmap?.let { bitmap ->
            CameraDialog(
                bitmap = bitmap,
                onSave = dashboardViewModel::savePhoto,
                onCancel = dashboardViewModel::clearPreview
            )
        }
    }
}
