package com.example.shopify.settings.presenation.address.adresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.FragmentAllAddressesBinding
import com.example.shopify.utils.recycler.swipeRecyclerItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllAddressesFragment : Fragment() {

    private lateinit var binding: FragmentAllAddressesBinding

    private lateinit var navController: NavController


    private val viewModel: AllAddressesViewModel by viewModels()


    private val addressesRecyclerAdapter by lazy {
        AddressesRecyclerAdapter { address ->
            navController.navigate(AllAddressesFragmentDirections.actionAllAddressesFragmentToWriteAddressFragment(address.latitude.toString(),address.longitude.toString()))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllAddressesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setUpAddressesRecyclerView()
        stateObserver()

        binding.fabPlusButton.setOnClickListener {
            navController.navigate(AllAddressesFragmentDirections.actionAllAddressesFragmentToAddressFragment(0.0.toString(),0.0.toString()))
        }
    }

    private fun setUpAddressesRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.addressesRecyclerView.swipeRecyclerItemListener { viewHolder ->
            viewModel.onEvent(AllAddressesIntent.DeleteAddress(viewHolder.adapterPosition))
        }
        binding.addressesRecyclerView.layoutManager = linearLayoutManager
        binding.addressesRecyclerView.adapter = addressesRecyclerAdapter
    }

    private fun stateObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    if (state.addresses.isNotEmpty()) {
                        addressesRecyclerAdapter.submitList(state.addresses)
                    }
                }
            }
        }
    }

}