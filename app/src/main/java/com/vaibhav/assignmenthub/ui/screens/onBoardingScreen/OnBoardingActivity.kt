package com.vaibhav.assignmenthub.ui.screens.onBoardingScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.assignmenthub.R
import com.vaibhav.assignmenthub.data.models.OnBoarding
import com.vaibhav.assignmenthub.databinding.ActivityOnBoardingBinding
import com.vaibhav.assignmenthub.ui.screens.authentication.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingList = mutableListOf<OnBoarding>()
        onboardingList.add(
            OnBoarding(
                R.drawable.onboarding1,
                getString(R.string.onboarding1),
            )
        )
        onboardingList.add(
            OnBoarding(
                R.drawable.onboarding2,
                getString(R.string.onboarding2),

                )
        )
        onboardingList.add(
            OnBoarding(
                R.drawable.onboarding3,
                getString(R.string.onboarding3),
                true
            )
        )
        val onBoardingAdapter = OnBoardingAdapter(onboardingList) {
            viewModel.setOnBoardingComplete()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        binding.onboardingViewpager.adapter = onBoardingAdapter
        binding.wormDotsIndicator.setViewPager2(binding.onboardingViewpager)
    }
}