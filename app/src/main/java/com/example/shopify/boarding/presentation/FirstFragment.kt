package com.example.shopify.boarding.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.shopify.R
import com.example.shopify.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private lateinit var binding: FragmentFirstBinding
    private lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
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
                FirstFragmentDirections.actionFirstFragmentToSecondFragment(), extras
            )

        }

        binding.skipTextView.setOnClickListener {
            controller.navigate(FirstFragmentDirections.actionToAuthenticationGraph())
        }
    }
}