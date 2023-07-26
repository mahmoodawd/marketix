package com.example.shopify.checkout.presentation.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.checkout.domain.model.PriceRule
import com.example.shopify.checkout.domain.usecase.account.GetEmailUseCase
import com.example.shopify.checkout.domain.usecase.address.GetAllAddressUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.GetAllDiscountCodeUseCase
import com.example.shopify.checkout.domain.usecase.account.GetUserPhoneUseCase
import com.example.shopify.checkout.domain.usecase.cart.DeleteDraftOrderUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.DeleteDiscountCodeFromDatabaseUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.GetDiscountCodeByIdUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.GetPriceRuleUseCase
import com.example.shopify.checkout.domain.usecase.order.CreateOrderUseCase
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.usecase.customer.GetCustomerIdUseCase
import com.example.shopify.settings.presenation.address.adresses.AllAddressesIntent
import com.example.shopify.utils.divideToPercent
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import com.example.shopify.utils.rounder.roundTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getUserPhoneUseCase: GetUserPhoneUseCase,
    private val getUserEmailUseCase: GetEmailUseCase,
    private val getAllAddressUseCase: GetAllAddressUseCase,
    private val getDiscountCodesUseCase: GetAllDiscountCodeUseCase,
    private val getDiscountCodeByIdUseCase: GetDiscountCodeByIdUseCase,
    private val deleteDiscountCodeFromDatabaseUseCase: DeleteDiscountCodeFromDatabaseUseCase,
    private val getPriceRuleUseCase: GetPriceRuleUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
    private val getCustomerIdUseCase: GetCustomerIdUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val deleteDraftOrderUseCase : DeleteDraftOrderUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<CheckOutState> =
        MutableStateFlow(CheckOutState())
    val state = _state.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun onEvent(intent: CheckOutIntent) {
        when (intent) {
            CheckOutIntent.GetAllAddress -> getAllAddress()
            CheckOutIntent.GetAllDiscountCodes -> getAllDiscountCodes()
            CheckOutIntent.GetUserEmail -> getUserEmail()
            CheckOutIntent.GetUserPhone -> getUserPhone()
            CheckOutIntent.PostOrdersFromCart -> {}
            CheckOutIntent.ValidateDiscountCode -> {validateDiscountCode()}
            is CheckOutIntent.ChooseAddress -> _state.update { it.copy(deliveryAddress = intent.Address) }
            is CheckOutIntent.ChooseDiscountCode -> _state.update { it.copy(discountCode = intent.discountCode) }
            is CheckOutIntent.EmitMessage -> { viewModelScope.launch(ioDispatcher) { _snackBarFlow.emit(intent.message) } }
            is CheckOutIntent.UserEditEmail -> _state.update { it.copy(email = intent.email) }
            is CheckOutIntent.UserEditPhone -> _state.update { it.copy(phone = intent.phone) }
            is CheckOutIntent.UserSubTotal ->{
                _state.update { it.copy(subtotal = intent.subtotal.toDouble()) }
                _state.update { it.copy(totalCost = intent.subtotal.toDouble()) }

            }
            CheckOutIntent.GetPriceRule -> getPriceRule()
            is CheckOutIntent.NewCartItems -> {
                _state.update { it.copy(cartItems = intent.cartItems.cartItems) }
            }
            is CheckOutIntent.CreateOrder ->{

            }

            CheckOutIntent.GetUserId -> {getCustomerIdWithEmail()}
        }

    }

    private fun getUserPhone() {
        viewModelScope.launch(ioDispatcher) {

            getUserPhoneUseCase.execute<String>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> {

                    }

                    is Response.Success -> {
                        _state.update { it.copy(phone = response.data!!) }
                    }
                }
            }


        }
    }

    private fun getUserEmail() {
        viewModelScope.launch(ioDispatcher) {
            getUserEmailUseCase.execute<String>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> {

                    }

                    is Response.Success -> {
                        _state.update { it.copy(email = response.data!!) }
                    }
                }
            }
        }
    }

    private fun getAllAddress() {
        viewModelScope.launch(ioDispatcher) {
            getAllAddressUseCase.execute<List<AddressModel>>(_state.value.customerId).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> {

                    }

                    is Response.Success -> {
                        Log.d("addresses", response.data.toString())
                        _state.update { it.copy(addresses = response.data ?: emptyList()) }
                        if (_state.value.addresses.isNotEmpty()) _state.update {
                            it.copy(
                                deliveryAddress = _state.value.addresses.first { it.isDefault }
                            )
                        }
                    }
                }
            }
        }
    }



    private fun getCustomerIdWithEmail()
    {
        viewModelScope.launch(ioDispatcher) {
            getCustomerIdUseCase.execute<String>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {

                    }

                    is Response.Loading -> {}
                    is Response.Success -> _state.update {
                        Log.d("customerId",response.data.toString())
                        it.copy(customerId = response.data!!)
                    }
                }
            }
        }.invokeOnCompletion {
            onEvent(CheckOutIntent.GetAllAddress)
        }
    }


    private fun getAllDiscountCodes() {
        viewModelScope.launch(ioDispatcher) {
            getDiscountCodesUseCase.execute<List<DiscountCodeModel>>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> {

                    }

                    is Response.Success -> {
                        _state.update { it.copy(discountCodes = response.data!!) }
                    }
                }
            }
        }
    }


    private fun validateDiscountCode()
    {
        viewModelScope.launch(ioDispatcher) {
            getDiscountCodeByIdUseCase.execute<DiscountCodeModel>(_state.value.discountCode?.id.toString()).collectLatest {

                when(it){
                    is Response.Failure ->{
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading ->{}
                    is Response.Success -> {
//                        if (it.data == null || it.data.usageCount < 1)
//                        {   deleteDiscountCodeFromDatabaseUseCase.execute<String>(_state.value.discountCodes.first { it.id == _state.value.discountCode?.id })
//                            _snackBarFlow.emit(R.string.discount_not_valid_any_more)
//
//                            return@collectLatest
//                        }else{

                            onEvent(CheckOutIntent.GetPriceRule)


//                        }

                    }
                }

            }
        }
    }

    private fun getPriceRule()
    {

        viewModelScope.launch(ioDispatcher){
            getPriceRuleUseCase.execute<PriceRule>(_state.value.discountCode!!.priceRuleId).collectLatest { response ->

                when (response){
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (response.data?.type == "percentage"){
                            _state.update { it.copy(totalCost = (_state.value.subtotal + _state.value.subtotal * response.data.discount.toDouble()/100.0).roundTo(2))}
                            _state.update { it.copy(discountValue = _state.value.subtotal * response.data.discount.toDouble()/100.0) }
                        }else{
                            _state.update { it.copy(totalCost = (_state.value.totalCost + response.data!!.discount.toDouble()).roundTo(2))}
                            _state.update { it.copy(discountValue =  response.data!!.discount.toDouble().absoluteValue) }
                        }

                        deleteDiscountCodeFromDatabaseUseCase.execute<String>(_state.value.discountCodes.first { it.id == _state.value.discountCode?.id })
                    }
                }


            }
        }
    }

    private fun getCurrency(id : String,successImplementation : (Response<String>) -> Unit)
    {
        viewModelScope.launch(ioDispatcher){
            readStringFromDataStoreUseCase.execute<String>(id).collectLatest { response->
                when(response)
                {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> TODO()
                    is Response.Success -> {
                       successImplementation(response)
                    }
                }
            }
        }
    }

    fun createOrder(postOrder: PostOrder){
        Timber.e(postOrder.toString())
        viewModelScope.launch(ioDispatcher) {
            createOrderUseCase.execute(postOrder).collectLatest {
                when(it){
                    is Response.Success ->{
                        Timber.e(it.data.toString())
                    }
                    else->{
                        Timber.e(it.error)
                    }
                }
            }
        }
    }


    init {
        onEvent(CheckOutIntent.GetUserId)

        onEvent(CheckOutIntent.GetAllDiscountCodes)
        onEvent(CheckOutIntent.GetUserEmail)
        onEvent(CheckOutIntent.GetUserPhone)
        getCurrency("currency"){ response ->
            _state.update { it.copy(currency = response.data ?: "EGP") }
        }

        getCurrency("currencyFactor"){ response ->
            _state.update { it.copy(currencyFactor = response.data?.toDouble() ?: 1.0) }
            _state.update { it.copy(subtotal = _state.value.subtotal * _state.value.currencyFactor) }
            _state.update { it.copy(subtotal = _state.value.discountValue * _state.value.currencyFactor) }
            _state.update { it.copy(subtotal = _state.value.totalCost * _state.value.currencyFactor) }
        }
    }

}