package com.example.shopify.home.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.settings.data.dto.location.AddressDto
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscountCodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(code: DiscountCode)

    @Query("DELETE  FROM DiscountCode WHERE code = :code")
    suspend  fun delete(code: String)

    @Query("SELECT * FROM DiscountCode")
    fun  getAllDiscountCodes() : Flow<List<DiscountCode>>
}