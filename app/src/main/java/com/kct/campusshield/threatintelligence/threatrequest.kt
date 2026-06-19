package com.kct.campusshield.threat

data class ThreatRequest(
    val client: Client,
    val threatInfo: ThreatInfo
)

data class Client(
    val clientId: String,
    val clientVersion: String
)

data class ThreatInfo(
    val threatTypes: List<String>,
    val platformTypes: List<String>,
    val threatEntryTypes: List<String>,
    val threatEntries: List<ThreatEntry>
)

data class ThreatEntry(
    val url: String
)