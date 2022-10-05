package com.example.hoinzeyshabits.views.composables

import com.example.hoinzeyshabits.model.HabitFrequency

data class HabitFormState(
    val habitId: Int = -1,
    val name: String? = null,
    val habitFrequency: HabitFrequency = HabitFrequency.DAILY,
    val habitFrequencyCount: String = "",
    val formMode: HabitFormMode = HabitFormMode.NEW_HABIT
)

enum class HabitFormMode {
    NEW_HABIT,
    EDIT_HABIT
}