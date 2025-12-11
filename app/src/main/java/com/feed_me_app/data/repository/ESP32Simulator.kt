package com.feed_me_app.data.repository


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

data class SensorData(
    val foodLevel: Int,
    val petPresent: Boolean,
    val weight: Float,
    val lastDispensed: Long?
)

class ESP32Simulator {
    private var currentFoodLevel = 85
    private var isConnected = false
    private var lastDispensedTime: Long? = null

    fun connect(): Flow<Boolean> = flow {
        emit(false)
        delay(1000) // Simular tiempo de conexión
        isConnected = true
        emit(true)
    }

    fun disconnect() {
        isConnected = false
    }

    fun getSensorData(): Flow<SensorData> = flow {
        while (isConnected) {
            val data = SensorData(
                foodLevel = currentFoodLevel,
                petPresent = Random.nextBoolean(),
                weight = Random.nextFloat() * 0.5f + 4.0f, // 4.0 - 4.5 kg
                lastDispensed = lastDispensedTime
            )
            emit(data)
            delay(2000) // Actualizar cada 2 segundos
        }
    }

    suspend fun dispenseFood(amount: Int = 50): Boolean {
        if (!isConnected) return false

        delay(500) // Simular tiempo de dispensación
        currentFoodLevel = maxOf(0, currentFoodLevel - amount)
        lastDispensedTime = System.currentTimeMillis()
        return true
    }

    fun isConnected(): Boolean = isConnected

    fun getCurrentFoodLevel(): Int = currentFoodLevel

    // Simular recarga de comida
    fun refillFood() {
        currentFoodLevel = 100
    }
}