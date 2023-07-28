package com.example.shopify.splash

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment(private val firebaseAuth: FirebaseAuth) : Fragment() {
    lateinit var binding: FragmentSplashBinding
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.splashAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                if (firebaseAuth.currentUser != null) {
                    navController.navigate(SplashFragmentDirections.actionToHomeGraph())
                } else {
                    navController.navigate(SplashFragmentDirections.actionToOnboardingGraph())
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })
    }
}