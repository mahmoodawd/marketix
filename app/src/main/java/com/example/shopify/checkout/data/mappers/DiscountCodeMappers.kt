package com.example.shopify.checkout.data.mappers

import android.util.Log
import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel


fun DiscountCodeResponse.toDiscountCodeModel() : DiscountCodeModel
{
    Log.d("priceRule",discount_code.price_rule_id.toString())
    return DiscountCodeModel(id = discount_code.id,code = discount_code.code, usageCount = discount_code.usage_count, priceRuleId = discount_code.price_rule_id.toString())
}