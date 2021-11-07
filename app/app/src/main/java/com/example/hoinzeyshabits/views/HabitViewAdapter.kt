package com.example.hoinzeyshabits.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.RecyclerViewClickListener
import com.example.hoinzeyshabits.model.Habit
import kotlin.properties.Delegates

class HabitViewAdapter ()
    : RecyclerView.Adapter<HabitViewAdapter.HabitViewHolder>(){

    var itemClickListener: RecyclerViewClickListener? = null
    private var habitList = listOf<Habit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(itemView)
    }

    fun setHabits(habits: List<Habit>) {
        this.habitList = habits
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.populate(habit, position)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    inner class HabitViewHolder(val habitView: View):RecyclerView.ViewHolder(habitView), View.OnClickListener {

        var currentPosition by Delegates.notNull<Int>()
        lateinit var habit: Habit
        var txv_habitName: TextView? = null

        fun populate(habit: Habit, position: Int) {
            txv_habitName = habitView.findViewById(R.id.txvHabitName)
            txv_habitName?.apply {
                text = habit.name
            }
            this.currentPosition = position
            this.habit = habit
            habitView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d("ADAPTER", "Clicked on a view")
            itemClickListener?.recyclerViewListClicked(view, habit.habitId)
        }
    }
}