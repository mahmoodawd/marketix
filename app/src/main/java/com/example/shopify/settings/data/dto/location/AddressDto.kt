package com.example.shopify.settings.data.dto.location

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AddressDto(
    @PrimaryKey(true)
     var id : Int = 0
    ,var latitude : Double = 0.0
    ,var longitude : Double= 0.0
    ,var city : String = ""
    , var address : String = ""
    )