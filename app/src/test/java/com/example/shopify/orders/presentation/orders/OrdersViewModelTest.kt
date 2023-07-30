package com.example.shopify.orders.presentation.orders

import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.CheckGuestStatusUseCase
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.orders.data.repository.FakeAuthRepository
import com.example.shopify.orders.data.repository.FakeOrdersRepository
import com.example.shopify.orders.data.repository.FakeSettingsRepository
import com.example.shopify.orders.domain.model.OrderModel
import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.orders.domain.usecase.GetCustomerOrdersUseCase
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.util.DispatcherTestingRule
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class OrdersViewModelTest {

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var fakeOrdersRepository: OrdersRepository
    private lateinit var fakeSettingsRepository: SettingsRepository
    private lateinit var fakeAuthRepository: AuthRepository
    private lateinit var getCustomerOrdersUseCase: GetCustomerOrdersUseCase
    private lateinit var readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase
    private lateinit var checkGuestStatusUseCase: CheckGuestStatusUseCase

    private val orders = listOf(
        OrderModel(
            "mohamedadel2323m@gmail.com",
            "29/7/2023",
            "90.0",
            "mohamedadel2323m@gmail.com",
            162466,
            listOf(),
            "Mohamed",
            1315,
            1315,
            "90.0",
            "",
            false,
            "90.0",
            "90.0"
        ),
        OrderModel(
            "mohamedadel2323m@gmail.com",
            "29/7/2023",
            "90.0",
            "mohamedadel2323m@gmail.com",
            162466,
            listOf(),
            "Mohamed",
            1315,
            1315,
            "90.0",
            "",
            false,
            "90.0",
            "90.0"
        ),
        OrderModel(
            "ahmed@gmail.com",
            "29/7/2023",
            "90.0",
            "ahmed@gmail.com",
            162466,
            listOf(),
            "Mohamed",
            1315,
            1315,
            "90.0",
            "",
            false,
            "90.0",
            "90.0"
        )
    )

    private val ordersModel = OrdersModel(orders)

    @ExperimentalCoroutinesApi
    @get:Rule
    val dispatcherTestingRule = DispatcherTestingRule()

    @Before
    fun setUp() {
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)
        fakeOrdersRepository = FakeOrdersRepository(ordersModel)
        fakeSettingsRepository = FakeSettingsRepository("EGP", "2.0")
        fakeAuthRepository = FakeAuthRepository(mockFirebaseUser)
        getCustomerOrdersUseCase = GetCustomerOrdersUseCase(fakeOrdersRepository)
        readStringFromDataStoreUseCase = ReadStringFromDataStoreUseCase(fakeSettingsRepository)
        checkGuestStatusUseCase = CheckGuestStatusUseCase(fakeAuthRepository)
        ordersViewModel = OrdersViewModel(
            Dispatchers.Unconfined,
            getCustomerOrdersUseCase,
            readStringFromDataStoreUseCase,
            checkGuestStatusUseCase
        )
    }


    @Test
    fun getCustomerOrders_returnListWithSize2() = runTest {
        //When: getCustomerOrders Called
        ordersViewModel.getCustomerOrders("mohamedadel2323m@gmail.com")
        //Given: There is logged in user "guest mode in disabled"
        backgroundScope.launch {
            ordersViewModel.ordersState.collect()
        }
        //Then: Assert that guest mode is false and list of size 2 returned
        MatcherAssert.assertThat(ordersViewModel.ordersState.value.guestMode, equalTo(false))
        MatcherAssert.assertThat(ordersViewModel.ordersState.value.orders.count(), equalTo(2))

    }


    @Test
    fun readCurrencyFactorFromDataStore() = runTest {
        //When: readCurrencyFactorFromDataStore Called
        ordersViewModel.readCurrencyFactorFromDataStore()
        //Given: fake repo have values "EGP" and "2.0" as currency and exchange rate
        backgroundScope.launch {
            ordersViewModel.ordersState.collect()
        }
        //Then: Assert that exchangeRate is 2.0 and currency is EGP
        MatcherAssert.assertThat(ordersViewModel.ordersState.value.exchangeRate, equalTo(2.0))
        MatcherAssert.assertThat(ordersViewModel.ordersState.value.currency, equalTo("EGP"))

    }

}