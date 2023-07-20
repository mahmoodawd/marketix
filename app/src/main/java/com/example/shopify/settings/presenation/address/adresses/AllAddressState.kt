package com.example.shopify.settings.presenation.address.adresses

import com.example.shopify.settings.domain.model.AddressModel

data class AllAddressState(val addresses : List<AddressModel> = emptyList()
                           ,val loading : Boolean = true
)
