package com.vaibhav.assignmenthub.ui.screens.homeActivity.assignment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.data.models.Submission
import com.vaibhav.assignmenthub.databinding.FragmentAssignmentBinding
import com.vaibhav.assignmenthub.ui.adapters.SubmissionAdapter
import com.vaibhav.assignmenthub.utils.Status
import com.vaibhav.assignmenthub.utils.getDateFromMillis
import com.vaibhav.assignmenthub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AssignmentFragment : Fragment(R.layout.fragment_assignment) {

    private lateinit var binding: FragmentAssignmentBinding
    private val viewModel: AssignmentViewModel by viewModels()
    private val args by navArgs<AssignmentFragmentArgs>()
    private lateinit var submissionAdapter: SubmissionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAssignmentBinding.bind(view)
        val assignment = args.assignment
        viewModel.getAllSubmissions(assignment.id)

        Timber.d(viewModel.getUser().toString())
//        binding.submissionFab.isVisible = !viewModel.getUser().teacher

        submissionAdapter = SubmissionAdapter {

        }


        binding.apply {
            subject.text = assignment.subject
            title.text = assignment.name
            deadline.text = getDateFromMillis(assignment.deadline.toLong())
            description.text = assignment.description

        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.submissionFab.setOnClickListener {
            viewModel.onSubmissionFabPressed(assignment.id)
        }

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            viewModel.getAllSubmissions(assignment.id)
            binding.refreshLayout.isRefreshing = false
        }
        binding.studentsRecycler.apply {
            setHasFixedSize(false)
            adapter = submissionAdapter
        }
        viewModel.submissions.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    Timber.d(status.message)
                    requireContext().showToast(status.message)
                    binding.loadingAnim.isVisible = false
                }
                is Status.Loading -> {
                    binding.loadingAnim.isVisible = true
                }
                is Status.Success -> {
                    binding.loadingAnim.isVisible = false
                    submissionAdapter.submitList(status.data ?: emptyList())
                    showFabIfRequired(status.data ?: emptyList())
                }
            }
        }

        viewModel.postSubmission.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    Timber.d(status.message)
                    requireContext().showToast(status.message)
                    binding.loadingAnim.isVisible = false
                }
                is Status.Loading -> {
                    binding.loadingAnim.isVisible = true
                }
                is Status.Success -> {
                    binding.loadingAnim.isVisible = false
                    requireContext().showToast("Assignment Submission done successfully")
                    viewModel.getAllSubmissions(assignment.id)
                }
            }
        }
    }


    private fun showFabIfRequired(submissions: List<Submission>) {
        val user = viewModel.getUser()
        val submission = submissions.find {
            it.studentId == user.id
        }
        binding.submissionFab.isVisible = submission == null && !user.teacher
    }
}