package com.jenugumpu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jenugumpu.ui.screens.BatchListScreen
import com.jenugumpu.ui.screens.DashboardScreen
import com.jenugumpu.ui.screens.HarvestScreen
import com.jenugumpu.ui.screens.ProfileScreen
import com.jenugumpu.ui.screens.ProfitScreen
import com.jenugumpu.ui.screens.SettingsScreen
import com.jenugumpu.viewmodel.SettingsViewModel

private enum class Dest(val route: String, val label: String) {
    Dashboard("dashboard", "Home"),
    Harvest("harvest", "Harvest"),
    Batches("batches", "Batches"),
    Profit("profit", "Profit"),
    Profile("profile", "Profile"),
    Settings("settings", "Settings")
}

@Composable
fun AppNavigation(settingsVm: SettingsViewModel, onToggleLocale: (Boolean) -> Unit = {}) {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()
    val tabs = Dest.entries

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { d ->
                    NavigationBarItem(
                        selected = current?.destination?.route == d.route,
                        onClick = { nav.navigate(d.route) { launchSingleTop = true } },
                        icon = {
                            Icon(
                                imageVector = when (d) {
                                    Dest.Dashboard -> Icons.Default.Home
                                    Dest.Harvest   -> Icons.Default.Add
                                    Dest.Batches   -> Icons.Default.List
                                    Dest.Profit    -> Icons.Default.Star
                                    Dest.Profile   -> Icons.Default.AccountCircle
                                    Dest.Settings  -> Icons.Default.Settings
                                },
                                contentDescription = d.label
                            )
                        },
                        label = { Text(d.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = Dest.Dashboard.route) {
            composable(Dest.Dashboard.route) { DashboardScreen(hiltViewModel(), padding) }
            composable(Dest.Harvest.route)   { HarvestScreen(hiltViewModel()) }
            composable(Dest.Batches.route)   { BatchListScreen(hiltViewModel()) }
            composable(Dest.Profit.route)    { ProfitScreen(hiltViewModel()) }
            composable(Dest.Profile.route)   { ProfileScreen(settingsVm) }
            composable(Dest.Settings.route)  { SettingsScreen(settingsVm, onToggleLocale) }
        }
    }
}
