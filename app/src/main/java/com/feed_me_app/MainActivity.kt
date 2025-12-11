package com.feed_me_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.feed_me_app.data.local.FeedMeDatabase
import com.feed_me_app.data.repository.ESP32Simulator
import com.feed_me_app.data.repository.FeederRepository
import com.feed_me_app.data.repository.NotificationRepository
import com.feed_me_app.data.repository.PetRepository
import com.feed_me_app.ui.navigation.FeedMeNavigation
import com.feed_me_app.ui.theme.FeedMeAppTheme
import com.feed_me_app.ui.viewmodels.AuthViewModel
import com.feed_me_app.ui.viewmodels.FeederViewModel
import com.feed_me_app.ui.viewmodels.NotificationViewModel
import com.feed_me_app.ui.viewmodels.PetViewModel

class MainActivity : ComponentActivity() {

    private lateinit var petViewModel: PetViewModel
    private lateinit var feederViewModel: FeederViewModel
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FeedMeDatabase.getDatabase(applicationContext)
        val petRepository = PetRepository(database.petDao())
        val feederRepository = FeederRepository(database.feederDao())
        val notificationRepository = NotificationRepository(database.notificationDao())
        val esp32Simulator = ESP32Simulator()

        petViewModel = PetViewModel(petRepository)
        feederViewModel = FeederViewModel(feederRepository, esp32Simulator)
        notificationViewModel = NotificationViewModel(notificationRepository)
        authViewModel = AuthViewModel()

        setContent {
            FeedMeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FeedMeApp(
                        authViewModel = authViewModel,
                        petViewModel = petViewModel,
                        feederViewModel = feederViewModel,
                        notificationViewModel = notificationViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FeedMeApp(
    authViewModel: AuthViewModel,
    petViewModel: PetViewModel,
    feederViewModel: FeederViewModel,
    notificationViewModel: NotificationViewModel
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        notificationViewModel.addNotification(
            title = "Alimentación de Gato 1",
            message = "Gato 1 será alimentado hoy.",
            type = "feeding"
        )

        notificationViewModel.addNotification(
            title = "Veterinario de Gato 3",
            message = "Tienes una cita veterinaria para mañana",
            type = "veterinary"
        )
    }

    FeedMeNavigation(
        navController = navController,
        authViewModel = authViewModel,
        petViewModel = petViewModel,
        feederViewModel = feederViewModel,
        notificationViewModel = notificationViewModel
    )
}