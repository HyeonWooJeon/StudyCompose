package com.example.project2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//데이터 베이스 테이블을 정의하는 역할

//엔티티 테이블 Users
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val name: String,
        val age: Int,
        val phone: String
)

//엔티티 테이블 UserInfo
@Entity(tableName = "userInfo")
data class UserInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val birth: String,
    val cardNum: Long,
    val efDate: String,
    val pw: String,
    val phoneNum: Long,
    val email: String,
    val homeNum: Int?,
    val safeNum:String?
)

@Entity(tableName = "SearchWord")
data class SearchWord (
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name="searchWord") val searchWord: String,
)