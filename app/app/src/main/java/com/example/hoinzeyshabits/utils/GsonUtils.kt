package com.example.hoinzeyshabits.utils

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type

object GsonUtils {
    fun dateTimeGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(DateTime::class.java, DateTimeSerializer)
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer)
            .create()
    }
}

@Suppress("unused")
private object DateTimeSerializer: JsonSerializer<DateTime> {
    override fun serialize(
        src: DateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(ISODateTimeFormat.dateTime().print(src))
    }
}

@Suppress("unused")
private object DateTimeDeserializer: JsonDeserializer<DateTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DateTime? {
        if(json is JsonObject) {
            return DateTime()
        } else {
            return ISODateTimeFormat.dateTimeParser().parseDateTime(json!!.asString)
        }
    }
}