package com.athkar.sa.ui.homeScreen.container.randomUi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.RandomAthkarFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomAthkarFragment : BaseFragment<RandomAthkarFragmentBinding>({ inflater, container ->
    RandomAthkarFragmentBinding.inflate(inflater, container, false)
}) {

    override fun RandomAthkarFragmentBinding.init() {

    }

    override fun observe() {

    }

    private val viewModel by viewModels<RandomAthkarViewModel>()

}