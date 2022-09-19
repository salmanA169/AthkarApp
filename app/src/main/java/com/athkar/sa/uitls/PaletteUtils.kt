package com.athkar.sa.uitls

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.collection.LruCache
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.athkar.sa.Constants
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

suspend inline fun getPalette(
    bitmap: Bitmap,
    crossinline onGenerate: Palette.(scope:CoroutineScope) -> Unit
) {
    supervisorScope {
        val palette = Palette.from(bitmap).generate()
        palette.onGenerate(this)
    }
}

suspend fun CollapsingToolbarLayout.setColorsFromImage(bitmap: Bitmap) {
    getPalette(bitmap) { scope: CoroutineScope ->
        val vibrantSwatch = vibrantSwatch
        scope.launch(Dispatchers.Main) {
            if (vibrantSwatch != null) {
                setContentScrimColor(ColorUtils.setAlphaComponent(vibrantSwatch.rgb, Constants.TOOLBAR_COLOR_ALPHA))
                setCollapsedTitleTextColor(vibrantSwatch.titleTextColor)
            } else {
                setContentScrimColor(Color.BLACK)
                setCollapsedTitleTextColor(Color.WHITE)
            }
        }

    }
}