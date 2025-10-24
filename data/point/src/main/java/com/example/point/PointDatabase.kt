package com.example.point

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [PointEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PointDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao
    
    companion object {
        fun create(context: Context): PointDatabase {
            return Room.databaseBuilder(
                context,
                PointDatabase::class.java,
                "points_database"
            ).build()
        }
    }
}
