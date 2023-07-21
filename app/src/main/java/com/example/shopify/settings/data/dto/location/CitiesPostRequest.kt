package com.example.shopify.settings.data.dto.location

import com.google.gson.annotations.SerializedName

data class CitiesPostRequest( @SerializedName("country")
                              val country: String = "egypt")