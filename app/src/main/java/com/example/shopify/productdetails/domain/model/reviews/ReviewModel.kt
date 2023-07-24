package com.example.shopify.productdetails.domain.model.reviews

import java.util.Date

data class ReviewModel(
    val rating: Float,
    val userName: String,
    val userComment:String,
    val date: String = "15 Jun, 2023",
    val userAvatar: String = "https://e7.pngegg.com/pngimages/340/946/png-clipart-avatar-user-computer-icons-software-developer-avatar-child-face-thumbnail.png"
)