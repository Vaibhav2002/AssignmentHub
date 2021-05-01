package com.vaibhav.assignmenthub.ui.screens.homeActivity.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.databinding.FragmentProfileBinding
import com.vaibhav.assignmenthub.ui.screens.authentication.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.apply {
            val user = viewModel.getUserData()
            nameText.text = user.name
            emailText.text = user.email
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.logoutbutton.setOnClickListener {
            viewModel.onLogoutButtonPressed()
            requireContext().startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

}