package com.feed_me_app.data.repository


import com.feed_me_app.data.local.dao.FeederDao
import com.feed_me_app.data.local.entities.FeederEntity
import kotlinx.coroutines.flow.Flow

class FeederRepository(private val feederDao: FeederDao) {
    val allFeeders: Flow<List<FeederEntity>> = feederDao.getAllFeeders()

    fun getFeedersByPetId(petId: Int): Flow<List<FeederEntity>> =
        feederDao.getFeedersByPetId(petId)

    fun getFeederById(feederId: Int): Flow<FeederEntity?> =
        feederDao.getFeederById(feederId)

    suspend fun insertFeeder(feeder: FeederEntity): Long =
        feederDao.insertFeeder(feeder)

    suspend fun updateFeeder(feeder: FeederEntity) =
        feederDao.updateFeeder(feeder)

    suspend fun deleteFeeder(feeder: FeederEntity) =
        feederDao.deleteFeeder(feeder)

    suspend fun updateFoodLevel(feederId: Int, level: Int) {
        feederDao.updateFoodLevel(feederId, level)
    }

    suspend fun updateConnectionStatus(feederId: Int, connected: Boolean) {
        feederDao.updateConnectionStatus(feederId, connected)
    }
}