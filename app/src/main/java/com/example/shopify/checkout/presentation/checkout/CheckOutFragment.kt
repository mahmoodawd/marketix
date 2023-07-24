package com.example.shopify.checkout.presentation.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.databinding.FragmentCheckOutBinding
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckOutFragment : Fragment() {



    private lateinit var binding: FragmentCheckOutBinding


    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckOutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        binding.editEmailIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToEmailDialogFragment())
        }

        binding.editPhoneIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToPhoneDialogFragment())
        }

        binding.checkOutButton.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToDiscountFragment())
        }
        paypalSetup()

    }

    private fun paypalSetup()
    {

        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "100.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->

                    Log.i("paypalResult", "CaptureOrderResult: $captureOrderResult")
                }
            },

            onCancel = OnCancel {
                Log.d("paypalResult", "Buyer canceled the PayPal experience.")
            }
        )
    }



}