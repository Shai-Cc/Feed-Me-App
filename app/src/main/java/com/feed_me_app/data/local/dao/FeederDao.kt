package com.feed_me_app.data.local.dao


import androidx.room.*
import com.feed_me_app.data.local.entities.FeederEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeederDao {
    @Query("SELECT * FROM feeders")
    fun getAllFeeders(): Flow<List<FeederEntity>>

    @Query("SELECT * FROM feeders WHERE petId = :petId")
    fun getFeedersByPetId(petId: Int): Flow<List<FeederEntity>>

    @Query("SELECT * FROM feeders WHERE id = :feederId")
    fun getFeederById(feederId: Int): Flow<FeederEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeder(feeder: FeederEntity): Long

    @Update
    suspend fun updateFeeder(feeder: FeederEntity)

    @Delete
    suspend fun deleteFeeder(feeder: FeederEntity)

    @Query("UPDATE feeders SET foodLevel = :level WHERE id = :feederId")
    suspend fun updateFoodLevel(feederId: Int, level: Int)

    @Query("UPDATE feeders SET isConnected = :connected WHERE id = :feederId")
    suspend fun updateConnectionStatus(feederId: Int, connected: Boolean)
}