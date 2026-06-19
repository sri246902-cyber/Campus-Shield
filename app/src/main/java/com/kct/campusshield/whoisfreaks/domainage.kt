package com.kct.campusshield.security

import java.time.LocalDate
import java.time.Period

object DomainAgeChecker {

    fun getDomainAgeYears(
        createDate: String?
    ): Int {

        return try {

            if (createDate == null)
                return 0

            val created =
                LocalDate.parse(createDate)

            Period.between(
                created,
                LocalDate.now()
            ).years

        } catch (e: Exception) {

            0

        }
    }
}