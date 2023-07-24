package com.example.shopify.orders.domain.usecase

import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.orders.data.dto.post.PostOrderResponse
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(private val ordersRepository: OrdersRepository){
    suspend fun execute(postOrder: PostOrder): Flow<Response<PostOrderResponse>> {
        return ordersRepository.createOrder(postOrder)
    }
}