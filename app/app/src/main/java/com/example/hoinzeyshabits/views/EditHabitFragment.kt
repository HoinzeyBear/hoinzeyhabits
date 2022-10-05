package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.views.composables.EditHabitScreenRoot

class EditHabitFragment : Fragment() {

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).habitsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        habitsViewModel.savedState.observe(this) { saved ->
            if (saved) {
                findNavController().navigate(R.id.action_editHabitFragment_to_nav_home)
            }
        }

        val habitId = EditHabitFragmentArgs.fromBundle(requireArguments()).habitId

        return ComposeView(requireContext()).apply {
            setContent {
                EditHabitScreenRoot(habitId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_habit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}