package com.example.shopify.productdetails.presentation.prodouctreviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopify.databinding.FragmentReviewsBinding


class ReviewsFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addReviewFab.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val addNewAlertBottomSheet = NewReviewFragment()
        addNewAlertBottomSheet.show(
            requireActivity().supportFragmentManager,
            NewReviewFragment.TAG
        )
    }
}