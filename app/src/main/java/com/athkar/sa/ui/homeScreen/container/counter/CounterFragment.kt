package com.athkar.sa.ui.homeScreen.container.counter

import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.athkar.sa.R
import com.athkar.sa.databinding.CounterFragmentBinding
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CounterFragment :
    BaseFragment<CounterFragmentBinding>({ inflater: LayoutInflater, container: ViewGroup? ->
        CounterFragmentBinding.inflate(inflater, container, false)
    }) {
    private var currentCounter: CounterAlthker? = null
    private val viewModel by viewModels<CounterViewModel>()
    private var isVibrateEnable = false
    private var isFirstTime = true

    override fun CounterFragmentBinding.init() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.vibrate_menu -> {
                    viewModel.updateEnableVibration(!isVibrateEnable)
                    true
                }
                R.id.refresh_menu -> {
                    val getCounter =
                        currentCounter?.copy(count = 0) ?: return@setOnMenuItemClickListener false
                    viewModel.updateCounterAlthker(getCounter)
                    true
                }

                R.id.more_menu -> {
                    controller.navigate(CounterFragmentDirections.actionCounterFragmentToChooseCounterSheetFragment())
                    true
                }
                else -> false
            }
        }
        binding.container.setOnClickListener {
            currentCounter?.copy(
                count = currentCounter?.count?.plus(1) ?: return@setOnClickListener
            )?.let { it1 ->
                    viewModel.updateCounterAlthker(it1)
                    if (isVibrateEnable) {
                        requireActivity().getSystemService(Vibrator::class.java).apply {
                            if (hasVibrator()) {
                                vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                            }
                        }
                    }
                }
        }
        binding.toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
    }

    override fun observe() {
        viewModel.currentCounterAlthker.observeFlow {
            val result = it.counterAlthker
            if (result != null) {
                currentCounter = result
                binding.addAlthker.isVisible = false
                binding.tvCounter.isVisible = true
                binding.tvNameAlthkr.isVisible = true
                binding.tvNameAlthkr.text = result.name
                binding.tvCounter.text = result.count.toString()
                updateMenu(it.enableVibrate)
            } else {
                binding.tvCounter.isVisible = false
                binding.tvNameAlthkr.isVisible = false
                binding.addAlthker.apply {
                    isVisible = true
                    setOnClickListener {
                        controller.navigate(CounterFragmentDirections.actionCounterFragmentToChooseCounterSheetFragment())
                    }
                }
            }
        }
    }

    private fun updateMenu(isEnable: Boolean) {
        if (isVibrateEnable != isEnable || isFirstTime) {
            isVibrateEnable = isEnable
            binding.toolbar.menu.findItem(R.id.vibrate_menu)?.let {
                it.setIcon(if (isVibrateEnable) R.drawable.vibrate_on_icon else R.drawable.vibrate_off_icon)
            }
            isFirstTime = false
        }
    }

}