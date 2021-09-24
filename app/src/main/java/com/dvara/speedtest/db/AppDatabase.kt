package com.dvara.speedtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dvara.speedtest.common.Constants
import com.dvara.speedtest.db.daos.SpeedDao
import com.dvara.speedtest.db.entities.SpeedTest


@Database(entities = [SpeedTest::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun speedDao(): SpeedDao

    // The AppDatabase a singleton to prevent having multiple instances of the database opened at the same time.
    companion object {
        // Marks the JVM backing field of the annotated property as volatile, meaning that writes to this field are immediately made visible to other threads.
        @Volatile
        private var instance: AppDatabase? = null

        // For Singleton instantiation.
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Creates and pre-populates the database.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
                // Prepopulate the database after onCreate was called.
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                  }
                }).build()
        }
    }
}