package com.example.shopify.data.dto

data class DraftOrderResponse(
	val draft_orders: List<DraftOrdersItem>
)

data class DraftOrdersItem(
	val note: Any = "",
	val applied_discount: Any = "",
	val created_at: String = "",
	val billing_address: Any = "",
	val taxes_included: Boolean = false,
	val line_items: List<LineItem> = emptyList(),
	val payment_terms: Any = "",
	val updated_at: String = "",
	val tax_lines: List<TaxLine> = emptyList(),
	val currency: String = "",
	val id: Long = 0L,
	val shipping_address: Any = "",
	val email: Any = "",
	val subtotal_price: String = "",
	val total_price: String = "",
	val tax_exempt: Boolean = false,
	val invoice_sent_at: Any = "",
	val total_tax: String = "",
	val tags: String = "",
	val completed_at: Any = "",
	val note_attributes: List<Any> = emptyList(),
	val admin_graphql_api_id: String = "",
	val name: String = "",
	val shipping_line: Any = "",
	val order_id: Any = "",
	val invoice_url: String = "",
	val status: String = "",
	val customer: Customer = Customer()
)

data class LineItem(
	val variant_title: String = "",
	val quantity: Int = 0,
	val taxable: Boolean = false,
	val gift_card: Boolean = false,
	val fulfillment_service: String = "",
	val applied_discount: Any = "",
	val requires_shipping: Boolean = false,
	val custom: Boolean = false,
	val title: String = "",
	val variant_id: Long = 0L,
	val tax_lines: List<TaxLine> = emptyList(),
	val vendor: String = "",
	val price: String = "",
	val product_id: Long = 0L,
	val admin_graphql_api_id: String = "",
	val name: String = "",
	val id: Long = 0L,
	val sku: String = "",
	val grams: Int = 0,
	val properties: List<Any> = emptyList()
)

data class TaxLine(
	val rate: Any = "",
	val price: String = "",
	val title: String = ""
)

data class EmailMarketingConsent(
	val consent_updated_at: Any = "",
	val state: String = "",
	val opt_in_level: String = ""
)

data class Customer(
	val note: Any = "",
	val last_order_name: Any = "",
	val created_at: String = "",
	val multipass_identifier: Any = "",
	val accepts_marketing_updated_at: String = "",
	val updated_at: String = "",
	val accepts_marketing: Boolean = false,
	val currency: String = "",
	val id: Long = 0L,
	val state: String = "",
	val marketing_opt_in_level: Any = "",
	val first_name: String = "",
	val email: String = "",
	val total_spent: String = "",
	val last_order_id: Any = "",
	val tax_exempt: Boolean = false,
	val email_marketing_consent: EmailMarketingConsent = EmailMarketingConsent(),
	val last_name: Any = "",
	val verified_email: Boolean = false,
	val tags: String = "",
	val orders_count: Int = 0,
	val sms_marketing_consent: Any = "",
	val phone: Any = "",
	val admin_graphql_api_id: String = "",
	val tax_exemptions: List<Any> = emptyList()
)