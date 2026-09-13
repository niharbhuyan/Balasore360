package com.example.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room TypeConverters for converting complex types (e.g. Date, String Lists)
 * to and from SQLite-compatible primitive types.
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        if (list == null) return null
        return list.joinToString(separator = "|||")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        if (data == null) return null
        if (data.isBlank()) return emptyList()
        return data.split("|||").map { it.trim() }
    }

    @TypeConverter
    fun fromLongList(list: List<Long>?): String? {
        if (list == null) return null
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toLongList(data: String?): List<Long>? {
        if (data == null) return null
        if (data.isBlank()) return emptyList()
        return data.split(",").mapNotNull { it.trim().toLongOrNull() }
    }
}
