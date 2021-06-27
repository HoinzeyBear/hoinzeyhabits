package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hoinzeyshabits.HabitDao
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.RecyclerViewClickListener
import com.example.hoinzeyshabits.databinding.FragmentHomeBinding
import com.example.hoinzeyshabits.model.Habit

class HomeFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = HabitViewAdapter(requireContext(), homeViewModel.getHabits())
        binding.habitRecyclerView.adapter = adapter

//        adapter.itemClickListener = AdapterView.OnItemClickListener { adapter, view, position, _ ->
//            val selectedHabit = adapter.getItemAtPosition(position) as Habit
//            val action = HomeFragmentDirections.actionNavHomeToEditHabitFragment(selectedHabit.habitId)
//            findNavController().navigate(action)
//        }
        adapter.itemClickListener = this

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.habitRecyclerView.layoutManager = layoutManager

        binding.addHabit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newHabitFragment)
        }

        Log.d("HOME", homeViewModel.getHabits().toString())

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