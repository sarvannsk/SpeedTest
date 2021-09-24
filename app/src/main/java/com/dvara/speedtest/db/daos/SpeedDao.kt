package com.dvara.speedtest.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dvara.speedtest.common.Constants
import com.dvara.speedtest.db.entities.SpeedTest

@Dao
interface SpeedDao {

    @Insert
    fun addSpeed(speedTest: SpeedTest) : Long

    @Update
    fun updateSpeed(speedTest: SpeedTest)

    @Delete
    fun deleteAccount(speedTest: SpeedTest)

    @Query("SELECT * FROM "+ Constants.TBL_TRN_SPEED_TEST+" ORDER BY "+Constants.COL_ID+" DESC")
    fun getAll() : LiveData<List<SpeedTest>>

}