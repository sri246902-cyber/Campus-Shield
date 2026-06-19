package com.kct.campusshield.utils

import android.content.Context

object StatisticsManager {

    private const val PREF_NAME = "campusshield_stats"

    private const val TOTAL = "total"
    private const val SAFE = "safe"
    private const val SUSPICIOUS = "suspicious"
    private const val PHISHING = "phishing"

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    fun incrementTotal(context: Context) {

        val value =
            getTotal(context) + 1

        prefs(context)
            .edit()
            .putInt(TOTAL, value)
            .apply()
    }

    fun incrementSafe(context: Context) {

        val value =
            getSafe(context) + 1

        prefs(context)
            .edit()
            .putInt(SAFE, value)
            .apply()
    }

    fun incrementSuspicious(
        context: Context
    ) {

        val value =
            getSuspicious(context) + 1

        prefs(context)
            .edit()
            .putInt(SUSPICIOUS, value)
            .apply()
    }

    fun incrementPhishing(
        context: Context
    ) {

        val value =
            getPhishing(context) + 1

        prefs(context)
            .edit()
            .putInt(PHISHING, value)
            .apply()
    }

    fun getTotal(context: Context) =
        prefs(context).getInt(TOTAL, 0)

    fun getSafe(context: Context) =
        prefs(context).getInt(SAFE, 0)

    fun getSuspicious(context: Context) =
        prefs(context).getInt(SUSPICIOUS, 0)

    fun getPhishing(context: Context) =
        prefs(context).getInt(PHISHING, 0)
}