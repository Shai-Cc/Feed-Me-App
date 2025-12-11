package com.feed_me_app.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feed_me_app.data.local.entities.NotificationEntity
import com.feed_me_app.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    val allNotifications: StateFlow<List<NotificationEntity>> =
        repository.allNotifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotifications: StateFlow<List<NotificationEntity>> =
        repository.unreadNotifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNotification(title: String, message: String, type: String) {
        viewModelScope.launch {
            val notification = NotificationEntity(
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = type
            )
            repository.insertNotification(notification)
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.deleteNotification(notification)
        }
    }

    fun cleanOldNotifications() {
        viewModelScope.launch {
            repository.deleteOldNotifications(30)
        }
    }
}