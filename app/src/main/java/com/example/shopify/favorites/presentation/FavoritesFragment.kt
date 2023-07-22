package com.example.shopify.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopify.databinding.FragmentFavoritesBinding
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    lateinit var binding: FragmentFavoritesBinding

    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(
            listOf(),
            onItemClick = { Timber.i("Item: $it pressed") },
            onDeleteClick = {
                showConfirmDeleteDialog(it.toString())
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.adapter = favoritesAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEvent(FavoritesIntent.GetFavorites)

        snackBarObserver(viewModel.snackBarFlow)

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {

                        binding.noFavsView visibleIf state.products.isNullOrEmpty()

                        binding.favoritesProgressBar visibleIf state.loading

                        favoritesAdapter.submitList(state.products)

                    }
                }
            }
        }

    }

    private fun showConfirmDeleteDialog(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are You Sure To Delete")
            .setPositiveButton("No") { _, _ ->

            }.setNeutralButton("Yes") { _, _ ->

                viewModel.onEvent(FavoritesIntent.RemoveFromFavorites(id))
            }
            .show()
    }
}