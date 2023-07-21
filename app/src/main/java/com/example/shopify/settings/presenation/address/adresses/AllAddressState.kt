package com.example.shopify.settings.presenation.address.adresses

import com.example.shopify.settings.domain.model.AddressModel

data class AllAddressState(val addresses : List<AddressModel> = emptyList()
                           ,val loading : Boolean = true
                           ,val latitude : Double = 0.0
                           ,val longitude : Double = 0.0
        ,val LocationService: Boolean = false,
)
