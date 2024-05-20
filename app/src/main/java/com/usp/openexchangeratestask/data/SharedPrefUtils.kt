package com.usp.openexchangeratestask.data

import android.content.Context
import java.sql.Timestamp

interface ISharedPrefUtils {
    fun saveCurrentTimestamp(timestamp: Long = System.currentTimeMillis())
    fun getSavedTimestamp(): Long
}

class SharedPrefUtils(private val context: Context) : ISharedPrefUtils {
    companion object {
        const val PREF_NAME: String = "com.usp.openexchangeratestask.PREFERENCE_APP_FOR_TIMESTAMP"
        const val KEY_TIMESTAMP: String = "timestamp"
    }

    override fun saveCurrentTimestamp(timestamp: Long) {
        val currentTimestamp = System.currentTimeMillis()
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(KEY_TIMESTAMP, currentTimestamp)
        editor.apply()
    }

    override fun getSavedTimestamp(): Long {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(KEY_TIMESTAMP, 0)
    }
}
