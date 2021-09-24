package com.dvara.speedtest.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.dvara.speedtest.R
import com.dvara.speedtest.common.AppPreferences
import com.dvara.speedtest.common.Constants
import com.dvara.speedtest.common.Utils

class SplashActivity : AppCompatActivity() {
    var TAG = "SplashActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun onStart() {
        super.onStart()
        askPermissionAndGetPhoneNumbers()
    }
    fun invokeHandler() {
        Handler(Looper.getMainLooper()).postDelayed({ launchActivity() }, Constants.SPLASH_DELAY)
    }
    fun launchActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
    private fun askPermissionAndGetPhoneNumbers() {

        // With Android Level >= 23, you have to ask the user
        // for permission to get Phone Number.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 23

            // Check if we have READ_PHONE_STATE permission
            val readPhoneStatePermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE )
            if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissions( arrayOf(Manifest.permission.READ_PHONE_STATE), Constants.PERMISSION_REQUEST_CODE)
                return
            }
        }
        this.getPhoneNumbers()
    }

    // Need to ask user for permission: android.permission.READ_PHONE_STATE
    @SuppressLint("MissingPermission")
    private fun getPhoneNumbers() {
        try {
            val manager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber1 = manager.line1Number
            if(phoneNumber1!=null){
                AppPreferences.save(Constants.PREF_MOBILE_NUMBER,phoneNumber1)
            }else{
                Utils.toast(this,"Unable to fetch mobile number over the phone")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error: ", ex)
            Utils.toast(this,"Error: " + ex.message)
            ex.printStackTrace()
        }finally {
            invokeHandler()
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int,permissions: Array<String?>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.PERMISSION_REQUEST_CODE -> {
                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.size > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission granted!")
                    Utils.toast(this,"Permission granted!")
                    getPhoneNumbers()
                } else {
                    Log.i(TAG, "Permission denied!")
                    Utils.toast(this,"Permission denied!")
                    invokeHandler()
                }
            }
        }
    }

    // When results returned
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Utils.toast(this,"Action OK")
            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(this,"Action Cancelled")
            } else {
                Utils.toast(this,"Action Failed")
            }
        }
    }

}