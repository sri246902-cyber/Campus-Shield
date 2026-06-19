package com.kct.campusshield.whois

data class WhoisResponse(

    val status: Boolean?,

    val domain_name: String?,

    val domain_registered: String?,

    val create_date: String?,

    val update_date: String?,

    val expiry_date: String?

)