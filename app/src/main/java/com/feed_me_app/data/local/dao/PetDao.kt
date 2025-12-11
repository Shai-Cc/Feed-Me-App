package com.feed_me_app.data.local.dao


import androidx.room.*
import com.feed_me_app.data.local.entities.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: Int): Flow<PetEntity?>

    @Query("SELECT * FROM pets WHERE isFavorite = 1")
    fun getFavoritePets(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("UPDATE pets SET isFavorite = :isFavorite WHERE id = :petId")
    suspend fun updateFavorite(petId: Int, isFavorite: Boolean)

    @Query("UPDATE pets SET lastFeedingTime = :time WHERE id = :petId")
    suspend fun updateLastFeedingTime(petId: Int, time: Long)
}