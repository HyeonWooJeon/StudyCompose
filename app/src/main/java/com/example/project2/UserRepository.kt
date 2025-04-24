package com.example.project2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Dao를 연결해 DB에 브릿지 역할
class UserRepository(context: Context) {
    private val userDao: UserDao = DatabaseProvider.getDatabase(context).userDAO()
    //예제 참조
    fun addUser(user: User, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(user)
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

    fun getAllUsers(callback: (List<User>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val users = userDao.getAllUsers()
            withContext(Dispatchers.Main) {
                callback(users)
            }
        }
    }

    fun deleteUser(user: UserInfo, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteUser(user)
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }
}

//개인 연습용
open class UserInfoRepository(context: Context) {
    private val userInfoDao: UserInfoDao = DatabaseProvider.getDatabase(context).userInfoDAO()
    open fun addUserInfo(userInfo: UserInfo, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            userInfoDao.insertUserInfo(userInfo)
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

    open fun getAllUserInfo(callback: (List<UserInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val usersInfo = userInfoDao.getUsersInfo()
            withContext(Dispatchers.Main) {
                callback(usersInfo)
            }
        }
    }

    open fun deleteUserInfo(userInfo: UserInfo, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            userInfoDao.deleteUserInfo(userInfo)
            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }
}

//Weather Project
open class WeatherWordRepository(context: Context){

    private val searchWordDao: SearchWordDao = DatabaseProvider.getDatabase(context).searchWordDAO()

    //검색어를 저장할 변수
    private val saveWordResult = MutableLiveData<List<SearchWord>>()
    val saveSearchWord : LiveData<List<SearchWord>> get() = saveWordResult

    init{
        getSearchWord()
    }

    // 검색어 추가
    open fun addWeatherWord(searchWord: SearchWord, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            searchWordDao.insertWord(searchWord)
            withContext(Dispatchers.Main) {
                callback()
                // 추가 후 목록을 다시 불러와서 업데이트
                getSearchWord()
            }
        }
    }
    // 검색어 Select
    open fun getSearchWord(): LiveData<List<SearchWord>> {
        val liveData = MutableLiveData<List<SearchWord>>()
        CoroutineScope(Dispatchers.IO).launch {
            val searchWordList = searchWordDao.getSearchWord()

            withContext(Dispatchers.Main) {
                liveData.postValue(searchWordList)
            }
        }
        return liveData
    }

    // 검색어 Delete
    open fun deleteSearchWord(searchWord: SearchWord, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            searchWordDao.deleteSearchWord(searchWord)
            withContext(Dispatchers.Main) {
                callback()
                // 삭제 후 목록을 다시 불러와서 업데이트
                getSearchWord()
            }
        }
    }
}