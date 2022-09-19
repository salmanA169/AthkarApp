package com.athkar.sa

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.TestWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import com.athkar.sa.di.DIRepo
import com.athkar.sa.di.ModuleSingleton
import com.athkar.sa.repo.Repository
import com.athkar.sa.ui.homeScreen.HomeScreenFragment
import com.athkar.sa.worker.FetchDataCalenderPrays
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: Repository

    @Before
    fun init() {

//        val config = Configuration.Builder()
//            .setMinimumLoggingLevel(Log.DEBUG)
//            .setExecutor(SynchronousExecutor())
//            .build()
//        // Initialize WorkManager for instrumentation tests.
//        WorkManagerTestInitHelper.initializeTestWorkManager(
//            ApplicationProvider.getApplicationContext(),
//            config
//        )
        hiltRule.inject()
    }

    @After
    fun clean() {
    }

    @Test
    fun useAppContext() {
        val workManager =
            TestListenableWorkerBuilder<FetchDataCalenderPrays>(ApplicationProvider.getApplicationContext()).build()
        runTest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Create request
            val request = OneTimeWorkRequestBuilder<FetchDataCalenderPrays>()
                .setConstraints(constraints)
                .build()


            val testDriver =
                WorkManagerTestInitHelper.getTestDriver(ApplicationProvider.getApplicationContext())
            val result = workManager.doWork()

            // Assert
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }

    }
}