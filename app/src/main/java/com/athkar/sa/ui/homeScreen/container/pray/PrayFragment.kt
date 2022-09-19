package com.athkar.sa.ui.homeScreen.container.pray

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.athkar.sa.adapters.PrayAdapter
import com.athkar.sa.databinding.PrayFragmentBinding
import com.athkar.sa.db.entity.DateToday
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.uitls.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.*
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@AndroidEntryPoint
class PrayFragment :
    BaseFragment<PrayFragmentBinding>({ inflater: LayoutInflater, container: ViewGroup? ->
        PrayFragmentBinding.inflate(inflater, container, false)
    }) {

    private val prayAdapter = PrayAdapter() {
        viewModel.updatePrayNotification(it) {
            mainViewModel.rescheduleAlarm()
        }
    }

    private var timer: CountDownTimer? = null
    override fun PrayFragmentBinding.init() {

        rvPrayes.adapter = prayAdapter

        toolbar2.setNavigationOnClickListener {
            controller.popBackStack()
        }

        btnCalendar.setOnClickListener {
            controller.navigate(PrayFragmentDirections.actionPrayFragmentToCalendarFragment())
        }
//        btnQibla.setOnClickListener {
////            controller.navigate(PrayFragmentDirections.actionPrayFragmentToQiblaFragment())
//        }
    }

    private fun hide() {
        binding.materialCardView2.isVisible = false
        binding.materialCardView3.isVisible = false
        binding.materialCardView4.isVisible = false
    }

    private fun show() {
        binding.materialCardView2.isVisible = true
        binding.materialCardView3.isVisible = true
        binding.materialCardView4.isVisible = true

    }

    override fun observe() {
        viewModel.prays.observe {
            val prayInfo = it.praysToday
            if (prayInfo.isEmpty() || it.orderPray == null) {
                hide()
                return@observe
            } else {
                show()
            }
            val nextPrayName = it.orderPray
            val nextPray = prayInfo.find { it.prayName == nextPrayName.nextPray }!!
            nextPray.isNextPray = true
            binding.tvNextPray.text = "${nextPray.prayName.namePray} بعد"
            if (it.date != null) {
                setDateAndCity(it.date)
            }
            startTimer(nextPrayName.calculateFromCurrentTimeToNextPrayTimeInMillis())
            prayAdapter.submitList(prayInfo)

        }
    }

    private fun setDateAndCity(dateToday: DateToday) {
        val parseDate = dateToday.parseDate()
        val parseDateHijrah = HijrahDate.from(parseDate)
        binding.tvDateAr.text = parseDateHijrah.format(ConstantPatternsDate.hijrahPattern)
        binding.tvDateEn.text = parseDate.toString()
        binding.tvCity.text = dateToday.city
        binding.tvToday.text = parseDate.format(ConstantPatternsDate.todayPattern)
    }

    private fun startTimer(timeNextPray: Long) {
        timer?.cancel()

        timer = object : CountDownTimer(timeNextPray, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCounterNextPray.text =
                    LocalTime.ofSecondOfDay(millisUntilFinished.milliseconds.toLong(DurationUnit.SECONDS))
                        .format(
                            DateTimeFormatter.ISO_LOCAL_TIME
                        )
            }

            override fun onFinish() {

            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

    private val viewModel by viewModels<PrayViewModel>()


}