package com.feed_me_app.ui.screens


import com.feed_me_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8DEF8)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val imageSize = screenWidth / 3
            val circleSize = imageSize + 50.dp
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(Color(0xFF6B4FA3)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(imageSize + 25.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Feed Me",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B4E)
            )

            Text(
                text = "App 🧡",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B4E)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Bienvenido con tu mascota...",
                fontSize = 14.sp,
                color = Color(0xFF6B4FA3)
            )
        }
    }
}