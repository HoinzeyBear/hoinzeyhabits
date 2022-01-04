package com.example.hoinzeyshabits.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Conversion {

    private const val ISO8601_STRING_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ"

    val ISO8601: DateTimeFormatter = DateTimeFormat.forPattern(ISO8601_STRING_FORMAT)

    fun getFormattedStartTime(dateTime: DateTime): String {
        return ISO8601.print(dateTime.withTimeAtStartOfDay())
    }

    fun getFormattedEndTime(dateTime: DateTime): String {
        return ISO8601.print(dateTime.withTime(23,59,59,59))
    }
}