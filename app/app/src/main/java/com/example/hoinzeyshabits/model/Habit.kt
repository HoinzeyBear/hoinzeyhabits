package com.example.hoinzeyshabits.model

import org.joda.time.DateTime

data class Habit(
    val habitId: Int,
    val name: String,
    val habitFrequency: HabitFrequency,
    val habitFrequencyCount: Int,
    val creationDate: DateTime,
)

enum class HabitFrequency{
    DAILY,
    WEEKLY
}
