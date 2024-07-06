package com.athkar.sa.ui.homeScreen.athkar

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.athkar.sa.AthkarFragmentPager
import com.athkar.sa.Constants
import com.athkar.sa.R
import com.athkar.sa.databinding.FragmentAthkarBinding
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.ui.homeScreen.athkar.bottomSheetAthkar.BottomSheetAthkarFragment
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.getShareIntent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AthkarFragment : BaseFragment<FragmentAthkarBinding>({ inflater, container ->
    FragmentAthkarBinding.inflate(inflater, container, false)
}) {

    private val viewModel by viewModels<AthkarViewModel>()
    private val args by navArgs<AthkarFragmentArgs>()
    lateinit var athkarFragmentPager: AthkarFragmentPager
    private val TAG = javaClass.simpleName
    private var contentAlthker = ""
    @RequiresApi(Build.VERSION_CODES.R)
    override fun FragmentAthkarBinding.init() {


        athkarFragmentPager = AthkarFragmentPager(this@AthkarFragment)
        viewModel.categoryAlthker = args.athkarCategory
        toolbarAthkar.setNavigationOnClickListener {
            controller.popBackStack()
        }
        toolbarAthkar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.athkar_menu -> {
                    val sheet = BottomSheetAthkarFragment()
                    val mapString = viewModel.athkar.value!!.map {
                        it.nameAlthker
                    }
                    sheet.arguments = bundleOf(
                        Constants.ITEM_BOTTOM_KEY to mapString,
                        Constants.COLOR_ALTHKER_BOTTOM_KEY to args.athkarCategory.color,
                        Constants.TITLE_BOTTOM_KEY to args.athkarCategory.nameAthkar
                    )
                    sheet.show(childFragmentManager, null)
                    true
                }
                else -> false

            }
        }
        viewModel.getAthkar(args.athkarCategory)
        setScrollNextListener()
        toolbarAthkar.title = args.athkarCategory.nameAthkar
        favorite.setOnClickListener {
            if (viewModel.favorite.value != null) {
                val hasFavorite = viewModel.favorite.value
                if (hasFavorite != null) {
                    viewModel.removeFavorite(
                        hasFavorite
                    )
                }
            } else {
                viewModel.addFavorite(
                    FavoriteAthkar(
                        0,
                        viewModel.currentNameOfAlthker,
                        args.athkarCategory
                    )
                )
            }
        }
        progress.root.setOnClickListener {
            updateProgress()
        }
        btnShare.setOnClickListener {
            startActivity(getShareIntent(contentAlthker))
        }
        setDividerIfNightMode()
        binding.toolbarAthkar.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(top = insets.getInsets(WindowInsets.Type.statusBars()).top)
            insets
        }
        binding.cardview.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(bottom = insets.getInsets(WindowInsets.Type.navigationBars()).bottom)
            insets
        }
//        setNeverMode()
    }

//    private fun setNeverMode() {
//        binding.viewPager.children.forEach {
//            it.overScrollMode = View.OVER_SCROLL_NEVER
//        }
//    }

    private fun setDividerIfNightMode() {

        val uiMode = if (Build.VERSION.SDK_INT >= 30) {
            requireContext().resources.configuration.isNightModeActive
        } else {
            val configuration = requireContext().resources.configuration
            configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
        if (uiMode) {
            binding.divider.dividerColor = Color.parseColor(args.athkarCategory.color)
        }
    }

    private fun updateProgress() {
        if (binding.progress.root.isVisible) {
            var currentProgress = binding.progress.circularProgressIndicator2.progress
            if (currentProgress < viewModel.times.value!!) {
                binding.progress.circularProgressIndicator2.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS
                )
                binding.progress.circularProgressIndicator2.setProgress(++currentProgress, true)
                binding.progress.tvCount.text = currentProgress.toString()
            }
        }
    }

    private fun moveToCurrentNameOfAlthker(nameAlthker: String) {
        val getAdapter = binding.viewPager.adapter as? AthkarFragmentPager
        binding.viewPager.setCurrentItem(getAdapter?.getPositionByName(nameAlthker) ?: 0, true)
    }

    private fun setScrollNextListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val adapter = binding.viewPager.adapter as? AthkarFragmentPager
                super.onPageSelected(position)
                adapter?.let {
                    restCounter()
                    val getAthkarInfo = it.getAthkarByPosition(position)
                    viewModel.updateTimesAlthker(getAthkarInfo.times)
                    viewModel.currentNameOfAlthker = getAthkarInfo.nameAlthker
                    viewModel.updateFavorite()
                    contentAlthker = getAthkarInfo.getContentAlthker()
                    updatePositionText(position, it.getSizeAthkr())

                }
            }
        })

    }

    private fun updatePositionText(position: Int, totalAthkar: Int) {
        binding.countAthkar.text = "${position + 1} / $totalAthkar"
    }

    private fun updateFavorite(isFavorite: Boolean) {
        binding.favorite.setIconResource(if (isFavorite) R.drawable.favorite_icon else R.drawable.unfavorite_icon)
//        binding.favorite.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    private fun restCounter() {
        binding.progress.tvCount.text = "0"
        binding.progress.circularProgressIndicator2.setProgress(0)
    }

    override fun observe() {
        viewModel.athkar.observe { pager ->
            athkarFragmentPager.clearList() // this when change config it remove old list and add new
            pager.forEach {
                athkarFragmentPager.addAthkars(it)
            }
            binding.viewPager.adapter = athkarFragmentPager
            args.nameAlthker?.let {
                moveToCurrentNameOfAlthker(it)
            }
        }
        viewModel.favorite.observeFlow {
            updateFavorite(it != null)
        }
        viewModel.times.observe {

            updateTimes(it)
        }
        viewModel.eventClick.observe {
            updateProgress()
        }
        viewModel.moveToPosition.observe {
            val currentAdapter = binding.viewPager.adapter as AthkarFragmentPager
            val getPositionNameAlthker = currentAdapter.getPositionByName(it)
            val currentPosition = binding.viewPager.currentItem
            if (currentPosition != getPositionNameAlthker) {
                binding.viewPager.currentItem = getPositionNameAlthker
            }
        }
    }

    private fun updateTimes(count: Int) {
        if (count == 0) {
            binding.progress.apply {
                tvCount.text = "0"
                root.isVisible = false
            }
        } else {
            binding.progress.apply {
                this.circularProgressIndicator2.max = count
                circularProgressIndicator2.progress = 0
                tvCount.text = count.toString()
                root.isVisible = true
            }
        }
    }
}
