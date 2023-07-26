package com.example.shopify.utils

fun Double.divideToPercent(divideTo: Double): Double {
        return if (divideTo == 0.0) 0.0
        else (this / divideTo)
}