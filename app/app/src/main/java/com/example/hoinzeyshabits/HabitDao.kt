package com.example.hoinzeyshabits

import com.example.hoinzeyshabits.model.Habit

object HabitDao {
    private var habits = arrayListOf<Habit>()

    fun getHabits(): ArrayList<Habit> {
        return habits
    }

    fun addHabit(habit: Habit): Boolean {
        if(containsHabit(habit)) {
            return false
        } else return habits.add(habit)
    }

    fun removeHabit(habit: Habit): Boolean {
        if(containsHabit(habit)) {
            return false
        } else return habits.remove(habit)
    }

    fun getById(id: Int): Habit? {
        return habits.filter { it.habitId == id }.getOrNull(0)
    }

    fun containsHabit(habit: Habit): Boolean = habits.stream().anyMatch { it.name == habit.name }

}