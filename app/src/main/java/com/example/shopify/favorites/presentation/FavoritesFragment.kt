package com.example.shopify.favorites.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shopify.databinding.FragmentFavoritesBinding
import com.example.shopify.search.presentation.SearchItemsAdapter
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.gone
import com.example.shopify.utils.ui.visible
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEvent(FavoritesIntent.GetFavorites)


        requireActivity().snackBarObserver(viewModel.snackBarFlow)

        binding.searchEditText.apply {

            setOnCloseListener {
                binding.searchResultRv.gone()
                false
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //Should take the first element of the result list and go to it's details
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    binding.searchResultRv.visible()
                    binding.noFavsView.gone()
                    search(newText)


                    return false
                }
            }
            )
        }

        observeState()
    }

    override fun onResume() {
        super.onResume()
        binding.searchResultRv.gone()

    }

    private fun search(keyword: String?) {
        viewModel.onEvent(FavoritesIntent.Search(keyword ?: ""))
//        searchAdapter.submitList(searchResultList)
    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {

                        binding.noFavsView visibleIf (state.products.isNullOrEmpty() && !state.loading)

                        binding.favoritesProgressBar visibleIf state.loading

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
            .setMessage("Are You Sure To Delete")
            .setPositiveButton("No") { _, _ ->

            }.setNeutralButton("Yes") { _, _ ->

                viewModel.onEvent(FavoritesIntent.RemoveFromFavorites(id, position))
            }
            .show()
    }
}