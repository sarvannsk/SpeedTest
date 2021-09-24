package com.dvara.speedtest.db.repository

import androidx.lifecycle.LiveData
import com.dvara.speedtest.db.daos.SpeedDao
import com.dvara.speedtest.db.entities.SpeedTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * The class for managing multiple data sources.
 */
class SpeedRepository(private val speedDao: SpeedDao) {

    fun insertSpeed(speed: SpeedTest) {
        CoroutineScope(Dispatchers.IO).launch {
            speedDao.addSpeed(speed)
        }
    }


    fun getAll(): LiveData<List<SpeedTest>> {
        return speedDao.getAll()
    }


    companion object {

        // Marks the JVM backing field of the annotated property as volatile, meaning that writes to this field are immediately made visible to other threads.
        @Volatile
        private var instance: SpeedRepository? = null

        // For Singleton instantiation.
        fun getInstance(speedDao: SpeedDao) =
            instance ?: synchronized(this) {
                instance ?: SpeedRepository(speedDao).also { instance = it }
            }
    }


}