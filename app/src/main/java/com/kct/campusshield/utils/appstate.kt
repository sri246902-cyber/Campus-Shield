package com.kct.campusshield.utils

import android.content.Context

object AppState {

    private const val PREF_NAME = "campusshield"

    fun isLoggedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean("loggedIn", false)
    }

    fun setLoggedIn(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("loggedIn", value)
            .apply()
    }

    fun isProtectionEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean("protectionEnabled", false)
    }

    fun setProtectionEnabled(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("protectionEnabled", value)
            .apply()
    }
}