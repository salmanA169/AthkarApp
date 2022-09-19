package com.athkar.sa.ui.startUp.permissionLocationFragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.athkar.sa.databinding.FragmentLocationPermissionBinding
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.checkLocationPermission
import com.athkar.sa.uitls.shouldShowLocationPermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationPermissionFragment :BaseFragment<FragmentLocationPermissionBinding>({
    inflater, container ->
    FragmentLocationPermissionBinding.inflate(inflater,container,false)
}) {

    // TODO: change to activity
    private val viewModel by viewModels<LocationPermissionViewModel>()
    lateinit var launcher: ActivityResultLauncher<Array<String>>

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentLocationPermissionBinding.inflate(inflater,container,false)
//        return binding.root
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.btnEnable.setOnClickListener {
            if (!shouldShowLocationPermissionDialog()) {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.setData(Uri.parse("package:com.athkar.sa"))
                    if (it.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(it)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "اذهب الى الاعدادات وقم بتفعيل الموقع",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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

    override fun FragmentLocationPermissionBinding.init() {
    }
}