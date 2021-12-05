package com.example.hoinzeyshabits.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.RecyclerViewClickListener
import com.example.hoinzeyshabits.RecyclerViewOnLongClickListener
import com.example.hoinzeyshabits.animations.AnimationUtils
import com.example.hoinzeyshabits.model.Habit
import kotlin.properties.Delegates

class HabitViewAdapter
    : RecyclerView.Adapter<HabitViewAdapter.HabitViewHolder>(){

    private var habitList = listOf<Habit>()
    var itemClickListener: RecyclerViewClickListener? = null
    var itemLongClickListener: RecyclerViewOnLongClickListener? = null
    var multiSelectMode = false
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        this.notifyDataSetChanged()
        field = value
    }
    var selectedHabits = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.habit_item, parent, false)
        return HabitViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHabits(habits: List<Habit>) {
        this.habitList = habits
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.populate(habit, position)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    fun notifyHabitChange(habit: Habit) {
        notifyItemChanged(habitList.indexOf(habit))
    }

    fun notifyHabitDelete(habit: Habit) {
        notifyItemRemoved(habitList.indexOf(habit))
    }

    inner class HabitViewHolder(val habitView: View):RecyclerView.ViewHolder(habitView),
        View.OnClickListener,
        View.OnLongClickListener {

        var currentPosition by Delegates.notNull<Int>()
        lateinit var habit: Habit
        var txv_habitName: TextView? = null
        var txv_freqCount: TextView? = null
        var txv_freq: TextView? = null
        var multiSelectView: View? = null

        fun populate(habit: Habit, position: Int) {
            txv_habitName = habitView.findViewById(R.id.txvHabitName)
            txv_freqCount = habitView.findViewById(R.id.txvFrequencyCount)
            txv_freq = habitView.findViewById(R.id.txvFrequency)
            multiSelectView = habitView.findViewById(R.id.multiSelectView)

            txv_habitName?.apply {
                text = habit.name
            }
            txv_freqCount?.apply {
                text = habit.habitFrequencyCount.toString()
            }
            txv_freq?.apply {
                text = habit.habitFrequency.name
            }
            multiSelectView?.apply {
                if(multiSelectMode) {
                    AnimationUtils.animateIntoVisibility(this)
                } else {
                    AnimationUtils.animateOutOfVisibility(this)
                }
            }
            this.currentPosition = position
            this.habit = habit
            habitView.setOnClickListener(this)
            habitView.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            Log.d("ADAPTER", "Clicked on a view")
            if(multiSelectMode) {
                if(selectedHabits.contains(habit.habitId)) {
                    this.multiSelectView?.setBackgroundColor(R.color.purple_700.toInt())
                    selectedHabits.remove(habit.habitId)
                    printSelectedHabits()
                } else {
                    this.multiSelectView?.setBackgroundColor(Color.DKGRAY)
                    selectedHabits.add(habit.habitId)
                    printSelectedHabits()
                }
            } else {
                itemClickListener?.recyclerViewListClicked(view, habit.habitId)
            }
        }

        override fun onLongClick(view: View?): Boolean {
            if(multiSelectMode) {
                multiSelectMode = false
                selectedHabits = mutableListOf()
                printSelectedHabits()
            } else {
                multiSelectMode = true
                selectedHabits.add(habit.habitId)
                this.multiSelectView?.apply{
                    setBackgroundColor(Color.DKGRAY)
                }
                printSelectedHabits()
            }
            Log.d("ADAPTER", "Long clicked item at pos $currentPosition")
            itemLongClickListener?.recyclerViewListLongClicked(view, habit.habitId)
            return true
        }
    }

    fun printSelectedHabits() {
        Log.d("MULTISELECT", "Selected ${selectedHabits.size}")
        val stringBuilder = StringBuilder("[")
        for(id in selectedHabits) {
            stringBuilder.append("$id,")
        }
        stringBuilder.append("]")
        Log.d("MULTISELECT", stringBuilder.toString())
    }
}