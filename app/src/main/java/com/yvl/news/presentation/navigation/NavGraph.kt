package com.yvl.news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yvl.news.presentation.screen.subscriptions.SubscriptionsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Subscription.route
    ) {
        composable(Screen.Subscription.route) {
            SubscriptionsScreen(
                onNavigateToSetting = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object Subscription: Screen("subscription")

    data object Settings: Screen("settings")
}