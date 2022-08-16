package com.example.hoinzeyshabits.views.composables

import com.example.hoinzeyshabits.model.HabitFrequency

data class NewHabitState(
    val name: String? = null,
    val habitFrequency: HabitFrequency = HabitFrequency.DAILY,
    val habitFrequencyCount: Int = 1
)
