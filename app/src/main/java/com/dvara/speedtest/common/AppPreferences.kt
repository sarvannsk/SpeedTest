package com.dvara.speedtest.common

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, MODE)
    }
    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_NAME, text)
        editor!!.commit()
    }
    fun getValueString(KEY_NAME: String): String? {
        return preferences.getString(KEY_NAME, null)
    }

}