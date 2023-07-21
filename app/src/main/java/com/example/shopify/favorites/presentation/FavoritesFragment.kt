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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    lateinit var binding: FragmentFavoritesBinding

    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(listOf(), {}, {})
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
        viewModel.getFavorites()

        snackBarObserver(viewModel.snackBarFlow)

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {
                        binding.favoritesProgressBar visibleIf state.loading
                        binding.noFavsView.imageView.parent as View visibleIf state.empty

                        favoritesAdapter.submitList(state.products)

                    }
                }
            }
        }

    }
}