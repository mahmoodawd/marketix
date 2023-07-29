package com.example.shopify.checkout.data.dto

data class Customer(
    val accepts_marketing: Boolean? = null,
    val accepts_marketing_updated_at: String? = null,
    val admin_graphql_api_id: String? = null,
    val created_at: String? = null,
    val currency: String? = null,
    val email: String? = null,
    val email_marketing_consent: EmailMarketingConsent? = null,
    val first_name: Any? = null,
    val id: Long? = null,
    val last_name: Any? = null,
    val last_order_id: Any? = null,
    val last_order_name: Any? = null,
    val marketing_opt_in_level: Any? = null,
    val multipass_identifier: Any? = null,
    val note: Any? = null,
    val orders_count: Int? = null,
    val phone: Any? = null,
    val sms_marketing_consent: Any? = null,
    val state: String? = null,
    val tags: String? = null,
    val tax_exempt: Boolean? = null,
    val tax_exemptions: List<Any>? = null,
    val total_spent: String? = null,
    val updated_at: String? = null,
    val verified_email: Boolean? = null
)