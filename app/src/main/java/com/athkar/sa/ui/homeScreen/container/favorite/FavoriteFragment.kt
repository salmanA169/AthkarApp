package com.athkar.sa.ui.homeScreen.container.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.FavoriteFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteFragmentBinding>({inflater: LayoutInflater, container: ViewGroup? ->
    FavoriteFragmentBinding.inflate(inflater,container,false)
}) {



    private val  viewModel by viewModels<FavoriteViewModel>()

    override fun FavoriteFragmentBinding.init() {

    }

    override fun observe() {

    }
}