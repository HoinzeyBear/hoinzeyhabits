package com.example.hoinzeyshabits.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.*
import com.example.hoinzeyshabits.databinding.FragmentHomeBinding
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.utils.Conversion
import com.example.hoinzeyshabits.utils.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime

class HomeFragment(var date: DateTime = DateTime()) : Fragment(),
    RecyclerViewClickListener,
    RecyclerViewOnLongClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).habitsRepository)
    }
    private var _binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setFragmentResultListener("newHabit") { requestKey, bundle ->
            val result = GsonUtils.dateTimeGson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            habitsViewModel.insert(result)
            Log.d("HOME", "I'm on thread ${Thread.currentThread()}")
            Log.d("HOME", "${result.creationDate}")
            Toast.makeText(requireContext(), "Habit created, good luck !", Toast.LENGTH_LONG).show()
        }

        setFragmentResultListener("editHabit") { requestKey, bundle ->
            val result = GsonUtils.dateTimeGson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            habitsViewModel.insert(result)
            (binding.habitRecyclerView.adapter as HabitViewAdapter).notifyHabitChange(result.habitId)
            Log.d("HOME", "I'm on thread ${Thread.currentThread()}")
            Log.d("HOME", "${result.creationDate}")
            Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_LONG).show()
        }

        setFragmentResultListener("deleteHabit") { requestKey, bundle ->
            val result = GsonUtils.dateTimeGson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            (binding.habitRecyclerView.adapter as HabitViewAdapter).notifyHabitDelete(result.habitId)
            habitsViewModel.delete(result)
            Log.d("HOME", "I'm on thread ${Thread.currentThread()}")
            Toast.makeText(requireContext(), "Habit deleted", Toast.LENGTH_LONG).show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (binding.habitRecyclerView.adapter as HabitViewAdapter).multiSelectMode = false
            removeDeleteOptionFromToolbar()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HOMEFRAGMENT", "Creating the homeFragment View for ${Conversion.ISO8601.print(this.date.withTimeAtStartOfDay())}")

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = HabitViewAdapter()
        binding.habitRecyclerView.adapter = adapter

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        habitsViewModel.habits.observe(viewLifecycleOwner) {
            adapter.setHabits(it, habitsViewModel.targetDate)
            adapter.notifyDataSetChanged()
        }

        habitsViewModel.printHabitsForDisplay()

        adapter.itemClickListener = this
        adapter.itemLongClickListener = this
        adapter.achievedClickListener = this

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.habitRecyclerView.layoutManager = layoutManager

        binding.addHabit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newHabitFragment)
        }

        binding.previousDate.setOnClickListener {
            habitsViewModel.targetDate = habitsViewModel.targetDate.minusDays(1)
            refreshAdaptersHabits(binding.habitRecyclerView.adapter as HabitViewAdapter)
            Log.d("MAINMENU", "Moving to previous date ${Conversion.ISO8601.print(habitsViewModel.targetDate)}")
        }

        binding.nextDate.setOnClickListener {
            habitsViewModel.targetDate = habitsViewModel.targetDate.plusDays(1)
            refreshAdaptersHabits(binding.habitRecyclerView.adapter as HabitViewAdapter)
            Log.d("MAINMENU", "Moving to next date ${Conversion.ISO8601.print(habitsViewModel.targetDate)}")
        }

        Log.d("HOME", habitsViewModel.habits.toString())
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdaptersHabits(adapter: HabitViewAdapter) {
        with(adapter) {
            adapter.targetDate = habitsViewModel.targetDate
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_habit_main_menu -> {
                val habitViewAdapter = binding.habitRecyclerView.adapter as HabitViewAdapter
                val selectedHabits =
                    habitViewAdapter.selectedHabits
                Log.d("MAINMENU", "Deleting $selectedHabits")
                for(id in selectedHabits) {
                    habitsViewModel.delete(id)
                }
                habitViewAdapter.multiSelectMode = false
                removeDeleteOptionFromToolbar()
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun recyclerViewListClicked(v: View?, id: Int, action: RecyclerViewClickListener.RecyclerViewAction) {
        when(action) {
            RecyclerViewClickListener.RecyclerViewAction.EDIT -> {
                Log.d("HOME", "Selected habit $id to view")
                val destination = HomeFragmentDirections.actionNavHomeToEditHabitFragment(id)
                findNavController().navigate(destination)
            }
            RecyclerViewClickListener.RecyclerViewAction.ACHIEVE_GOAL -> {
                if(habitsViewModel.targetDate.isAfterNow) {
                    Toast.makeText(activity, R.string.cant_achieve_in_future, Toast.LENGTH_LONG)
                        .show()
                } else {
                    val markAsAchieved = !habitsViewModel.habits.value?.first { it ->
                        it.habit?.habitId == id }!!.achievedOnDate(habitsViewModel.targetDate)

                    habitsViewModel.insert(AchievedHabit(id, habitsViewModel.targetDate.withTimeAtStartOfDay(), markAsAchieved))
                    refreshAdaptersHabits(binding.habitRecyclerView.adapter as HabitViewAdapter)
                    Log.d("HOME", "Selected habit $id to achieve for ${DateTime.now().withTimeAtStartOfDay()}")
                }
            }
        }
    }

    override fun recyclerViewListLongClicked(view: View?, id: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("HOME", "Selected habit ${habitsViewModel.getById(id)}")
            }
        }

        removeDeleteOptionFromToolbar()
    }

    private fun removeDeleteOptionFromToolbar() {
        val menu = (activity as MainActivity).menu
        if(menu != null) {
            val findItem = menu.findItem(R.id.delete_habit_main_menu)
            if(findItem != null) {
                findItem.isVisible = findItem.isVisible.not()
            }
        }
    }
}