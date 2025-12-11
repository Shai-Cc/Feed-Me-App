package com.feed_me_app.data.repository


import com.feed_me_app.data.local.dao.NotificationDao
import com.feed_me_app.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {
    val allNotifications: Flow<List<NotificationEntity>> =
        notificationDao.getAllNotifications()

    val unreadNotifications: Flow<List<NotificationEntity>> =
        notificationDao.getUnreadNotifications()

    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    suspend fun updateNotification(notification: NotificationEntity) {
        notificationDao.updateNotification(notification)
    }

    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }

    suspend fun markAsRead(notificationId: Int) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun deleteOldNotifications(daysOld: Int = 30) {
        val cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)
        notificationDao.deleteOldNotifications(cutoffTime)
    }
}