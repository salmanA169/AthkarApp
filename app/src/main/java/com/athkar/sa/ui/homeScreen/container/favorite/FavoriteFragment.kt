package com.athkar.sa.ui.homeScreen.container.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
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
        rvFavorite.adapter = favoriteAdapter
        toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
    }

    override fun observe() {
        viewModel.favorite.observe {
            favoriteAdapter.submitList(it)
        }
    }
}