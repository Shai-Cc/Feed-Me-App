package com.feed_me_app.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.feed_me_app.data.local.entities.FeederEntity
import com.feed_me_app.ui.viewmodels.FeederViewModel
import com.feed_me_app.ui.viewmodels.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeederScreen(
    petId: Int,
    navController: NavController,
    feederViewModel: FeederViewModel,
    petViewModel: PetViewModel
) {
    val pet by petViewModel.selectedPet.collectAsState()
    var selectedDevice by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<Boolean?>(null) }

    val availableDevices = listOf(
        "Comedero MiMi",
        "Feeder Pro X1",
        "SmartPet Dispenser",
        "AutoFood 3000"
    )

    LaunchedEffect(petId) {
        petViewModel.selectPet(petId)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8DEF8))
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(
                "Añadir alimentador",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B4E)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Seleccionar mascota",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = pet?.name ?: "",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.Black
                ),
                trailingIcon = {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Dispositivos disponibles",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedDevice.ifEmpty { "Nombre dispositivo" },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF6B4FA3),
                        unfocusedBorderColor = Color.Gray
                    ),
                    trailingIcon = {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableDevices.forEach { device ->
                        DropdownMenuItem(
                            text = { Text(device) },
                            onClick = {
                                selectedDevice = device
                                expanded = false
                                testResult = null
                            }
                        )
                    }
                }
            }

            if (selectedDevice.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        testResult = feederViewModel.testConnection()
                        if (testResult == false) {
                            // Simular conexión exitosa en la primera prueba
                            testResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Probar")
                    if (testResult == true) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (selectedDevice.isNotEmpty() && testResult == true) {
                            val newFeeder = FeederEntity(
                                name = selectedDevice,
                                petId = petId,
                                isConnected = false
                            )
                            feederViewModel.insertFeeder(newFeeder)
                            navController.navigateUp()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedDevice.isNotEmpty() && testResult == true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B4FA3)
                    )
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}