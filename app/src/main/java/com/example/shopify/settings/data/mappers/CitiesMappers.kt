package com.example.shopify.settings.data.mappers

import com.example.shopify.settings.data.dto.location.CitiesResponse


fun CitiesResponse.toCities() : List<String>
{
    return data
}