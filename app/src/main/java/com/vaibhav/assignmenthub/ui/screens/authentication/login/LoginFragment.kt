package com.vaibhav.assignmenthub.ui.screens.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.databinding.FragmentLoginBinding
import com.vaibhav.assignmenthub.ui.screens.homeActivity.MainActivity
import com.vaibhav.assignmenthub.utils.Status
import com.vaibhav.assignmenthub.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            viewModel.onLoginPressed(email, password)
        }


        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginStatus.collect { status ->
                when (status) {
                    is Status.Error -> {
                        setLoadingAnimVisibility(false)
                        requireContext().showToast(status.message)
                    }
                    is Status.Loading -> setLoadingAnimVisibility(true)
                    is Status.Success -> {
                        setLoadingAnimVisibility(false)
                        requireContext().showToast("Successfully logged in")
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