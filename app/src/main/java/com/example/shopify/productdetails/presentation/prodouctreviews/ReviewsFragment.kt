package com.example.shopify.productdetails.presentation.prodouctreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.FragmentReviewsBinding
import com.example.shopify.productdetails.domain.model.reviews.ReviewModel


class ReviewsFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding
    private val reviewsAdapter: ProductReviewsAdapter by lazy {
        ProductReviewsAdapter()
    }
    private val navController: NavController by lazy {
        this@ReviewsFragment.findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        binding.backImageView.setOnClickListener { navController.popBackStack() }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addReviewFab.setOnClickListener {
            showBottomSheet()
        }
        val reviewList = listOf(
            ReviewModel(
                rating = 4.5f,
                userName = "John Doe",
                userComment = "Great product! I've been using it for a few weeks now and it works really well.",
                date = "20 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=1"
            ),
            ReviewModel(
                rating = 3.0f,
                userName = "Jane Smith",
                userComment = "The product is okay, but I expected better results. It's not worth the price.",
                date = "18 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=2"
            ),
            ReviewModel(
                rating = 5.0f,
                userName = "Samuel Lee",
                userComment = "I absolutely love this product! It's made a huge difference in my daily routine.",
                date = "16 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=3"
            ),
            ReviewModel(
                rating = 2.5f,
                userName = "Sarah Johnson",
                userComment = "I'm not impressed with this product. It didn't meet my expectations.",
                date = "14 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=4"
            ),
            ReviewModel(
                rating = 4.0f,
                userName = "Mike Williams",
                userComment = "This product is pretty good. It's not perfect, but it gets the job done.",
                date = "12 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=5"
            ),
            ReviewModel(
                rating = 4.0f,
                userName = "Mike Williams",
                userComment = "This product is pretty good. It's not perfect, but it gets the job done.",
                date = "12 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=5"
            ),
            ReviewModel(
                rating = 4.0f,
                userName = "Mike Williams",
                userComment = "This product is pretty good. It's not perfect, but it gets the job done.",
                date = "12 Jul, 2023",
                userAvatar = "https://i.pravatar.cc/150?img=5"
            ),

            )

        binding.adapter = reviewsAdapter

        binding.reviewsNumber.text =
            StringBuilder().append(reviewList.size, " ", getString(R.string.product_reviews))
        reviewsAdapter.submitList(reviewList)
        val ratingAvg = reviewList.sumOf { it.rating.toDouble() }.div(reviewList.size).toFloat()
        binding.totalRating.rating = ratingAvg
    }

    private fun showBottomSheet() {
        val addNewAlertBottomSheet = NewReviewFragment()
        addNewAlertBottomSheet.show(
            requireActivity().supportFragmentManager,
            NewReviewFragment.TAG
        )
    }
}