package com.athkar.sa.ui.homeScreen.container.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athkar.sa.adapters.FavoriteAdapter
import com.athkar.sa.databinding.FavoriteFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteFragmentBinding>({inflater: LayoutInflater, container: ViewGroup? ->
    FavoriteFragmentBinding.inflate(inflater,container,false)
}) {

    private val  viewModel by viewModels<FavoriteViewModel>()
    private val favoriteAdapter = FavoriteAdapter()
    override fun FavoriteFragmentBinding.init() {
//        rvFavorite.addItemDecoration(MaterialDividerItemDecoration(requireContext(),MaterialDividerItemDecoration.VERTICAL))
        rvFavorite.adapter = favoriteAdapter
    }

    override fun observe() {
        viewModel.favorite.observe {
            favoriteAdapter.submitList(it)
        }
    }
}