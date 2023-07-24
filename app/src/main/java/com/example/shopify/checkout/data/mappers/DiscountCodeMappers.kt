package com.example.shopify.checkout.data.mappers

import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel


fun DiscountCodeResponse.toDiscountCodeModel() : DiscountCodeModel
{
    return DiscountCodeModel(id = discount_code.id,code = discount_code.code, usageCount = discount_code.usage_count)
}