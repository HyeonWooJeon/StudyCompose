package com.example.project2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
//DAO 어노테이션으로 관련 DB에 Query없이 접근

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Delete
    fun deleteUser(user: UserInfo)
}

@Dao
interface UserInfoDao{
    @Insert
    fun insertUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM userInfo")
    fun getUsersInfo(): List<UserInfo>

    @Delete
    fun deleteUserInfo(userInfo: UserInfo)
}

@Dao
interface SearchWordDao{
    @Insert
    fun insertWord(SearchWord: SearchWord)

    @Query("SELECT * FROM SearchWord ORDER BY id DESC")
    fun getSearchWord(): List<SearchWord>

    @Delete
    fun deleteSearchWord(searchWord: SearchWord)
}
