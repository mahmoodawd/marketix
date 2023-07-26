package com.example.shopify.settings.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shopify.settings.data.dto.location.AddressDto
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(alert: AddressDto)

    @Query("DELETE  FROM AddressDto WHERE latitude = :latitude and longitude = :longitude")
    suspend  fun delete(latitude : Double ,longitude : Double)

    @Query("SELECT * FROM AddressDto WHERE latitude = :latitude and longitude = :longitude ")
    suspend  fun getAddressByLatLong(latitude : Double ,longitude : Double) : List<AddressDto>

    @Update
    suspend fun update(alert: AddressDto)

    @Query("SELECT * FROM AddressDto")
    fun  getAllAddress() : Flow<List<AddressDto>>
}