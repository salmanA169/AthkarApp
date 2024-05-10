package com.athkar.sa.ui.homeScreen

import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import com.athkar.sa.MainActivity
import com.athkar.sa.R
import com.athkar.sa.adapters.HomeScreenAdapter
import com.athkar.sa.adapters.HomeScreenEvents
import com.athkar.sa.databinding.HomeScreenFragmentBinding
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.formatMonthToString
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

    private var isPrayInfoEmpty = true

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

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }


    override fun observe() {
        viewModel.athkar.observe {
            homeScreenAdapter.submitList(it)
        }
        viewModel.prays.observe { data ->
            val prayToday = data.prayToday
            val dateToday = data.dateToday
            binding.tvCurrentPray.text =
                "${prayToday.currentPrayName.namePray} ${prayToday.currentTimePrayTimeFormat}"
            binding.tvNextPray.text =
                "${prayToday.nextPray.namePray} ${prayToday.nextPrayTimeFormat}"
            binding.tvMonth.text = dateToday.month.formatMonthToString()
            binding.tvDayOfWeek.text = dateToday.day.toString()
            isPrayInfoEmpty = false

        }

    }


    override fun onAthkerClick(athkarCategory: AthkarCategory) {
        controller.navigate(
            HomeScreenFragmentDirections.actionHomeScreenToAthkarFragment(
                athkarCategory, null
            )
        )
    }

    override fun onContainerClick(destination: NavDirections) {
        if (destination.actionId == HomeScreenFragmentDirections.actionHomeScreenToPrayFragment().actionId) {
            if (isPrayInfoEmpty) {
                val mainActivity = requireActivity() as MainActivity
                mainActivity.checkPermissionAndShowDialog()
            } else {
                controller.navigate(destination)
            }
        } else {
            controller.navigate(destination)
        }

    }
}