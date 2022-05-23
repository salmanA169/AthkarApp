package com.athkar.sa.ui.settings

import com.athkar.sa.databinding.SettingsFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsFragmentBinding>({
    inflater, container ->
    SettingsFragmentBinding.inflate(inflater,container,false)
}) {

    override fun SettingsFragmentBinding.init() {

    }

    override fun observe() {

    }
}