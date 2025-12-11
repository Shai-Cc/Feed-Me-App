package com.feed_me_app.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val weightKg: Float,
    val ageYears: Int,
    val photoUri: String? = null,
    val lastFeedingTime: Long? = null,
    val nextVetAppointment: Long? = null,
    val isFavorite: Boolean = false
)