package com.example.shopify.settings.data.mappers

import android.location.Address
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.settings.domain.model.AddressModel


fun AddressDto.toAddressModel() : AddressModel
{
    return AddressModel(latitude, longitude, city, address)
}


fun AddressModel.toAddressDto() : AddressDto
{
    return  AddressDto(latitude = latitude, longitude =  longitude,city =  city, address = address)
}