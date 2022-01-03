package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.databinding.FragmentEditHabitBinding
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import com.example.hoinzeyshabits.utils.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class EditHabitFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditHabitBinding? = null
    private val binding get() = _binding!!
    private var selectedFrequency = HabitFrequency.DAILY

    private lateinit var habit: Habit

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentEditHabitBinding.inflate(inflater)

        val habitId = EditHabitFragmentArgs.fromBundle(requireArguments()).habitId
        habit = runBlocking {
            withContext(Dispatchers.IO) {
                habitsViewModel.getById(habitId);
            }
        }

        ArrayAdapter.createFromResource(requireContext(),
            R.array.habit_frequency_options,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.habitSpinner.adapter = adapter
            binding.habitSpinner.onItemSelectedListener = this
        }

        binding.apply {
            editHabitName.editText?.setText(habit.name)
            editHabitFrequencyUnit.editText?.setText(habit.habitFrequencyCount.toString())
            habit.habitFrequency.let { habitSpinner.setSelection(it.ordinal) }
        }

        binding.habitEdit.setOnClickListener { saveHabit() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_habit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete_habit -> {
                setFragmentResult("deleteHabit", bundleOf(Pair("habitkey", GsonUtils.dateTimeGson().toJson(habit, Habit::class.java))))
                findNavController().navigate(R.id.action_editHabitFragment_to_nav_home)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        if(adapter?.getItemAtPosition(position) == "Weekly") {
            selectedFrequency = HabitFrequency.WEEKLY
            binding.editHabitFrequencyUnit.hint = getString(R.string.times_per_week)
        } else {
            selectedFrequency = HabitFrequency.DAILY
            binding.editHabitFrequencyUnit.hint = getString(R.string.times_per_day)
        }
    }

    private fun saveHabit() {
        val updatedHabit = habit.copy(
            name = binding.editHabitName.editText?.text.toString(),
            habitFrequency = selectedFrequency,
            habitFrequencyCount = binding.editHabitFrequencyUnit.editText?.text.toString().toInt()
        )

//        habitsViewModel.insert(updatedHabit)
        setFragmentResult("editHabit", bundleOf(Pair("habitkey", GsonUtils.dateTimeGson().toJson(updatedHabit, Habit::class.java))))
        findNavController().navigate(R.id.action_editHabitFragment_to_nav_home)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Do nothing
    }

    companion object {
//        @JvmStatic
//        fun newInstance() =
//            EditHabitFragment()
//                .apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}