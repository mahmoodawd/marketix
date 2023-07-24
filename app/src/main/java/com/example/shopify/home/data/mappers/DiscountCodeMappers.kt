package com.example.shopify.home.data.mappers

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.dto.codes.DiscountCodesResponse
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.home.domain.model.discountcode.DiscountCodesModel

fun DiscountCodesResponse.toDiscountCodeModel() : DiscountCodesModel
{
    return DiscountCodesModel(discountCodes  = discount_codes.map { it.toDiscountCodeModel() })
}

fun DiscountCode.toDiscountCodeModel() : DiscountCodeModel{
    return DiscountCodeModel(id = id, code = code, usageCount = usage_count)
}


fun DiscountCodeModel.toDiscountCode() : DiscountCode{
    return DiscountCode(id = id , code = code, usage_count = usageCount)
}