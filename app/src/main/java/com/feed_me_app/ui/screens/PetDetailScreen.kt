package com.feed_me_app.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.feed_me_app.ui.navigation.Screen
import com.feed_me_app.ui.viewmodels.FeederViewModel
import com.feed_me_app.ui.viewmodels.NotificationViewModel
import com.feed_me_app.ui.viewmodels.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: Int,
    navController: NavController,
    petViewModel: PetViewModel,
    feederViewModel: FeederViewModel,
    notificationViewModel: NotificationViewModel
) {
    val pet by petViewModel.selectedPet.collectAsState()
    val feeders by feederViewModel.getFeedersByPetId(petId).collectAsState()
    val sensorData by feederViewModel.sensorData.collectAsState()
    val isConnected by feederViewModel.connectionStatus.collectAsState()

    var showFeedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(petId) {
        petViewModel.selectPet(petId)
    }

    if (showFeedDialog && pet != null) {
        AlertDialog(
            onDismissRequest = { showFeedDialog = false },
            title = { Text("Alimentar") },
            text = { Text("¿Seguro que quieres alimentar a ${pet?.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        feeders.firstOrNull()?.let { feeder ->
                            feederViewModel.dispenseFood(feeder.id, 50)
                            petViewModel.updateLastFeedingTime(petId)
                            notificationViewModel.addNotification(
                                title = "Alimentación de ${pet?.name}",
                                message = "Tu mascota ha sido alimentada.",
                                type = "feeding"
                            )
                        }
                        showFeedDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B4FA3)
                    )
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFeedDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Feed Me App 🧡",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE8DEF8)
                )
            )
        }
    ) { paddingValues ->
        pet?.let { currentPet ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8DEF8))
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🐱", fontSize = 60.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentPet.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B4E)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFE4E1)
                    ) {
                        Text(
                            text = "${currentPet.weightKg} kg",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 14.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE1F5FF)
                    ) {
                        Text(
                            text = "${currentPet.ageYears} años",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Hora última alimentación",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentPet.lastFeedingTime?.let {
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(it))
                            } ?: "18:31",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Próximo control veterinario",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentPet.nextVetAppointment?.let {
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                            } ?: "21/10/2025",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Dispositivo de comida",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (feeders.isEmpty()) {
                            TextButton(
                                onClick = {
                                    navController.navigate(Screen.AddFeeder.createRoute(petId))
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Añadir nuevo dispositivo", color = Color(0xFF6B4FA3))
                            }
                        } else {
                            feeders.forEach { feeder ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!isConnected) {
                                                feederViewModel.connectToDevice(feeder.id)
                                            }
                                        }
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = feeder.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = if (isConnected) "Conectado" else "Desconectado",
                                            fontSize = 12.sp,
                                            color = if (isConnected) Color(0xFF4CAF50) else Color.Gray
                                        )
                                    }

                                    if (isConnected) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Conectado",
                                            tint = Color(0xFF4CAF50)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (isConnected && sensorData != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4E1))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Nivel de Alimento",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = (sensorData?.foodLevel ?: 0) / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = Color(0xFF6B4FA3),
                                trackColor = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "${sensorData?.foodLevel ?: 0}%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showFeedDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4DD0E1)
                        )
                    ) {
                        Icon(Icons.Default.Restaurant, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Dispensar Ahora", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}