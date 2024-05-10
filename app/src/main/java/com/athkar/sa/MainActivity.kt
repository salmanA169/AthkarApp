package com.athkar.sa

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.athkar.sa.audio.AudioItemInfo
import com.athkar.sa.audio.PlayerMediaEvent
import com.athkar.sa.databinding.ActivityMainBinding
import com.athkar.sa.ui.startUp.downloadData.DownloadFragmentDialog
import com.athkar.sa.uitls.checkLocationPermission
import com.athkar.sa.uitls.setPlayPause
import com.athkar.sa.uitls.shouldShowLocationPermissionDialog
import com.athkar.sa.uitls.updateWidgets
import com.athkar.sa.worker.FetchDataCalenderPrays
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MotionLayout.TransitionListener, PlayerMediaEvent {

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

    private var isAudioPlay = false
    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        if (currentId == R.id.start) {
            if (isAudioPlay) {
                viewModel.releaseAudio()
            }
        }
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.R)
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
        binding.root.setTransitionListener(this)

        binding.forward5.setOnClickListener {
            viewModel.forward5()
        }
        binding.back5.setOnClickListener {
            viewModel.back5()
        }
        binding.btnPlay.setOnClickListener {
            viewModel.updateStateAudio()
        }
        viewModel.setAudioListener(this)

        binding.seekAudio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(seekBar!!.progress.toLong())
            }
        })

    }

    override fun onError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onPlay(isPlay: Boolean, audioItemInfo: AudioItemInfo) {
        binding.btnPlay.setPlayPause(isPlay)
        binding.textReader.text = audioItemInfo.title
        isAudioPlay = isPlay
    }

    override fun showPlayer(showPlayer: Boolean) {
        if (showPlayer) {
            if (binding.root.currentState == R.id.start) {
                binding.root.transitionToState(R.id.down)
            }
        } else {
            binding.root.transitionToState(R.id.start, 0)
        }
    }

    override fun onUpdateProgress(position: Long, duration: Long, title: String) {
        val position1 = position.milliseconds.inWholeSeconds
        val duration1 = duration.milliseconds.inWholeSeconds
        val progress = (position1.toFloat() / duration1.toFloat()) * 100
        binding.progressPosition.setProgress(progress.toInt())
        binding.seekAudio.max = duration.toInt()
        binding.seekAudio.progress = position.toInt()
        binding.exoPosition.text = DateUtils.formatElapsedTime(position / 1000).removePrefix("0")
        binding.exoDuration.text = DateUtils.formatElapsedTime(duration / 1000).removePrefix("0")
        binding.textReader.text = title
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
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                Manifest.permission.POST_NOTIFICATIONS else ""
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
        lifecycleScope.launch {
            viewModel.effect.collect {
                when (it) {
                    MainEffect.ShowAlarmDialog -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                        }
                    }

                    null -> Unit
                }
                viewModel.resetEffect()
            }
        }
        lifecycleScope.launch {
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
                                        .setConstraints(Constraints(NetworkType.CONNECTED))
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