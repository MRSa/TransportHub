package net.osdn.ja.gokigen.transporthub.mobile.storage

import androidx.room.TypeConverter
import java.util.Date

class DateConverter
{
    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date?) : Long? {
        return date?.time
    }
}
