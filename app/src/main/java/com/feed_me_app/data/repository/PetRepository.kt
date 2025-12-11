package com.feed_me_app.data.repository


import com.feed_me_app.data.local.dao.PetDao
import com.feed_me_app.data.local.entities.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {
    val allPets: Flow<List<PetEntity>> = petDao.getAllPets()

    fun getPetById(petId: Int): Flow<PetEntity?> = petDao.getPetById(petId)

    val favoritePets: Flow<List<PetEntity>> = petDao.getFavoritePets()

    suspend fun insertPet(pet: PetEntity): Long = petDao.insertPet(pet)

    suspend fun updatePet(pet: PetEntity) = petDao.updatePet(pet)

    suspend fun deletePet(pet: PetEntity) = petDao.deletePet(pet)

    suspend fun toggleFavorite(petId: Int, isFavorite: Boolean) {
        petDao.updateFavorite(petId, isFavorite)
    }

    suspend fun updateLastFeedingTime(petId: Int, time: Long) {
        petDao.updateLastFeedingTime(petId, time)
    }
}