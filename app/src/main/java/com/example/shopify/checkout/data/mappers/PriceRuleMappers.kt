package com.example.shopify.checkout.data.mappers

import com.example.shopify.checkout.data.dto.pricerule.PriceRules
import com.example.shopify.checkout.domain.model.PriceRule

fun PriceRules.toPriceRule() : PriceRule
{

    return PriceRule(price_rule.value,price_rule.value_type)

}