package com.example.shopify.settings.data.remote

import android.net.Uri
import android.util.Log
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.settings.data.dto.address.SendAddressDTO
import com.example.shopify.settings.data.dto.location.CitiesPostRequest
import com.example.shopify.settings.data.mappers.toCities
import com.example.shopify.settings.data.mappers.toCurrenciesModel
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface,
    private val remoteCountriesInterface: RemoteCountriesInterface,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun <T> getAllCurrencies(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllCurrencies().toCurrenciesModel() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getAllCities(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(
                    remoteCountriesInterface.getAllCities(CitiesPostRequest()).toCities() as T
                )
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> updateUserImage(uri: Uri): Response<T> {
        return try {

            val user = firebaseAuth.currentUser
            val imageRef = firebaseStorage.reference.child(user!!.uid)
            val result = imageRef.putFile(uri).await()

            if (result.task.isSuccessful) {
                user.updateProfile(userProfileChangeRequest {
                    photoUri = uri
                }).await()
            }
            Response.Success(true as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        }
    }

    override suspend fun <T> updateUsername(userName: String): Response<T> {
        return try {
            val user = firebaseAuth.currentUser
            if (user!!.displayName == userName){
              return  Response.Success(true as T)
            }
            user.updateProfile(userProfileChangeRequest {
                displayName = userName
            }).await()
            Response.Success(true as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        }
    }


    override suspend fun <T> updateUserPhone(phone: String): Response<T> {
        return try {
            val user = firebaseAuth.currentUser
            firebaseFirestore.collection("users").document(user!!.uid)
                .set(mapOf(user.uid to phone))
            Response.Success(true as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        }
    }

    override suspend fun <T> getUserName(): Response<T> {
        return Response.Success(firebaseAuth.currentUser!!.displayName as T)
    }

    override suspend fun <T> getUserImage(): Response<T> {
        return Response.Success(firebaseAuth.currentUser!!.photoUrl as T)
    }

    override suspend fun <T> getUserPhone(): Response<T> {
        return try {
            val user = firebaseAuth.currentUser
            val phoneTask = firebaseFirestore.collection("users").document(user!!.uid).get().await()


            Response.Success(phoneTask.data!![user.uid].toString() as T)

        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        }
    }

    override suspend fun <T> getAllCustomerAddress(
        customerId: String,

    ): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getAddressesForCustomer(customerId) as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })
    }

    override suspend fun <T> createAddressForCustomer(customerId: String, customerAddress: SendAddressDTO): Flow<Response<T>> {
        return flowOf(try {
            Log.d("createAddress",customerId)
            remoteInterface.createAddressForCustomer(customerId,customerAddress)
            Response.Success("createdSuccessFully" as T)
        } catch (e: Exception) {
            Log.d("createAddress",e.message ?: "")
            Response.Failure(e.message ?: "unknownError")
        })
    }

    override suspend fun <T> deleteAddressForCustomer(
        customerId: String,
        addressId: String
    ): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.deleteAddressForCustomer(customerId,addressId) as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })
    }

    override suspend fun <T> getCustomerId(): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getUserWithEmail(firebaseAuth.currentUser!!.email!!).customers.first().id.toString() as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })
    }


}