package com.example.hoinzeyshabits.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.views.composables.NewHabit

/**
 * A simple [Fragment] subclass.
 * Use the [NewHabitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewHabitFragment : Fragment() {

    private val habitsViewModel: HabitsViewModel by viewModels {
        HabitsViewModelFactory((activity?.application as HabitsApplication).habitsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        habitsViewModel.savedState.observe(this) { saved ->
            if (saved) {
                findNavController().navigate(R.id.action_newHabitFragment_to_nav_home)
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                NewHabit()
            }
        }
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