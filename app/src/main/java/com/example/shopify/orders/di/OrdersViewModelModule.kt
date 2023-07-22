package com.example.shopify.orders.di


import com.example.shopify.orders.data.remote.OrdersApiClient
import com.example.shopify.orders.data.remote.OrdersRemoteSource
import com.example.shopify.orders.data.repository.OrdersRepositoryImp
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.orders.domain.usecase.GetCustomerOrdersUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class OrdersViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsOrdersRepository(ordersRepositoryImp: OrdersRepositoryImp): OrdersRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsOrdersApiClient(ordersApiClient: OrdersApiClient): OrdersRemoteSource

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetCustomerOrdersUseCase(ordersRepository: OrdersRepository): GetCustomerOrdersUseCase =
            GetCustomerOrdersUseCase(ordersRepository)
    }
}