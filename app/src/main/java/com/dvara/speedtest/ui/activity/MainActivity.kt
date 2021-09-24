package com.dvara.speedtest.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dvara.speedtest.R
import com.dvara.speedtest.common.AppPreferences
import com.dvara.speedtest.common.Constants
import com.dvara.speedtest.common.Utils
import com.dvara.speedtest.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){

    lateinit var toolBar: Toolbar

    lateinit var navView: BottomNavigationView

    private lateinit var binding: ActivityMainBinding

    var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView

        toolBar = binding.toolBar

        setSupportActionBar(toolBar)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        visibilityNavElements(navController) //If you want to hide ToolBar configure that in this function

//        askPermissionAndGetPhoneNumbers()

    }
    fun visibilityNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_speed_test -> showToolBar(true,true,resources.getString(R.string.app_name))
                R.id.navigation_search -> showToolBar(true,true,resources.getString(R.string.app_name))
            }
        }
    }
    fun hideToolBar(isToolBar:Boolean,isNavView: Boolean) { //Hide ToolBar
        Utils.showViews(isToolBar,toolBar)
        Utils.showViews(isNavView,navView)
    }
    fun showToolBar(isToolBar:Boolean,isNavView: Boolean,title:String) { //SHOW ToolBar
        Utils.showViews(isToolBar,toolBar)
        Utils.showViews(isNavView,navView)
        setTitle(title)
    }

//    private fun askPermissionAndGetPhoneNumbers() {
//
//        // With Android Level >= 23, you have to ask the user
//        // for permission to get Phone Number.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 23
//
//            // Check if we have READ_PHONE_STATE permission
//            val readPhoneStatePermission = ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_PHONE_STATE
//            )
//            if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
//                // If don't have permission so prompt the user.
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_PHONE_STATE),
//                    Constants.PERMISSION_REQUEST_CODE
//                )
//                return
//            }
//        }
//        this.getPhoneNumbers()
//    }
//
//    // Need to ask user for permission: android.permission.READ_PHONE_STATE
//    @SuppressLint("MissingPermission")
//    private fun getPhoneNumbers() {
//        try {
//            val manager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//            val phoneNumber1 = manager.line1Number
//             if(phoneNumber1!=null){
//                AppPreferences.save(Constants.PREF_MOBILE_NUMBER,phoneNumber1)
//             }else{
//                 Toast.makeText(this, "Unable to fetch mobile number over the phone",Toast.LENGTH_LONG).show()
//             }
//
//            Log.i(TAG, "Your Phone Number: $phoneNumber1")
//            Toast.makeText(
//                this, "Your Phone Number: $phoneNumber1",
//                Toast.LENGTH_LONG
//            ).show()
//
//            // Other Informations:
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API Level 26.
//                val imei = manager.imei
//                val phoneCount = manager.phoneCount
//                Log.i(TAG, "Phone Count: $phoneCount")
//                Log.i(TAG, "EMEI: $imei")
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // API Level 28.
//                val signalStrength = manager.signalStrength
//                val level = signalStrength!!.level
//                Log.i(TAG, "Signal Strength Level: $level")
//            }
//        } catch (ex: Exception) {
//            Log.e(TAG, "Error: ", ex)
//            Toast.makeText(
//                this, "Error: " + ex.message,
//                Toast.LENGTH_LONG
//            ).show()
//            ex.printStackTrace()
//        }
//    }
//
//    override fun onRequestPermissionsResult( requestCode: Int,permissions: Array<String?>, grantResults: IntArray ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            Constants.PERMISSION_REQUEST_CODE -> {
//                // Note: If request is cancelled, the result arrays are empty.
//                // Permissions granted (SEND_SMS).
//                if (grantResults.size > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    Log.i(TAG, "Permission granted!")
//                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show()
//                    getPhoneNumbers()
//                } else {
//                    Log.i(TAG, "Permission denied!")
//                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    // When results returned
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Do something with data (Result returned).
//                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show()
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

}