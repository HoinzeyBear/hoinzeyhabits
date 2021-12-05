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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.*
import com.example.hoinzeyshabits.databinding.FragmentHomeBinding
import com.example.hoinzeyshabits.model.Habit
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(),
    RecyclerViewClickListener,
    RecyclerViewOnLongClickListener {

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).repository)
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setFragmentResultListener("newHabit") { requestKey, bundle ->
            val result = Gson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            habitsViewModel.insert(result)
            Log.d("HOME", "I'm on thread ${Thread.currentThread()}")
            Toast.makeText(requireContext(), "Habit created, good luck !", Toast.LENGTH_LONG).show()
        }

        setFragmentResultListener("editHabit") { requestKey, bundle ->
            val result = Gson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            habitsViewModel.insert(result)
            (binding.habitRecyclerView.adapter as HabitViewAdapter).notifyHabitChange(result)
            Log.d("HOME", "I'm on thread ${Thread.currentThread()}")
            Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_LONG).show()
        }

        setFragmentResultListener("deleteHabit") { requestKey, bundle ->
            val result = Gson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            (binding.habitRecyclerView.adapter as HabitViewAdapter).notifyHabitDelete(result)
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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = HabitViewAdapter()
        binding.habitRecyclerView.adapter = adapter

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        habitsViewModel.habits.observe(viewLifecycleOwner) { habits ->
            // Update the cached copy of the words in the adapter.
            habits.let { adapter.setHabits(it) }
            adapter.notifyDataSetChanged()
        }

        adapter.itemClickListener = this
        adapter.itemLongClickListener = this

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.habitRecyclerView.layoutManager = layoutManager

        binding.addHabit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newHabitFragment)
        }
        Log.d("HOME", habitsViewModel.habits.toString())

        return root
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

    override fun recyclerViewListClicked(v: View?, id: Int) {
        val action = HomeFragmentDirections.actionNavHomeToEditHabitFragment(id)
        findNavController().navigate(action)
    }

    override fun recyclerViewListLongClicked(view: View?, id: Int) {
        runBlocking {
            withContext(Dispatchers.IO){
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