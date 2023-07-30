package com.example.shopify.orders.data.repository

import com.example.shopify.orders.data.dto.Order
import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.orders.data.remote.FakeRemoteSource
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class OrdersRepositoryImpTest {

    private lateinit var fakeRemoteSource: FakeRemoteSource
    private lateinit var ordersRepositoryImp: OrdersRepositoryImp


    private val orders = listOf(
        Order(
            contact_email = "mohamedadel2323m@gmail.com",
            created_at = "29/7/2023",
            current_total_price = "90.0",
            email = "mohamedadel2323m@gmail.com",
            id = 162466,
            line_items = listOf(),
            name = "Mohamed",
            number = 1315,
            order_number = 1315,
            subtotal_price = "90.0",
            tags = "",
            test = false,
            total_line_items_price = "90.0",
            total_price = "90.0"
        ),
        Order(
            contact_email = "mohamed@gmail.com",
            created_at = "29/7/2023",
            current_total_price = "90.0",
            email = "mohamed@gmail.com",
            id = 162466,
            line_items = listOf(),
            name = "Mohamed",
            number = 1315,
            order_number = 1315,
            subtotal_price = "90.0",
            tags = "",
            test = false,
            total_line_items_price = "90.0",
            total_price = "90.0"
        ),
        Order(
            contact_email = "ahmed@gmail.com",
            created_at = "29/7/2023",
            current_total_price = "90.0",
            email = "ahmed@gmail.com",
            id = 162466,
            line_items = listOf(),
            name = "Mohamed",
            number = 1315,
            order_number = 1315,
            subtotal_price = "90.0",
            tags = "",
            test = false,
            total_line_items_price = "90.0",
            total_price = "90.0"
        ),
    )

    private val ordersResponse = OrdersResponse(orders)

    @Before
    fun setUp() {
        fakeRemoteSource = FakeRemoteSource(ordersResponse)
        ordersRepositoryImp = OrdersRepositoryImp(fakeRemoteSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCustomerOrders_retrieveOrderListWithCustomerEmail() = runTest {
        //When: getCustomerOrders called repo

        //Given: list of 3 orders will return form remote source

        //Then: Assert that the count of the orders returned is only one for certain email.
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            ordersRepositoryImp.getCustomerOrders("mohamedadel2323m@gmail.com")
                .collectLatest { ordersModelResponse ->
                    when (ordersModelResponse) {
                        is Response.Success -> {
                            ordersModelResponse.data?.let {
                                assertThat(it.orders.count(), `is`(1))
                            }
                        }

                        else -> {}
                    }
                }
        }
    }
}