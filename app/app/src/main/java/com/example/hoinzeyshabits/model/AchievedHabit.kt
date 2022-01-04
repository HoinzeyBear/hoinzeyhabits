package com.example.hoinzeyshabits.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.example.hoinzeyshabits.data.DateTimeConverter
import org.joda.time.DateTime

@Entity(tableName = "achievedhabits",
    foreignKeys = [ForeignKey(
    entity = Habit::class,
    parentColumns = arrayOf("habitid"),
    childColumns = arrayOf("habitid"),
    onDelete = ForeignKey.CASCADE)],
    primaryKeys = ["habitid", "achieveddate"])
@TypeConverters(DateTimeConverter::class)
data class AchievedHabit(
    @ColumnInfo(name = "habitid", index = true)
    var habitId: Int = 0,
    @ColumnInfo(name = "achieveddate")
    val achievedDate: DateTime,
)
