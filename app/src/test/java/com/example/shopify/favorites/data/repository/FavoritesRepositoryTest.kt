package com.example.shopify.favorites.data.repository

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.favorites.data.local.FakeFavLocalDataSource
import com.example.shopify.favorites.data.remote.FakeFavRemoteDataSource
import com.example.shopify.favorites.domain.model.FavoritesModel
import com.example.shopify.favorites.domain.repository.FavoritesRepository
import com.example.shopify.util.DispatcherTestingRule
import com.example.shopify.utils.constants.TAG_CART
import com.example.shopify.utils.constants.TAG_FAVORITES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesRepositoryTest {

    @get:Rule
    val mainDispatcherTestingRule = DispatcherTestingRule()

    private lateinit var favoritesRepositoryImpl: FavoritesRepository
    lateinit var remoteDataSource: FakeFavRemoteDataSource
    lateinit var localDataSource: FakeFavLocalDataSource

    private var draftOrderResponse: DraftOrderResponse = DraftOrderResponse(
        listOf(
            DraftOrdersItem(
                id = 1,
                email = "3wd@gmail.com", tags = TAG_FAVORITES,
                line_items = listOf(
                    LineItem(
                        name = "item", properties = listOf(
                            PropertiesItem(value = ""), PropertiesItem(value = "1")
                        )
                    )
                )

            ),
            DraftOrdersItem(
                id = 2,
                email = "user.name@gmail.com", tags = TAG_FAVORITES,
                line_items = listOf(
                    LineItem(
                        name = "item", properties = listOf(
                            PropertiesItem(value = ""), PropertiesItem(value = "1")
                        )
                    )
                )
            ),
            DraftOrdersItem(
                id = 3,
                email = "3wd@gmail.com", tags = TAG_CART,
                line_items = listOf(
                    LineItem(
                        name = "item", properties = listOf(
                            PropertiesItem(value = ""), PropertiesItem(value = "1")
                        )
                    )
                )
            )
        )
    )

    @Before
    fun setup() {


        remoteDataSource = FakeFavRemoteDataSource(
            draftOrderResponse = draftOrderResponse, email = "3wd@gmail.com"
        )
        localDataSource = FakeFavLocalDataSource()
        favoritesRepositoryImpl = FavoritesRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `get favorites should return only items associated with current user email and marked as favorite`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            val favoriteItemsFlow = favoritesRepositoryImpl.getDraftOrders<FavoritesModel>()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

                favoriteItemsFlow.collect {
                    MatcherAssert.assertThat(
                        it.data!!.products.size,
                        CoreMatchers.equalTo(1)
                    )
                }
            }

        }

    @Test
    fun `remove item from favorites should return the list of favorites without the specified item`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            val itemToDelete = draftOrderResponse.draft_orders.first()
            val removeItemFlow =
                favoritesRepositoryImpl.removeDraftOrder(itemToDelete.id.toString())

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

                removeItemFlow.collect {
                    MatcherAssert.assertThat(
                        draftOrderResponse.draft_orders.contains(itemToDelete),
                        CoreMatchers.equalTo(false)
                    )
                }
            }

        }


}

