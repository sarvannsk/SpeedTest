package com.dvara.speedtest.common

import android.content.Context
import android.view.View
import android.widget.Toast
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    var toast: Toast? = null

     fun showViews(show: Boolean, vararg views: View) {
        val visibility = if (show) View.VISIBLE else View.GONE
        for (view in views) {
            view.visibility = visibility
        }
    }

    fun currentTime() : String{
        val currentTime = Calendar.getInstance().time
        val  simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val  currentTimeStamp = simpleDateFormat.format(currentTime).toString()
        return currentTimeStamp
    }
    fun toast(context: Context?, text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }
    fun roundOffDecimal(number: Double?): Double? {
        val df = DecimalFormat("#0.00")
        return df.format(number).toDouble()
    }

}