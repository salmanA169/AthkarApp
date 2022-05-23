package com.athkar.sa.ui.homeScreen.container.qibla

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athkar.sa.R
import com.athkar.sa.databinding.QiblaFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QiblaFragment : BaseFragment<QiblaFragmentBinding>({inflater, container ->
    QiblaFragmentBinding.inflate(inflater,container,false)
}) {

    private val  viewModel by viewModels<QiblaViewModel>()
    override fun QiblaFragmentBinding.init() {

    }

    override fun observe() {

    }
}