package com.vaibhav.assignmenthub.ui.screens.homeActivity.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.data.models.Assignment
import com.vaibhav.assignmenthub.databinding.FragmentDashboardBinding
import com.vaibhav.assignmenthub.ui.adapters.AssignmentAdapter
import com.vaibhav.assignmenthub.utils.Status
import com.vaibhav.assignmenthub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var onGoingAdapter: AssignmentAdapter
    private lateinit var completedAdapter: AssignmentAdapter
    private val viewModel: DashBoardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)
        Timber.d(viewModel.getUser().toString())
        binding.createAssignmentFab.isVisible = viewModel.getUser().teacher

        onGoingAdapter = AssignmentAdapter {
            navigateToAssignment(it)
        }


        completedAdapter = AssignmentAdapter {
            navigateToAssignment(it)
        }

        binding.profileImg.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment)
        }
        binding.createAssignmentFab.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_createAssignmentFragment)
        }

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            viewModel.fetchAllAssignments()
            binding.refreshLayout.isRefreshing = false
        }
        viewModel.assignments.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    setLoadingAnimVisibility(false)
                    requireContext().showToast(status.message)
                }
                is Status.Loading -> {
                    setLoadingAnimVisibility(true)
                }
                is Status.Success -> {
                    Timber.d(status.data.toString())
                    setLoadingAnimVisibility(false)
                    fillDataIntoAdapter(status.data ?: emptyList())
                }
            }
        }

        binding.ongoingRecycler.apply {
            setHasFixedSize(false)
            adapter = onGoingAdapter
        }

        binding.completedRecycler.apply {
            setHasFixedSize(false)
            adapter = completedAdapter
        }

    }

    private fun setLoadingAnimVisibility(isVisible: Boolean) {
        binding.loadingAnim.isVisible = isVisible

    }

    private fun fillDataIntoAdapter(assignments: List<Assignment>) {
        val onGoingList = assignments.filter {
            it.deadline > System.currentTimeMillis().toString()
        }
        val completedList = assignments.filter {
            it.deadline < System.currentTimeMillis().toString()
        }

        onGoingAdapter.submitList(onGoingList)
        completedAdapter.submitList(completedList)
    }

    private fun navigateToAssignment(assignment: Assignment) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToAssignmentFragment(assignment)
        findNavController().navigate(action)
    }

}