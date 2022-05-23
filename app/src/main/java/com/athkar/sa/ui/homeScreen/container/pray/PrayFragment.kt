package com.athkar.sa.ui.homeScreen.container.pray

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athkar.sa.Constants
import com.athkar.sa.adapters.PrayAdapter
import com.athkar.sa.databinding.PrayFragmentBinding
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.ConstantPatternsDate
import com.athkar.sa.uitls.calculateNextPrayTime
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@AndroidEntryPoint
class PrayFragment :
    BaseFragment<PrayFragmentBinding>({ inflater: LayoutInflater, container: ViewGroup? ->
        PrayFragmentBinding.inflate(inflater, container, false)
    }) {

    private val prayAdapter = PrayAdapter()


    private var timer: CountDownTimer? = null
    override fun PrayFragmentBinding.init() {

        rvPrayes.adapter = prayAdapter
        toolbar2.setNavigationOnClickListener {
            controller.popBackStack()
        }
        btnCalendar.setOnClickListener {
            controller.navigate(PrayFragmentDirections.actionPrayFragmentToCalendarFragment())
        }
    }

    override fun observe() {
        viewModel.prays.observe {
            val nextPrayName = it.calculateNextPrayTime()
            val nextPray = it.find { it.prayName == nextPrayName }!!
            nextPray.isNextPray = true
            binding.tvNextPray.text = "${nextPray.prayName.namePray} بعد"
            startTimer(nextPray.timePray, nextPray.prayName)
            prayAdapter.submitList(it)
        }
        viewModel.prayInfo.observe {
            val parseDate = LocalDate.ofEpochDay(it.date)
            val parseDateHijrah = HijrahDate.from(parseDate)
            binding.tvDateAr.text = parseDateHijrah.format(ConstantPatternsDate.hijrahPattern)
            binding.tvDateEn.text = parseDate.toString()
            binding.tvCity.text = it.city
            binding.tvToday.text = parseDate.format(ConstantPatternsDate.todayPattern)
        }

    }

    private fun startTimer(timeNextPray: Long, prayName: PrayName) {
        timer?.cancel()


        val currentTime = if (prayName == PrayName.FAJAR) {
            LocalDateTime.now().until(
                LocalDateTime.of(
                    LocalDate.now().plusDays(1),
                    LocalTime.ofSecondOfDay(timeNextPray)
                ), ChronoUnit.MILLIS
            )
        } else {
            LocalTime.now().until(LocalTime.ofSecondOfDay(timeNextPray), ChronoUnit.MILLIS)
        }

        timer = object : CountDownTimer(currentTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCounterNextPray.text =
                    LocalTime.ofSecondOfDay(millisUntilFinished.milliseconds.toLong(DurationUnit.SECONDS)).format(
                        DateTimeFormatter.ISO_LOCAL_TIME)
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