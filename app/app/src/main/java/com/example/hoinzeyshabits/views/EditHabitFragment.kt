package com.example.hoinzeyshabits.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.hoinzeyshabits.HabitDao
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.databinding.FragmentEditHabitBinding
import com.example.hoinzeyshabits.databinding.FragmentNewHabitBinding
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency


class EditHabitFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditHabitBinding? = null
    private val binding get() = _binding!!
    private var selectedFrequency = HabitFrequency.DAILY

    lateinit var habit: Habit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditHabitBinding.inflate(inflater)

        val habitId = EditHabitFragmentArgs.fromBundle(requireArguments()).habitId
        val habit: Habit? = HabitDao.getById(habitId)

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
            editHabitName.editText?.setText(habit?.name)
            editHabitFrequencyUnit.editText?.setText(habit?.habitFrequencyCount.toString())
            habit?.habitFrequency?.let { habitSpinner.setSelection(it.ordinal) }
        }

        return binding.root
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

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Do nothing
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditHabitFragment()
//                .apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}