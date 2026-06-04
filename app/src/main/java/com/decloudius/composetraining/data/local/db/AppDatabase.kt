package com.decloudius.composetraining.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.decloudius.composetraining.data.local.db.entity.PhotoEntity
import com.decloudius.composetraining.data.local.db.entity.ProfileEntity

@Database(
    entities = [PhotoEntity::class, ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "familytree_db"

        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )

                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
