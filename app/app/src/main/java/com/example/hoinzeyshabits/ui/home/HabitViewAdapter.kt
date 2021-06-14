package com.example.hoinzeyshabits.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.model.Habit

class HabitViewAdapter (val context: Context, var habitList: ArrayList<Habit>)
    : RecyclerView.Adapter<HabitViewAdapter.HabitViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.populate(habit, position)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    inner class HabitViewHolder(val habitView: View):RecyclerView.ViewHolder(habitView) {

        var currentPosition: Int = -1
        var habit: Habit? = null
        var txv_habitName: TextView? = null

        fun populate(habit: Habit, position: Int) {
            txv_habitName = habitView.findViewById(R.id.txvHabitName)
            txv_habitName?.apply {
                text = habit.name
            }
            this.currentPosition = position
            this.habit = habit
        }
    }
}