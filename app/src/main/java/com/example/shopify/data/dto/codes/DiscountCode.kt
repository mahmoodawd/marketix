package com.example.shopify.data.dto.codes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class DiscountCode(
    @PrimaryKey
    var id: Long = 0,
    var code: String,
    var usage_count: Int = 0 ,
    val created_at: String? = null,
    val price_rule_id: Long? = null,
    val updated_at: String? = null ,
)