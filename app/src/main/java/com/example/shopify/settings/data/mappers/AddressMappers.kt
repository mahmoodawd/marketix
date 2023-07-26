package com.example.shopify.settings.data.mappers

import com.example.shopify.settings.data.dto.address.AddressDto
import com.example.shopify.settings.data.dto.address.SendAddress
import com.example.shopify.settings.data.dto.address.SendAddressDTO
import com.example.shopify.settings.domain.model.AddressModel


fun AddressDto.toAddressModel() : AddressModel
{
    return AddressModel(addressId =  id.toString()
        ,address2!!.split(" ").first().toDouble()
        , address2.split(" ")[1].toDouble()
        , city!!, address1!!,
        isDefault = default!!
        )
}


fun AddressModel.toAddressDto() : AddressDto
{
    return  AddressDto(city =  city, address1 = address, address2 ="$latitude $longitude",id = addressId!!.toLong())
}


fun AddressModel.toSendAddressDto() : SendAddressDTO
{
    return  SendAddressDTO(customer_address = SendAddress(address1 = address , address2 = "$latitude $longitude",city))
}