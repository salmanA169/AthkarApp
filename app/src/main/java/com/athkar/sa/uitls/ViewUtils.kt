package com.athkar.sa.uitls

import com.athkar.sa.R
import com.google.android.material.button.MaterialButton

fun MaterialButton.setPlayPause(isPlay:Boolean){
    setIconResource(if (isPlay) R.drawable.pause_24px else R.drawable.play_arrow_24px)
}