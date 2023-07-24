package com.example.shopify.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.home.data.local.DiscountCodesDao
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.settings.data.local.AddressDao

@Database(
    entities = [AddressDto::class,DiscountCode::class],
    version = 1
)
abstract class LocationDatabase : RoomDatabase() {

    abstract val addressDao : AddressDao

    abstract val discountCodesDao : DiscountCodesDao

}