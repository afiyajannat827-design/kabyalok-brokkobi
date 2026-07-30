package com.example.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.about.AboutScreen
import com.example.ui.backup.BackupRestoreScreen
import com.example.ui.editor.EditorScreen
import com.example.ui.groups.GroupsScreen
import com.example.ui.hidden.HiddenNotesScreen
import com.example.ui.home.HomeScreen
import com.example.ui.home.HomeViewModel
import com.example.ui.pinned.PinnedNotesScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.splash.SplashScreen
import com.example.ui.theme.BrokkobiTheme
import com.example.ui.theme.ThemeScreen
import com.example.ui.trash.TrashScreen

@Composable
fun AppNavGraph(
    homeViewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentThemePreset by homeViewModel.currentThemePreset.collectAsState()
    val isDarkModePref by homeViewModel.isDarkMode.collectAsState()

    BrokkobiTheme(
        themePreset = currentThemePreset
    ) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    viewModel = homeViewModel,
                    onCreateNewClick = {
                        navController.navigate("editor/-1")
                    },
                    onNoteClick = { noteId ->
                        navController.navigate("editor/$noteId")
                    },
                    onNavigatePlaceholder = { route ->
                        if (route == "pdf") {
                            homeViewModel.exportAllPdf(context)
                        } else {
                            navController.navigate(route)
                        }
                    }
                )
            }

            composable(
                route = "editor/{noteId}",
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                EditorScreen(
                    noteId = noteId,
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("groups") {
                GroupsScreen(
                    viewModel = homeViewModel,
                    onNoteClick = { noteId ->
                        navController.navigate("editor/$noteId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("pinned") {
                PinnedNotesScreen(
                    viewModel = homeViewModel,
                    onNoteClick = { noteId ->
                        navController.navigate("editor/$noteId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("hidden") {
                HiddenNotesScreen(
                    viewModel = homeViewModel,
                    onNoteClick = { noteId ->
                        navController.navigate("editor/$noteId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("trash") {
                TrashScreen(
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("backup") {
                BackupRestoreScreen(
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("theme") {
                ThemeScreen(
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = homeViewModel,
                    onNavigateToTheme = { navController.navigate("theme") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("about") {
                AboutScreen(
                    viewModel = homeViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
