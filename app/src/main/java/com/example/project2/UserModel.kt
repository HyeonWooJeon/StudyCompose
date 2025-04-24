package com.example.project2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserModel(private val userDao: UserDao) : ViewModel() {
    var users by mutableStateOf<List<User>>(listOf())
        private set

    fun getAllUsers() {
        viewModelScope.launch {
            users = userDao.getAllUsers()
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
            getAllUsers() // 업데이트된 유저 목록을 가져옴
        }
    }
}