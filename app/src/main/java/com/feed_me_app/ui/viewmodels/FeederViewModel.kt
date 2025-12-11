package com.feed_me_app.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feed_me_app.data.local.entities.FeederEntity
import com.feed_me_app.data.repository.ESP32Simulator
import com.feed_me_app.data.repository.FeederRepository
import com.feed_me_app.data.repository.SensorData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeederViewModel(
    private val repository: FeederRepository,
    private val esp32Simulator: ESP32Simulator
) : ViewModel() {

    val allFeeders: StateFlow<List<FeederEntity>> = repository.allFeeders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sensorData = MutableStateFlow<SensorData?>(null)
    val sensorData: StateFlow<SensorData?> = _sensorData.asStateFlow()

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()

    private val _connectionStatus = MutableStateFlow(false)
    val connectionStatus: StateFlow<Boolean> = _connectionStatus.asStateFlow()

    fun getFeedersByPetId(petId: Int): StateFlow<List<FeederEntity>> {
        return repository.getFeedersByPetId(petId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun insertFeeder(feeder: FeederEntity) {
        viewModelScope.launch {
            repository.insertFeeder(feeder)
        }
    }

    fun updateFeeder(feeder: FeederEntity) {
        viewModelScope.launch {
            repository.updateFeeder(feeder)
        }
    }

    fun deleteFeeder(feeder: FeederEntity) {
        viewModelScope.launch {
            repository.deleteFeeder(feeder)
        }
    }

    fun connectToDevice(feederId: Int) {
        viewModelScope.launch {
            _isConnecting.value = true
            esp32Simulator.connect().collect { connected ->
                _connectionStatus.value = connected
                repository.updateConnectionStatus(feederId, connected)
                _isConnecting.value = false

                if (connected) {
                    startSensorMonitoring()
                }
            }
        }
    }

    fun disconnectDevice(feederId: Int) {
        viewModelScope.launch {
            esp32Simulator.disconnect()
            _connectionStatus.value = false
            _sensorData.value = null
            repository.updateConnectionStatus(feederId, false)
        }
    }

    private fun startSensorMonitoring() {
        viewModelScope.launch {
            esp32Simulator.getSensorData().collect { data ->
                _sensorData.value = data
            }
        }
    }

    fun dispenseFood(feederId: Int, amount: Int = 50) {
        viewModelScope.launch {
            val success = esp32Simulator.dispenseFood(amount)
            if (success) {
                val newLevel = esp32Simulator.getCurrentFoodLevel()
                repository.updateFoodLevel(feederId, newLevel)
            }
        }
    }

    fun testConnection(): Boolean {
        return esp32Simulator.isConnected()
    }
}