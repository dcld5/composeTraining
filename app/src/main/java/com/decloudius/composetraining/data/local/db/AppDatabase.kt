package com.decloudius.composetraining.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.decloudius.composetraining.data.local.db.entity.PhotoEntity
import com.decloudius.composetraining.data.local.db.entity.ProfileEntity

/**
 * The Database class is the main entry point to Room.
 * It connects the entities (tables) with the DAOs (data accessors).
 * Room builds the actual SQLite file automatically.
 *
 * version = 1 -> whenever you change the schema, you must bump this number
 * and write a Migration (or just uninstall the app while learning).
 */
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

        /** Build the database. Room uses a singleton pattern (one instance only). */
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                // Allows Room to recreate tables if version changes without a migration.
                // ONLY use this while learning; real apps need proper Migrations.
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
