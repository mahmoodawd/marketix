package com.example.shopify.settings.data.mappers

import com.example.shopify.settings.data.dto.currencies.CurrenciesResponse
import com.example.shopify.settings.data.dto.currencies.Currency
import com.example.shopify.settings.domain.model.CurrenciesModel
import com.example.shopify.settings.domain.model.CurrencyModel


fun CurrenciesResponse.toCurrenciesModel() : CurrenciesModel
{
return CurrenciesModel(currencies = currencies.map { it.toCurrencyModel() })
}


fun Currency.toCurrencyModel() : CurrencyModel
{
return  CurrencyModel(currency = currency)
}