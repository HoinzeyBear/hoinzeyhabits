package com.example.hoinzeyshabits.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import org.joda.time.DateTime

class HomeViewModel : ViewModel() {

    fun getHabits(): ArrayList<Habit> {
        return arrayListOf(
            Habit(1, "Exercise", HabitFrequency.DAILY, 1, DateTime.now()),
            Habit(2, "Sleep 8 hours", HabitFrequency.DAILY, 1 , DateTime.now()))
    }
}