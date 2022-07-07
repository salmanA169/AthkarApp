package com.athkar.sa.ui.startUp.permissionLocationFragment

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.FragmentLocationPermissionBinding
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.checkLocationPermission
import com.athkar.sa.uitls.shouldShowLocationPermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationPermissionFragment :
    BaseFragment<FragmentLocationPermissionBinding>({ inflater, container ->
        FragmentLocationPermissionBinding.inflate(inflater, container, false)
    }) {

    private val viewModel by viewModels<LocationPermissionViewModel>()
    lateinit var launcher: ActivityResultLauncher<Array<String>>
    override fun FragmentLocationPermissionBinding.init() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineAccess =
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
                val coarseAccess =
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                if (fineAccess && coarseAccess) {
                    controller.popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "الرجاء تفعيل الموقع عن طريق الاعدادات",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        btnEnable.setOnClickListener {
            if (shouldShowLocationPermissionDialog()) {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                // show Settings
                Log.d("LocationPermissionFragment", "called settings")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireContext().checkLocationPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            controller.popBackStack()
        }
    }

    override fun observe() {

    }
}