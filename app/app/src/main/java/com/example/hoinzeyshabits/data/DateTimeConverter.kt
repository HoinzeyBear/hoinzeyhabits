package com.example.hoinzeyshabits.data

import androidx.room.TypeConverter
import com.example.hoinzeyshabits.utils.Conversion
import com.example.hoinzeyshabits.utils.StringUtils
import org.joda.time.DateTime

class DateTimeConverter {

    @Suppress("unused")
    @TypeConverter
    fun stringToDateTime(value: String): DateTime? {
        return if (StringUtils.nullOrEmpty(value)) null else Conversion.ISO8601.parseDateTime(value)
    }

    @Suppress("unused")
    @TypeConverter
    fun dateTimeToString(value: DateTime?): String {
        return if (value == null) "" else Conversion.ISO8601.print(value)
    }
}