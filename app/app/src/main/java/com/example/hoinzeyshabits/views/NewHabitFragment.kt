package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.databinding.FragmentNewHabitBinding
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import com.example.hoinzeyshabits.utils.GsonUtils

/**
 * A simple [Fragment] subclass.
 * Use the [NewHabitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewHabitFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentNewHabitBinding? = null
    private val binding get() = _binding!!

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).repository)
    }

    private var selectedFrequency = HabitFrequency.DAILY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewHabitBinding.inflate(inflater)

        ArrayAdapter.createFromResource(requireContext(),
            R.array.habit_frequency_options,
            R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.habitSpinner.adapter = adapter
            binding.habitSpinner.onItemSelectedListener = this
        }

        binding.habitSave.setOnClickListener { saveHabit() }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun saveHabit() {
        val newHabit = Habit(
            name = binding.newHabitName.editText?.text.toString(),
            habitFrequency = selectedFrequency,
            habitFrequencyCount = binding.newHabitFrequencyUnit.editText?.text.toString().toInt())

        setFragmentResult("newHabit", bundleOf(Pair("habitkey", GsonUtils.dateTimeGson().toJson(newHabit, Habit::class.java))))
        findNavController().navigate(R.id.action_newHabitFragment_to_nav_home)
    }

    override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        if(adapter?.getItemAtPosition(position) == "Weekly") {
            selectedFrequency = HabitFrequency.WEEKLY
            binding.newHabitFrequencyUnit.hint = getString(R.string.times_per_week)
        } else {
            selectedFrequency = HabitFrequency.DAILY
            binding.newHabitFrequencyUnit.hint = getString(R.string.times_per_day)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //do nothing
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NewHabitFragment()
//                .apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}