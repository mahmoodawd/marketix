package com.example.shopify.checkout.data.repository

import com.example.shopify.checkout.data.dto.Customer
import com.example.shopify.checkout.data.dto.post.Order
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.data.dto.pricerule.PriceRule
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.checkout.data.local.CartAndCheckOutLocalDataSource
import com.example.shopify.checkout.data.local.FakeLocalDataSource
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.data.remote.FakeRemoteDataSource
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.settings.data.dto.address.AddressDto
import com.example.shopify.util.DispatcherTestingRule
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.AddressModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CartAndCheckOutRepositoryTest {

    @get:Rule
    val mainDispatcherTestingRule = DispatcherTestingRule()

    private lateinit var repository: CartAndCheckoutRepository
    private lateinit var fakeLocalDataSource: CartAndCheckOutLocalDataSource
    private lateinit var fakeRemoteDataSource: CartAndCheckOutRemoteDataSource


    private val address: MutableList<AddressDto> = mutableListOf(
        AddressDto(address1 = "mansoura egypt", customer_id = 123987456, address2 = "10 20" , city = "" , default = true),
        AddressDto(address1 = "cairo egypt", customer_id = 123789456, address2 = "10 20", city = "", default = true),
        AddressDto(address1 = "giza egypt", customer_id = 654789123, address2 = "10 20", city = "", default = true),
        AddressDto(address1 = "alexandria egypt", customer_id = 654789123, address2 = "10 20", city = "", default = true)
    )

    private val discountCodes = mutableListOf(
        com.example.shopify.checkout.data.dto.discountcode.DiscountCode(id = 98723456, code = "abdc543", usage_count = 1),
        com.example.shopify.checkout.data.dto.discountcode.DiscountCode(id = 98723455, code = "ab543dc", usage_count = 3),
        com.example.shopify.checkout.data.dto.discountcode.DiscountCode(id = 98723454, code = "abgh54ab", usage_count = 2),
        com.example.shopify.checkout.data.dto.discountcode.DiscountCode(id = 98723453, code = "abox543c", usage_count = 5),
    )


    private val localDiscountCodes = mutableListOf(
        DiscountCode(id = 98723456, code = "abdc543", usage_count = 1),
       DiscountCode(id = 98723455, code = "ab543dc", usage_count = 3),
      DiscountCode(id = 98723454, code = "abgh54ab", usage_count = 2),
       DiscountCode(id = 98723453, code = "abox543c", usage_count = 5),
    )





    private val orders = mutableListOf(
        DraftOrdersItem(id = 9876985),
        DraftOrdersItem(id = 1234567),
        DraftOrdersItem(id = 1199110),
        DraftOrdersItem(id = 4568246),
        DraftOrdersItem(id = 3497621),
    )

    private val draftOrderResponse = DraftOrderResponse(draft_orders = orders)

    private val cartItems = mutableListOf(
        DraftOrdersItem(
            id = 1,
            email = "abdelrahmanesam20000@gmail.com", tags = "cartItem",
            subtotal_price = "50.0",
            total_tax = "55.0",
             line_items = listOf(
                LineItem(
                    name = "abdelrahman", properties = listOf(
                        PropertiesItem(value = ""), PropertiesItem(value = "1")
                    ), quantity = 2
                )
            )

        ),
        DraftOrdersItem(
            id = 2,
            email = "abdelrahmanesam20000@gmail.com", tags = "cartItem",
            subtotal_price = "50.0",
            total_tax = "55.0",
            line_items = listOf(
                LineItem(
                    name = "abdelrahman", properties = listOf(
                        PropertiesItem(value = ""), PropertiesItem(value = "1")
                    ), quantity = 2
                )
            )
        ),
        DraftOrdersItem(
            id = 3,
            email = "abdelrahmanesam00@gmail.com", tags = "cartItem",
            subtotal_price = "50.0",
            total_tax = "55.0",
            line_items = listOf(
                LineItem(
                    name = "abdelrahman", properties = listOf(
                        PropertiesItem(value = ""), PropertiesItem(value = "1")
                    ), quantity = 2
                )
            )
        ),
        DraftOrdersItem(
            id = 4,
            subtotal_price = "50.0",
            total_tax =   "55.0",
            email = "abdelrahmanesam20000@gmail.com", tags = "cartItem", line_items = listOf(
                LineItem(
                    name = "abdelrahman", properties = listOf(
                        PropertiesItem(value = ""), PropertiesItem(value = "1")
                    ), quantity = 2
                )
            )
        ),
        DraftOrdersItem(
            id = 5,
            subtotal_price = "50.0",
            total_tax =   "55.0",
            email = "abdelrahmanesam20000@gmail.com",
            tags = "cartItem",
            line_items = listOf(
                LineItem(
                    name = "abdelrahman", properties = listOf(
                        PropertiesItem(value = ""), PropertiesItem(value = "1")
                    ), quantity = 2
                )
            ),

            ),
    )

    private val cartItemResponse = DraftOrderResponse(draft_orders = cartItems)

    private val priceRules = mutableListOf(
        PriceRule(id = 97987946541321, value = "40" , value_type = "percentage"),
        PriceRule(id = 78914562212312, value = "70", value_type = "percentage"),
        PriceRule(id = 9798794678, value = "80", value_type = "percentage"),
        PriceRule(id = 798312, value = "50", value_type = "percentage"),
        PriceRule(id = 123456, value = "60", value_type = "percentage"),
    )

    private val customers = mutableListOf(
        Customer(email = "abdelrahman@gmail.com", id = 123987456),
        Customer(email = "abdelrahmanesam20000@gmail.com", id = 123987897456),
        Customer(email = "abdelrahman300000@gmail.com", id = 1239878941327456),
        Customer(email = "abdelrahman400000@gmail.com", id = 1239812317456),
        Customer(email = "abdelrahman50000@gmail.com", id = 1239874675456),

        )

    @Before
    fun setup() {

        fakeLocalDataSource = FakeLocalDataSource(address, localDiscountCodes)
        fakeRemoteDataSource = FakeRemoteDataSource(
            discountCodes = discountCodes,
            addresses = address,
            carItems = cartItemResponse,
            customers = customers,
            priceRules = priceRules,
            draftOrders = orders,
            email = "abdelrahmanesam20000@gmail.com", phone = "01149911456"
        )

        repository = CartAndCheckoutRepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            localDataSource = fakeLocalDataSource
        )
    }


    @Test
    fun `get cart items from remote should return the cart items that matches my email and cartItem tag`() = runTest(
        UnconfinedTestDispatcher()
    ) {

        val cartItemsFlow = repository.getCartItems<CartItems>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            cartItemsFlow.collect { itemsResponse ->
                MatcherAssert.assertThat(
                    itemsResponse.data!!.cartItems.size,
                    CoreMatchers.equalTo(4)
                )
            }
        }

    }


    @Test
    fun `get user email should return email address`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val emailFlow = repository.getUserEmail<String>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            emailFlow.collect { itemsResponse ->
                MatcherAssert.assertThat(
                    itemsResponse.data,
                    CoreMatchers.equalTo("abdelrahmanesam20000@gmail.com")
                )
            }
        }

    }

    @Test
    fun `delete cart item from remote should return successful delete and delete from the cart items`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val firstItem = cartItems.first()
        val deleteItemFlow = repository.deleteItemFromCart<String>(firstItem.id.toString())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            deleteItemFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.data,
                    CoreMatchers.equalTo("successful delete")
                )
                MatcherAssert.assertThat(
                    cartItems.contains(firstItem),
                    CoreMatchers.equalTo(false)
                )

            }
        }
    }


    @Test
    fun `delete cart item from remote with invalid delete should return error`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val firstItem = cartItems.first()
        val deleteItemFlow = repository.deleteItemFromCart<String>("")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            deleteItemFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.error,
                    CoreMatchers.equalTo("For input string: \"\"")
                )
                MatcherAssert.assertThat(
                    cartItems.contains(firstItem),
                    CoreMatchers.equalTo(true)
                )

            }
        }
    }


    @Test
    fun `update item cart with new quantity values should return successful`() =  runTest(UnconfinedTestDispatcher()) {

        val firstItem = cartItems.first()
        MatcherAssert.assertThat(
            firstItem.line_items.first().quantity,
            CoreMatchers.equalTo(2)
        )
        val deleteItemFlow = repository.updateItemFromCart<String>(firstItem.id.toString(),"5")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            deleteItemFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.data,
                    CoreMatchers.equalTo("successful")
                )
                MatcherAssert.assertThat(
                    firstItem.line_items.first().quantity,
                    CoreMatchers.equalTo(5)
                )
            }
        }
    }


    @Test
    fun `update item cart with new quantity and invalid id  should return error`() =  runTest(UnconfinedTestDispatcher()) {

        val firstItem = cartItems.first()
        MatcherAssert.assertThat(
            firstItem.line_items.first().quantity,
            CoreMatchers.equalTo(2)
        )
        val deleteItemFlow = repository.updateItemFromCart<String>("","5")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            deleteItemFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.error,
                    CoreMatchers.equalTo("Collection contains no element matching the predicate.")
                )
            }
        }
    }


    @Test
    fun `get discount code with valid id should return the discount code`() =  runTest(UnconfinedTestDispatcher()) {

        val discountFirst = discountCodes.first()
        val discountFlow = repository.getDiscountCodeById<DiscountCodeModel>(discountFirst.id.toString())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            discountFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.data!!.id,
                    CoreMatchers.equalTo(discountFirst.id)
                )
            }
        }
    }


    @Test
    fun `get discount code with invalid id should return null`() =  runTest(UnconfinedTestDispatcher()) {

        val discountFlow = repository.getDiscountCodeById<DiscountCodeModel>("")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            discountFlow.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.data,
                    CoreMatchers.equalTo(null)
                )
            }
        }
    }


    @Test
    fun `get user phone should return user phone passed`() =  runTest(UnconfinedTestDispatcher()) {

        val userPhone = repository.getUserPhone<String>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userPhone.collectLatest { deleteResponse ->
                MatcherAssert.assertThat(
                    deleteResponse.data,
                    CoreMatchers.equalTo("01149911456")
                )
            }
        }
    }


    @Test
    fun `get price rule with valid id should return successfully`() =  runTest(UnconfinedTestDispatcher()) {

        val priceRule = priceRules.first()
        val priceRuleFlow = repository.getPriceRule<com.example.shopify.checkout.domain.model.PriceRule>(priceRule.id.toString())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            priceRuleFlow.collectLatest { priceRuleResponse ->
                MatcherAssert.assertThat(
                    priceRuleResponse.data!!.discount,
                    CoreMatchers.equalTo(priceRule.value)
                )
            }
        }
    }



    @Test(expected = NullPointerException::class)
    fun `get price rule with invalid id should return error`() =  runTest(UnconfinedTestDispatcher()) {
        val priceRuleFlow = repository.getPriceRule<com.example.shopify.checkout.domain.model.PriceRule>("")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            priceRuleFlow.collect()
        }
    }


    @Test
    fun `get all address with valid customer id should return matched passed addresses`() =  runTest(UnconfinedTestDispatcher()) {
        val addressFlow = repository.getAllCustomerAddress<List<AddressModel>>("123987456")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            addressFlow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data!!.size,
                    CoreMatchers.equalTo(1)
                )
            }
        }
    }


    @Test
    fun `get all address with invalid customer id should return list of zero items`() =  runTest(UnconfinedTestDispatcher()) {
        val addressFlow = repository.getAllCustomerAddress<List<AddressModel>>("454646456464")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            addressFlow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data!!.size,
                    CoreMatchers.equalTo(0)
                )
            }
        }
    }


    @Test
    fun `get customerId with valid email should return successfully`() =  runTest(UnconfinedTestDispatcher()) {
        val flow = repository.getCustomerId<String>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data,
                    CoreMatchers.equalTo("123987897456")
                )
            }
        }
    }


    @Test
    fun `get exchange currency should return successfully`() =  runTest(UnconfinedTestDispatcher()) {
        val flow = repository.exchangeCurrency<Double>(from = "whaterver",to = "whaterver")
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data,
                    CoreMatchers.equalTo(30.0)
                )
            }
        }
    }


    @Test
    fun `delete discount code should return successfull delete`() =  runTest(UnconfinedTestDispatcher()) {
        val discount = discountCodes.first()
        val flow = repository.deleteDiscountCodeFromDatabase<String>(DiscountCode(code = discount.code))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data,
                    CoreMatchers.equalTo("successful delete")
                )
            }
        }
    }



    @Test
    fun `get all discount codes should return list of discount codes`() =  runTest(UnconfinedTestDispatcher()) {
        val flow = repository.getAllDiscountCodes<List<DiscountCodeModel>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data!!.size,
                    CoreMatchers.equalTo(discountCodes.size)
                )
            }
        }
    }


    @Test
    fun `create new order should make our list modified`() =  runTest(UnconfinedTestDispatcher()) {
        val flow = repository.createOrder(PostOrder(Order("new", listOf(com.example.shopify.checkout.data.dto.post.LineItem(
                properties = listOf(
            PropertiesItem(value = ""), PropertiesItem(value = "1")
        ), quantity = 2
        , variant_id = 5565656565)), listOf())))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data!!,
                    CoreMatchers.equalTo("successfully")
                )


                MatcherAssert.assertThat(
                    orders.size,
                    CoreMatchers.equalTo(6)
                )
            }
        }
    }



    @Test
    fun `delete order should make our list size reduced`() =  runTest(UnconfinedTestDispatcher()) {
        val flow = repository.deleteDraftOrder<String>(id = orders.first().id.toString())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            flow.collect{ response ->
                MatcherAssert.assertThat(
                    response.data!!,
                    CoreMatchers.equalTo("successfully")
                )


                MatcherAssert.assertThat(
                    orders.size,
                    CoreMatchers.equalTo(4)
                )
            }
        }
    }



}