package com.example.hoinzeyshabits.views.composables

import com.example.hoinzeyshabits.model.pojo.HabitsWithAchievedDates
import org.joda.time.DateTime

data class HabitListState(
    val habitList: List<HabitsWithAchievedDates> = emptyList(),
    val targetDate: DateTime = DateTime.now().withTimeAtStartOfDay()
)
