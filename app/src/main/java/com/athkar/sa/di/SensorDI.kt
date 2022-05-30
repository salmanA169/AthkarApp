package com.athkar.sa.di

import android.content.Context
import com.athkar.sa.sensor.SensorOrientation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
object SensorDI {

    @Provides
    @FragmentScoped
    fun provideOrientationSensor(@ActivityContext context:Context) = SensorOrientation(context)
}