package com.example.shopify.utils.rounder

fun Double.roundTo(n : Int) : Double {
    return "%.${n}f".format(this).toDouble()
}