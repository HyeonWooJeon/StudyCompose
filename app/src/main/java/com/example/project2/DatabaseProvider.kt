package com.example.project2

import android.content.Context
import androidx.room.Room

//Room 라이브러리로 안드로이드 내부 DB에 연결
object DatabaseProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return db ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).addMigrations(MIGRATION_2_3)
                .build()
            db = instance
            instance
        }
    }
}