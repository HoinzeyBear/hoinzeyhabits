package com.example.hoinzeyshabits.views

import androidx.lifecycle.ViewModel
import com.example.hoinzeyshabits.HabitDao
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import org.joda.time.DateTime

class HomeViewModel : ViewModel() {

    init {
        HabitDao.apply {
            addHabit(Habit(1, "Make my bed", HabitFrequency.DAILY, 1, DateTime.now()))
            addHabit(Habit(2, "Gym session", HabitFrequency.WEEKLY, 4, DateTime.now()))
        }
    }

    fun getHabits(): ArrayList<Habit> {
        return HabitDao.getHabits()
    }
}