package com.example.hoinzeyshabits.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.RecyclerViewClickListener
import com.example.hoinzeyshabits.RecyclerViewOnLongClickListener
import com.example.hoinzeyshabits.animations.AnimationUtils
import com.example.hoinzeyshabits.model.pojo.HabitsWithAchievedDates
import org.joda.time.DateTime
import kotlin.properties.Delegates

class HabitViewAdapter
    : RecyclerView.Adapter<HabitViewAdapter.HabitViewHolder>(){

    private var habitList = listOf<HabitsWithAchievedDates>()
    lateinit var targetDate: DateTime
    var itemClickListener: RecyclerViewClickListener? = null
    var achievedClickListener: RecyclerViewClickListener? = null
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
    fun setHabits(habits: List<HabitsWithAchievedDates>, date: DateTime) {
        this.habitList = habits
        this.targetDate = date
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.populate(habit, position)
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    fun notifyHabitChange(habitId: Int) {
        notifyItemChanged(habitList.indexOfFirst { habit -> habit.habit?.habitId == habitId })
    }

    fun notifyHabitDelete(habitId: Int) {
        notifyItemRemoved(habitList.indexOfFirst { habit -> habit.habit?.habitId == habitId })
    }

    inner class HabitViewHolder(val habitView: View):RecyclerView.ViewHolder(habitView) {

        var currentPosition by Delegates.notNull<Int>()
        lateinit var habit: HabitsWithAchievedDates
        var cstLayout: ConstraintLayout? = null
        var txv_habitName: TextView? = null
        var txv_freqCount: TextView? = null
        var txv_freq: TextView? = null
        var multiSelectView: View? = null
        var achieved: View? = null

        fun populate(habit: HabitsWithAchievedDates, position: Int) {
            cstLayout = habitView.findViewById(R.id.itemConstraintLayout)
            txv_habitName = habitView.findViewById(R.id.txvHabitName)
            txv_freqCount = habitView.findViewById(R.id.txvFrequencyCount)
            txv_freq = habitView.findViewById(R.id.txvFrequency)
            multiSelectView = habitView.findViewById(R.id.multiSelectView)
            achieved = habitView.findViewById(R.id.achieved)

            cstLayout?.apply{
                setOnClickListener { view ->
                    onClick(view, achievedClickListener, RecyclerViewClickListener.RecyclerViewAction.EDIT)
                }
                setOnLongClickListener { view ->
                    onLongClick(view, itemLongClickListener)
                    true
                }
            }

            txv_habitName?.apply {
                text = habit.habit?.name
            }
            txv_freqCount?.apply {
                text = habit.habit?.habitFrequencyCount.toString()
            }
            txv_freq?.apply {
                text = habit.habit?.habitFrequency?.name
            }
            multiSelectView?.apply {
                if(multiSelectMode) {
                    AnimationUtils.animateIntoVisibility(this)
                } else {
                    AnimationUtils.animateOutOfVisibility(this)
                }
            }
            achieved?.apply {
                if(habit.achievedOnDate(targetDate)) {
                    achieved?.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    achieved?.setBackgroundColor(resources.getColor(R.color.black))
                }
                setOnClickListener { view ->
                    onClick(view, achievedClickListener, RecyclerViewClickListener.RecyclerViewAction.ACHIEVE_GOAL)
                }
            }
            this.currentPosition = position
            this.habit = habit
        }

        fun onClick(view: View?, listener: RecyclerViewClickListener? ,action: RecyclerViewClickListener.RecyclerViewAction) {
            Log.d("ADAPTER", "Clicked on a view")
            if(multiSelectMode) {
                if(selectedHabits.contains(habit.habit?.habitId)) {
                    this.multiSelectView?.setBackgroundColor(R.color.purple_700.toInt())
                    selectedHabits.remove(habit.habit?.habitId)
                    printSelectedHabits()
                } else {
                    this.multiSelectView?.setBackgroundColor(Color.DKGRAY)
                    selectedHabits.add(habit.habit!!.habitId)
                    printSelectedHabits()
                }
            } else {
                listener?.recyclerViewListClicked(view, habit.habit!!.habitId, action)
            }
        }

        fun onLongClick(view: View?, listener: RecyclerViewOnLongClickListener?) {
            if(multiSelectMode) {
                multiSelectMode = false
                selectedHabits = mutableListOf()
                printSelectedHabits()
            } else {
                multiSelectMode = true
                selectedHabits.add(habit.habit!!.habitId)
                this.multiSelectView?.apply{
                    setBackgroundColor(Color.DKGRAY)
                }
                printSelectedHabits()
            }
            Log.d("ADAPTER", "Long clicked item at pos $currentPosition")
            listener?.recyclerViewListLongClicked(view, habit.habit!!.habitId)
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