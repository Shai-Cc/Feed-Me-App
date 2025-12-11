package com.feed_me_app.ui.viewmodels


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserState(
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val userEmail: String = ""
)

class AuthViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun login(email: String) {
        _userState.value = UserState(
            isLoggedIn = true,
            userName = email.substringBefore("@").capitalize(),
            userEmail = email
        )
    }

    fun logout() {
        _userState.value = UserState()
    }

    fun isLoggedIn(): Boolean = _userState.value.isLoggedIn
}