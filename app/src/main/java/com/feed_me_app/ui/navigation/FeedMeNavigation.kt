package com.feed_me_app.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feed_me_app.ui.screens.*
import com.feed_me_app.ui.viewmodels.AuthViewModel
import com.feed_me_app.ui.viewmodels.FeederViewModel
import com.feed_me_app.ui.viewmodels.NotificationViewModel
import com.feed_me_app.ui.viewmodels.PetViewModel

@Composable
fun FeedMeNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    petViewModel: PetViewModel,
    feederViewModel: FeederViewModel,
    notificationViewModel: NotificationViewModel
) {
    val userState by authViewModel.userState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (userState.isLoggedIn) Screen.Home.route else Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { email ->
                    authViewModel.login(email)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                petViewModel = petViewModel,
                feederViewModel = feederViewModel,
                userName = userState.userName
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            PetDetailScreen(
                petId = petId,
                navController = navController,
                petViewModel = petViewModel,
                feederViewModel = feederViewModel,
                notificationViewModel = notificationViewModel
            )
        }

        composable(Screen.AddPet.route) {
            AddEditPetScreen(
                navController = navController,
                petViewModel = petViewModel,
                petId = null
            )
        }

        composable(
            route = Screen.EditPet.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            AddEditPetScreen(
                navController = navController,
                petViewModel = petViewModel,
                petId = petId
            )
        }

        composable(
            route = Screen.AddFeeder.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
            AddFeederScreen(
                petId = petId,
                navController = navController,
                feederViewModel = feederViewModel,
                petViewModel = petViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel,
                userName = userState.userName
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }
    }
}