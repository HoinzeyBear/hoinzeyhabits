package com.example.hoinzeyshabits.model.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import org.joda.time.DateTime

class HabitsWithAchievedDates {

    @Embedded
    var habit: Habit? = null

    @Relation(parentColumn = "habitid", entityColumn = "habitid")
    var achievedHabitDates: List<AchievedHabit> = ArrayList()

    fun achievedOnDate(date: DateTime): Boolean {
        val achievedDate = achievedHabitDates.firstOrNull { it.achievedDate == date }
        return achievedDate?.achieved ?: false
    }
}