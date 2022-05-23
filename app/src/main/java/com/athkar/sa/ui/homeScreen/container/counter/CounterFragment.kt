package com.athkar.sa.ui.homeScreen.container.counter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.CounterFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CounterFragment : BaseFragment<CounterFragmentBinding>({inflater: LayoutInflater, container: ViewGroup? ->
    CounterFragmentBinding.inflate(inflater,container,false)
}) {
    private val  viewModel by viewModels<CounterViewModel>()
    override fun CounterFragmentBinding.init() {

    }

    override fun observe() {

    }
}