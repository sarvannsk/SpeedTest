package com.dvara.speedtest.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvara.speedtest.db.AppDatabase
import com.dvara.speedtest.db.entities.SpeedTest
import com.dvara.speedtest.db.repository.SpeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SpeedTestViewModel (application: Application) : AndroidViewModel(application) {

    val mSpeedRepository: SpeedRepository
    val appDb: AppDatabase
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

//    //Database
//    var parentJob = Job()
//    val coroutineContext: CoroutineContext
//        get() = parentJob + Dispatchers.Main
//    val scope = CoroutineScope(coroutineContext)

    init {
        appDb = AppDatabase.getInstance(application)
        mSpeedRepository = SpeedRepository(appDb.speedDao())
    }
    fun addSpeed(speedTest: SpeedTest) {
        mSpeedRepository.insertSpeed(speedTest)
    }
    var all: LiveData<List<SpeedTest>> = mSpeedRepository.getAll()


//    override fun onCleared() {
//        super.onCleared()
//        parentJob.cancel()
//    }
}