package com.example.shopify.boarding.presentation

import android.R
import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.shopify.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    private lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = findNavController(view)
        binding.forwardImageView.setOnClickListener {
            val extras =  if (resources.configuration.orientation ==  Configuration.ORIENTATION_PORTRAIT){
                FragmentNavigatorExtras(
                    binding.cardProgress to "orangeProgress")
            }else{
                FragmentNavigatorExtras()
            }
            controller.navigate(
                SecondFragmentDirections.actionFavouritesFragmentToAllergiesFragment(),
                extras
            )
        }
    }
}