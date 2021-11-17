package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.RecyclerViewClickListener
import com.example.hoinzeyshabits.databinding.FragmentHomeBinding
import com.example.hoinzeyshabits.model.Habit
import com.google.gson.Gson

class HomeFragment : Fragment(), RecyclerViewClickListener {

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).repository)
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("newHabit") { requestKey, bundle ->
            val result = Gson().fromJson(bundle.getString("habitkey"), Habit::class.java)
            habitsViewModel.insert(result)
        }
    }

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

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.habitRecyclerView.layoutManager = layoutManager

        binding.addHabit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newHabitFragment)
        }

        Log.d("HOME", habitsViewModel.habits.toString())

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun recyclerViewListClicked(v: View?, id: Int) {
        val action = HomeFragmentDirections.actionNavHomeToEditHabitFragment(id)
        findNavController().navigate(action)
    }
}