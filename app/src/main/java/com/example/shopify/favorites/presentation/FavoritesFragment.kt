package com.example.shopify.favorites.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.FragmentFavoritesBinding
import com.example.shopify.search.presentation.SearchItemsAdapter
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.gone
import com.example.shopify.utils.ui.goneIf
import com.example.shopify.utils.ui.visible
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoritesFragment(
    private val connectivityObserver: ConnectivityObserver
) : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    lateinit var binding: FragmentFavoritesBinding

    private val navController by lazy {
        this@FavoritesFragment.findNavController()
    }

    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(
            onItemClick = { productId ->
                navToDetails(productId)
            },
            onDeleteClick = { id, position ->
                showConfirmDeleteDialog(id, position)
            })
    }

    private val searchAdapter: SearchItemsAdapter by lazy {
        SearchItemsAdapter { productId ->
            navToDetails(productId)
        }
    }

    private fun navToDetails(productId: String) {
        val uri = Uri.parse("shopify://productDetailsFragment/${productId}")
        navController.navigate(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.adapter = favoritesAdapter
        binding.searchAdapter = searchAdapter
        binding.guestView.navToAuthBtn.setOnClickListener {
            navController.navigate(getString(R.string.authFragmentDeepLink).toUri())
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        requireActivity().snackBarObserver(viewModel.snackBarFlow)

        binding.searchEditText.apply {

            setOnSearchClickListener {
                binding.searchResultRv.visible()
                binding.noFavsView.gone()
                binding.guestView.root.gone()
            }
            setOnCloseListener {
                binding.searchResultRv.gone()
                false
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    binding.noFavsView.gone()
                    binding.guestView.root.gone()

                    viewModel.onEvent(FavoritesIntent.Search(newText ?: ""))


                    return false
                }
            }
            )
        }
        checkConnection()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        binding.searchResultRv.gone()

    }

    override fun onPause() {
        super.onPause()
        binding.searchEditText.setQuery("", false);
        binding.searchEditText.isIconified = true;
    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {

                        binding.noFavsView goneIf (state.products!!.isNotEmpty() || state.loading || state.guest || binding.searchResultRv.isVisible)
                        binding.guestView.root visibleIf (state.guest && !state.loading)
                        binding.searchEditText goneIf state.guest
                        binding.favoritesProgressBar visibleIf state.loading
                        binding.favItemsRv goneIf state.guest

                        favoritesAdapter.submitList(state.products)
                        println("Prices: ")
                        state.products?.forEach { println(it.price) }
                        if (state.searchResult.isNotEmpty()) {
                            searchAdapter.submitList(state.searchResult)
                        }

                    }
                }
            }
        }

    }

    private fun showConfirmDeleteDialog(id: String, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Item Will be Deleted, Proceed?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onEvent(FavoritesIntent.RemoveFromFavorites(id, position))
            }.setNegativeButton("No") { _, _ -> }
            .show()
    }

    private fun checkConnection() {
        val connectivitySnackBar = Snackbar.make(
            binding.root, getString(com.firebase.ui.auth.R.string.fui_no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                delay(200)
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        connectivitySnackBar.dismiss()
                        viewModel.onEvent(FavoritesIntent.GetFavorites)
                    }

                    else -> {
                        connectivitySnackBar.show()
                    }
                }
            }
        }
    }
}