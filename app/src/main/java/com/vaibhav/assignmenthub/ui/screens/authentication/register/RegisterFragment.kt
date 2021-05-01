package com.vaibhav.assignmenthub.ui.screens.authentication.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.databinding.FragmentRegisterBinding
import com.vaibhav.assignmenthub.ui.screens.homeActivity.MainActivity
import com.vaibhav.assignmenthub.utils.Status
import com.vaibhav.assignmenthub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            val name = binding.usernameInput.text.toString()
            val rollNo = binding.rollNoInput.text.toString()
            val isTeacher = binding.teacherCheckBox.isChecked
            val ofClass = if (isTeacher) "" else binding.classSpinner.selectedItem.toString()
            viewModel.onRegisterPressed(email, password, name, rollNo, ofClass, isTeacher)
        }

        binding.teacherCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.rollNoInput.hint = if (isChecked) "Teacher Id" else "Roll no."
            binding.classLayout.isVisible = !isChecked
        }

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.classes,
            R.layout.spinner_item
        )
        binding.classSpinner.adapter = spinnerAdapter





        binding.goToLogin.setOnClickListener {
            findNavController().popBackStack()
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.registerStatus.collect { status ->
                when (status) {
                    is Status.Error -> {
                        setLoadingAnimVisibility(false)
                        requireContext().showToast(status.message)
                    }
                    is Status.Loading -> setLoadingAnimVisibility(true)
                    is Status.Success -> {
                        setLoadingAnimVisibility(false)
                        requireContext().showToast("Account created successfully")
                        navigateToDashBoard()
                    }
                }
            }
        }


    }

    private fun setLoadingAnimVisibility(isVisible: Boolean) {
        binding.loadingAnim.isVisible = isVisible

    }


    private fun navigateToDashBoard() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

}

