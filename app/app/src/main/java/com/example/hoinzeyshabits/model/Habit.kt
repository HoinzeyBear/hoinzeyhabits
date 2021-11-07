package com.example.hoinzeyshabits.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hoinzeyshabits.data.TypeConverters
import org.joda.time.DateTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "habitid")
    val habitId: Int = 0,
    val name: String,
    val habitFrequency: HabitFrequency,
    val habitFrequencyCount: Int = 1,
//    @androidx.room.TypeConverters(TypeConverters::class)
//    val creationDate: DateTime = DateTime.now(),
)

enum class HabitFrequency{
    DAILY,
    WEEKLY
}
