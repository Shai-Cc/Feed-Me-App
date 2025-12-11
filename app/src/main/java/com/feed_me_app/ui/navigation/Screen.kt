package com.feed_me_app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object PetDetail : Screen("pet_detail/{petId}") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }
    object AddPet : Screen("add_pet")
    object EditPet : Screen("edit_pet/{petId}") {
        fun createRoute(petId: Int) = "edit_pet/$petId"
    }
    object AddFeeder : Screen("add_feeder/{petId}") {
        fun createRoute(petId: Int) = "add_feeder/$petId"
    }
    object Settings : Screen("settings")
    object Notifications : Screen("notifications")
}