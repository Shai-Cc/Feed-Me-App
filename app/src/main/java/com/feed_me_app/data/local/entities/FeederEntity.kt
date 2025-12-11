package com.feed_me_app.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeders")
data class FeederEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val petId: Int,
    val isConnected: Boolean = false,
    val foodLevel: Int = 100, // 0-100%
    val nextFeedingTime: Long? = null
)
