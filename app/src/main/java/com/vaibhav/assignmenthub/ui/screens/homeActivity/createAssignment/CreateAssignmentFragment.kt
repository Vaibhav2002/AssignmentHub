package com.vaibhav.assignmenthub.ui.screens.homeActivity.createAssignment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.databinding.FragmentCreateAssignmentBinding
import com.vaibhav.assignmenthub.utils.Status
import com.vaibhav.assignmenthub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateAssignmentFragment : Fragment(R.layout.fragment_create_assignment) {

    private lateinit var binding: FragmentCreateAssignmentBinding
    private val viewModel: CreateAssignmentViewModel by viewModels()
    private val calendar = Calendar.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateAssignmentBinding.bind(view)
        val user = viewModel.getUserData()
        val classAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.classes,
            android.R.layout.simple_list_item_1
        )
        binding.classSpinner.adapter = classAdapter

        val datePickerListener =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                val formatter = SimpleDateFormat("dd-MM")
                val formatted = formatter.format(Date(year, month, day))
                binding.deadline.text = formatted
            }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.openCalendar.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), datePickerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        viewModel.createAssignmentResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Error -> {
                    binding.loadingAnim.isVisible = false
                    Timber.d(it.message)
                    requireContext().showToast(it.message)
                }
                is Status.Loading -> {
                    binding.loadingAnim.isVisible = true
                }
                is Status.Success -> {
                    binding.loadingAnim.isVisible = false
                    requireContext().showToast("Assignment Created successfully.\nSwipe down to see new assignments")
                    findNavController().popBackStack()
                }
            }
        }

        binding.addAssignmentButton.setOnClickListener {
            val subject = binding.subjectInput.text.toString()
            val title = binding.titleInput.text.toString()
            val deadline = calendar.timeInMillis.toString()
            val forClass = binding.classSpinner.selectedItem as String
            val description = binding.description.text.toString()
            Timber.d(forClass)
            viewModel.onSaveClicked(subject, title, deadline, forClass, description, user!!)
        }
    }

}