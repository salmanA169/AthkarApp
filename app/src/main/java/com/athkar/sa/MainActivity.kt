package com.athkar.sa

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.athkar.sa.databinding.ActivityMainBinding
import com.athkar.sa.ui.startUp.downloadData.DownloadFragmentDialog
import com.athkar.sa.uitls.checkLocationPermission
import com.athkar.sa.uitls.shouldShowLocationPermissionDialog
import com.athkar.sa.uitls.updateWidgets
import com.athkar.sa.worker.FetchDataCalenderPrays
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    lateinit var controller: NavController


    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var alarmManager: AlarmManager

    lateinit var launcher: ActivityResultLauncher<Array<String>>

    private lateinit var snackbar: Snackbar
    private var isEmptyData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineAccess =
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
                val coarseAccess =
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                if (fineAccess && coarseAccess) {
//                controller.popBackStack()
                    showProgressDialog()
                } else {
                    Toast.makeText(
                        this,
                        "الرجاء تفعيل الموقع عن طريق الاعدادات",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        snackbar = Snackbar.make(
            binding.root,
            "يجب تحميل البيانات ",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("تحميل") {
            checkPermissionAndShowDialog()
        }
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = navHost.navController
        // TODO: debug this later maybe not schedule alarm because first time pray info is empty
        viewModel.scheduleAlarm()
        createNotificationChannel()
        observe()
        updateWidgets(this)
    }

    private fun showDialogLocationPermission() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setTitle("طلب أذن وصول موقعك")
            .setIcon(R.drawable.location_icon1)
            .setMessage("نحتاج أذن وصول موقعك لتحميل مواقيت الصلاة")
            .setPositiveButton("تفعيل") { d, aa ->
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
                        if (it.resolveActivity(packageManager) != null) {
                            startActivity(it)
                        } else {
                            Toast.makeText(
                                this,
                                "اذهب الى الاعدادات وقم بتفعيل الموقع",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            .show()
    }

    private fun showProgressDialog() {
        DownloadFragmentDialog()
            .show(supportFragmentManager, null)
    }

    fun checkPermissionAndShowDialog() {
        if (checkLocationPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            if (isEmptyData) {
                showProgressDialog()
            }
        } else {
            showDialogLocationPermission()
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.getPrayInfo.flowWithLifecycle(lifecycle)
                .collect { praysInfo ->
                    isEmptyData = praysInfo.isEmpty()
                    if (praysInfo.isEmpty()) {
//                        controller.navigate(R.id.downloadFragment)
                        checkPermissionAndShowDialog()
                        snackbar.show()
                    } else {
                        snackbar.dismiss()
                        lifecycleScope.launch {
                            if (viewModel.getPrayInfoToday() == null) {
                                val oneTimeRequestWorker =
                                    OneTimeWorkRequestBuilder<FetchDataCalenderPrays>()
                                        .setConstraints(Constraints().apply {
                                            requiredNetworkType = NetworkType.CONNECTED
                                        })
                                        .setInputData(workDataOf(ConstantsWorker.DOWNLOAD_DATA_WORKER_KEY to true))
                                        .build()
                                // TODO: after fix alarm worker chain with this work
                                workManager.enqueueUniqueWork(
                                    ConstantsWorker.DOWNLOAD_WORKER,
                                    ExistingWorkPolicy.KEEP,
                                    oneTimeRequestWorker
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)

            description = "تنبيهات الصلوات"
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }
}