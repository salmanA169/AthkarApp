package com.athkar.sa.ui.startUp.downloadData

import android.Manifest
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.work.WorkInfo
import com.athkar.sa.databinding.FragmentDownloadBinding
import com.athkar.sa.uitls.checkConnection
import com.athkar.sa.uitls.checkLocationPermission
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragmentDialog : DialogFragment() {


    private val viewModel by viewModels<DownloadViewModel>()
    private lateinit var connectivityManager: ConnectivityManager
    private val TAG = javaClass.simpleName

    private val netWorkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.enqueueWorker()
        }


        override fun onLost(network: Network) {
            super.onLost(network)
            binding.down.text = "الرجاء التحقق من اتصالك بالانترنت"
            binding.circularProgressIndicator.isVisible = false
        }

        override fun onUnavailable() {
            super.onUnavailable()
            binding.down.text = "الرجاء التحقق من اتصالك بالانترنت"
            binding.circularProgressIndicator.isVisible = false
        }

    }

    private var _binding: FragmentDownloadBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.enqueueWorker()
        connectivityManager = requireActivity().getSystemService(ConnectivityManager::class.java)
        val netWorkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(netWorkRequest, netWorkCallback)

        observe()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(MaterialShapeDrawable().apply {
                setCornerSize(8f)
                this.fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterNetworkCallback(netWorkCallback)
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (!requireContext().checkLocationPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
//            controller.navigate(R.id.locationPermissionFragment)
        } else {
            viewModel.enqueueWorker()
            if (!connectivityManager.checkConnection()) {
                binding.down.text = "الرجاء التحقق من اتصالك بالانترنت"
                binding.circularProgressIndicator.isVisible = false
            }
        }
    }

    private fun observe() {
        viewModel.workInfo.observe(viewLifecycleOwner) {
            Log.d("DownloadFragment", it.toString())
            val workInfo = it.firstOrNull() ?: return@observe
            Log.d("DownloadFragment", workInfo.state.toString())
            when (workInfo.state) {
                WorkInfo.State.ENQUEUED -> {
                    binding.down.text = "الرجاء التحقق من اتصالك بالانترنت"
                    binding.circularProgressIndicator.isVisible = false
                }
                WorkInfo.State.RUNNING -> {
                    binding.down.text = "جاري تحميل مواقيت الصلاة.... "
                    binding.circularProgressIndicator.isVisible = true
                }
                WorkInfo.State.SUCCEEDED -> {
                    dismiss()
                }
                WorkInfo.State.FAILED -> {
                    binding.down.text = "خطاء غير معروف"
                }
                WorkInfo.State.BLOCKED -> {

                }
                WorkInfo.State.CANCELLED -> {
                }
                else -> {}
            }
        }
    }

}