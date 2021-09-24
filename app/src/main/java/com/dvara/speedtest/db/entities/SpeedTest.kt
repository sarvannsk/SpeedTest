package com.dvara.speedtest.db.entities

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dvara.speedtest.common.Constants
import java.util.*

@Entity(tableName = Constants.TBL_TRN_SPEED_TEST, indices = [Index(value = [Constants.COL_ID], unique = true)])
class SpeedTest() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.COL_ID)
    var id: Long? = null

    @ColumnInfo(name = Constants.COL_MOBILE_NUMBER)
    var mobileNumber: String? = null

    @ColumnInfo(name = Constants.COL_SPEED)
    var speed: String? = null

    @ColumnInfo(name = Constants.COL_CURRENT_TIME)
    var timeStamp: String? = null


    constructor(id: Long?, mobileNumber: String?, speed: String?, timeStamp: String?) : this() {
        this.id = id
        this.mobileNumber = mobileNumber
        this.speed = speed
        this.timeStamp = timeStamp
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        mobileNumber = parcel.readString()
        speed = parcel.readString()
        timeStamp = parcel.readString()
    }
    // for DiffUtil class
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SpeedTest) return false
        val speedTest = other as? SpeedTest
        return id == speedTest?.id
                && mobileNumber == speedTest?.mobileNumber
                && speed == speedTest?.speed
                && timeStamp == speedTest?.timeStamp

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(id, mobileNumber, speed, timeStamp)
    }

    fun firstLetter(accountName: String): String {
        val firstChar: Char = accountName.get(0)
        return firstChar.toString()
    }

    // parcelable stuff
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(mobileNumber)
        parcel.writeString(speed)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpeedTest> {
        override fun createFromParcel(parcel: Parcel): SpeedTest {
            return SpeedTest(parcel)
        }

        override fun newArray(size: Int): Array<SpeedTest?> {
            return arrayOfNulls(size)
        }
    }

}