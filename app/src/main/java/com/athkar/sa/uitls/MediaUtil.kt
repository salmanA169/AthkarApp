package com.athkar.sa.uitls

import androidx.media3.common.MediaMetadata

fun createMediaMetaData(title:String) = MediaMetadata.Builder().setTitle(title).build()