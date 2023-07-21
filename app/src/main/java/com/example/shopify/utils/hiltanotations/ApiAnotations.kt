package com.example.shopify.utils.hiltanotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Apis(val apiType: Api)

enum class Api {
    Shopify,
    Countries,
    Currencies,
}