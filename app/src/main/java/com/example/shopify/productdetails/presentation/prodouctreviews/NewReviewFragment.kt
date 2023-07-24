package com.example.shopify.productdetails.presentation.prodouctreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopify.databinding.AddReviewLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewReviewFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "Tag"
    }

    private lateinit var binding: AddReviewLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddReviewLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }
}