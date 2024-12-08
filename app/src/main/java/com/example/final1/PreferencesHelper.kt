package com.example.final1

import android.content.Context
import android.content.SharedPreferences

fun saveData(key: String, value: String) {
    preferences.edit().putString(key, value).apply()
}

fun getData(key: String): String? {
    return preferences.getString(key, null)
}