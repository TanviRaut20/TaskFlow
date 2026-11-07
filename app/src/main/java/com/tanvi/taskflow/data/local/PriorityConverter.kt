package com.tanvi.taskflow.data.local

import androidx.room.TypeConverter
import com.tanvi.taskflow.data.model.Priority

class PriorityConverter{
    @TypeConverter
    public fun fromPriority(priority: Priority): String{
        return priority.name
    }

    @TypeConverter
    public fun toPriority(value: String): Priority{
        return Priority.valueOf(value)
    }
}
