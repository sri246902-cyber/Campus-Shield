package com.kct.campusshield.security

data class RiskResult(
    val status: String,
    val score: Int,
    val reasons: List<String>
)