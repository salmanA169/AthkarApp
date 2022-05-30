package com.athkar.sa.ui.homeScreen.container.qibla

import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.QiblaFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.sensor.SensorEventListener
import com.athkar.sa.sensor.SensorOrientation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QiblaFragment : BaseFragment<QiblaFragmentBinding>({inflater, container ->
    QiblaFragmentBinding.inflate(inflater,container,false)
}),SensorEventListener {
    var currentDegree = 0f
    override fun onEventSensorChange(value: List<Float>?, onSensorNotExist: String?) {
        Log.d("QiblaFragment",value.toString())
        Log.d("QiblaFragment",onSensorNotExist.toString())
        value?.let {
            val o = Math.round(it[0])
            val animation = RotateAnimation(currentDegree,-o.toFloat(), Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            animation.fillAfter = true
            animation.duration = 500
            binding.imageView.animation = animation
            binding.imageView.animation.start()
            currentDegree = -o.toFloat()
        }
    }
    @Inject
    lateinit var o :SensorOrientation
    private val  viewModel by viewModels<QiblaViewModel>()
    override fun QiblaFragmentBinding.init() {
        toolbar2.setNavigationOnClickListener {
            controller.popBackStack()
        }
        o = SensorOrientation(requireContext())
        o.sensorEventListener = this@QiblaFragment
        o.startListing()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        o.stopListen()
    }
    override fun observe() {

    }
}