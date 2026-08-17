package com.yvl.news.presentation.navigation

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yvl.news.presentation.screen.subscriptions.SubscriptionsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {

        }
    )
    NavHost(
        navController = navController,
        startDestination = Screen.Subscription.route
    ) {
        composable(Screen.Subscription.route) {
            SubscriptionsScreen(
                onNavigateToSetting = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object Subscription: Screen("subscription")

    data object Settings: Screen("settings")
}