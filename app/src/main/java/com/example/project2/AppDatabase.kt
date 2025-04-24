package com.example.project2

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, UserInfo::class, SearchWord::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDao
    abstract fun userInfoDAO(): UserInfoDao
    abstract fun searchWordDAO(): SearchWordDao
}
//마이그레이션 1 -> 2 -> 3
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 새 테이블 생성
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `SearchWord` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `searchWord` TEXT   
            )
        """
        )
    }
}
