package com.feed_me_app.ui.components


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.feed_me_app.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color(0xFFF5F5F5),
        contentColor = Color(0xFF6B4FA3)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6B4FA3),
                unselectedIconColor = Color.Gray,
                indicatorColor = Color(0xFFE8DEF8)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            selected = false,
            onClick = { /* Filtrar favoritos en Home */ },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6B4FA3),
                unselectedIconColor = Color.Gray,
                indicatorColor = Color(0xFFE8DEF8)
            )
        )

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddPet.route) },
            containerColor = Color(0xFFFF9E9E),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Añadir",
                tint = Color.White
            )
        }

        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            selected = currentRoute == Screen.Notifications.route,
            onClick = { navController.navigate(Screen.Notifications.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6B4FA3),
                unselectedIconColor = Color.Gray,
                indicatorColor = Color(0xFFE8DEF8)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            selected = currentRoute == Screen.Settings.route,
            onClick = { navController.navigate(Screen.Settings.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6B4FA3),
                unselectedIconColor = Color.Gray,
                indicatorColor = Color(0xFFE8DEF8)
            )
        )
    }
}