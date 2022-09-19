package com.athkar.sa.uitls

import android.content.Intent

fun getShareIntent(message:String) = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT , message)
    type = "text/plain"
}.run {
    Intent.createChooser(this,null)
}