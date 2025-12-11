package com.feed_me_app.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.feed_me_app.data.local.dao.FeederDao
import com.feed_me_app.data.local.dao.NotificationDao
import com.feed_me_app.data.local.dao.PetDao
import com.feed_me_app.data.local.entities.FeederEntity
import com.feed_me_app.data.local.entities.NotificationEntity
import com.feed_me_app.data.local.entities.PetEntity

@Database(
    entities = [
        PetEntity::class,
        FeederEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FeedMeDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun feederDao(): FeederDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: FeedMeDatabase? = null

        fun getDatabase(context: Context): FeedMeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FeedMeDatabase::class.java,
                    "feedme_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}