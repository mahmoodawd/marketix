package com.example.shopify.home.data.mappers

import android.util.Log
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.dto.codes.DiscountCodesResponse
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.home.domain.model.discountcode.DiscountCodesModel

fun DiscountCodesResponse.toDiscountCodeModel() : DiscountCodesModel
{
    return DiscountCodesModel(discountCodes  = discount_codes.map { it.toDiscountCodeModel() })
}

fun DiscountCode.toDiscountCodeModel() : DiscountCodeModel{
    return DiscountCodeModel(id = id, code = code, usageCount = usage_count, priceRuleId = price_rule_id.toString())
}


fun DiscountCodeModel.toDiscountCode() : DiscountCode{
    return DiscountCode(id = id , code = code, usage_count = usageCount, price_rule_id = priceRuleId.toLong())
}