package com.example.hoinzeyshabits.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hoinzeyshabits.data.DateTimeConverter
import org.joda.time.DateTime

@Entity(tableName = "habits")
@TypeConverters(DateTimeConverter::class)
data class Habit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "habitid")
    var habitId: Int = 0,
    val name: String,
    val habitFrequency: HabitFrequency,
    val habitFrequencyCount: Int = 1,
    val creationDate: DateTime = DateTime()
)

enum class HabitFrequency{
    DAILY,
    WEEKLY
}
