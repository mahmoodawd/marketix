package com.example.shopify.settings.presenation.address.write

data class WriteAddressState(val cities : List<String> = emptyList()
                             , val loading : Boolean = true
                             ,val address : String = ""
,val selectedCity : String = ""


)
