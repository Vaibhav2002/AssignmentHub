package com.vaibhav.assignmenthub.ui.screens.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.assignmenthub.databinding.ActivitySplashBinding
import com.vaibhav.assignmenthub.ui.screens.authentication.AuthActivity
import com.vaibhav.assignmenthub.ui.screens.homeActivity.MainActivity
import com.vaibhav.assignmenthub.ui.screens.onBoardingScreen.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            when {
                viewModel.isFirstTime() -> startActivity(
                    Intent(
                        this,
                        OnBoardingActivity::class.java
                    )
                )
                viewModel.isUserLoggedIn() -> startActivity(Intent(this, MainActivity::class.java))
                else -> startActivity(Intent(this, AuthActivity::class.java))
            }
            finish()
        }, 2000L)

    }
}