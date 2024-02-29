package cufoon.litkeep.android.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(time: OffsetDateTime): String {
        return time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @FromJson
    fun fromJson(time: String): OffsetDateTime {
        return OffsetDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}
