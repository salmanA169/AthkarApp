package com.athkar.sa.ui.homeScreen

import android.location.LocationManager
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import com.athkar.sa.R
import com.athkar.sa.adapters.HomeScreenAdapter
import com.athkar.sa.adapters.HomeScreenEvents
import com.athkar.sa.databinding.HomeScreenFragmentBinding
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.setColorsFromImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<HomeScreenFragmentBinding>({ inflater, container ->
    HomeScreenFragmentBinding.inflate(inflater, container, false)
}), HomeScreenEvents {

    private val viewModel by viewModels<HomeScreenViewModel>()
    private val homeScreenAdapter = HomeScreenAdapter(this)

    override var shouldRemoveView: Boolean = false


    override fun HomeScreenFragmentBinding.init() {
        rvHomeScreen.adapter = homeScreenAdapter

        lifecycleScope.launch(Dispatchers.Default) {
            collapseToolbar.setColorsFromImage(appBarImage.drawable.toBitmap())
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_settings -> {

                }
            }
            true
        }
    }

    override fun observe() {
        viewModel.athkar.observe {
            homeScreenAdapter.submitList(it)
        }
    }

    override fun onAthkerClick(athkarCategory: AthkarCategory) {

    }

    override fun onContainerClick(destination: NavDirections) {
        controller.navigate(destination)
    }
}