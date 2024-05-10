package com.athkar.sa.audio

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.work.await
import com.athkar.sa.service.PlaybackService
import com.athkar.sa.uitls.formatDuration
import com.google.common.util.concurrent.Futures
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.Closeable
import javax.inject.Inject


@SuppressLint("RestrictedApi")
class PlayerAudioManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : Closeable, Player.Listener {
    private val sessionToken =
        SessionToken(context, ComponentName(context, PlaybackService::class.java))
    private lateinit var mediaController: MediaController
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var playerMediaEvent: PlayerMediaEvent? = null

    init {
        scope.launch {
            mediaController = MediaController.Builder(context, sessionToken).buildAsync().await()
            mediaController.addListener(this@PlayerAudioManager)
            playerMediaEvent?.onPlay(
                mediaController.playWhenReady,
                AudioItemInfo(
                    mediaController.mediaMetadata.title.toString(),
                    mediaController.currentMediaItem?.mediaId ?: "",
                )
            )
            playerMediaEvent?.showPlayer(mediaController.playbackState == Player.STATE_READY)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        playerMediaEvent?.onError(error.message?:"Unknown Error")
    }
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        playerMediaEvent?.showPlayer(playbackState != Player.STATE_IDLE)

    }
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        playerMediaEvent?.onPlay(
            playWhenReady,
            AudioItemInfo(
                mediaController.mediaMetadata.title?.toString()?:"",
                mediaController.currentMediaItem?.mediaId ?: ""
            ),

        )

        if (!playWhenReady) {
            job?.cancel()
        } else {
            updateProgress()
        }
    }

    private fun updateProgress() {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                delay(200)
                playerMediaEvent?.onUpdateProgress(
                    mediaController.currentPosition,
                    mediaController.duration,
                    mediaController.currentMediaItem?.mediaMetadata?.title.toString()
                )
            }
        }
    }

    fun playAudioItem(id: String, uri: String, mediaMetaData: MediaMetadata) {
        mediaController.setMediaItem(
            MediaItem.Builder().setMediaId(id).setUri(uri).setMediaMetadata(mediaMetaData).build()
        )
        mediaController.prepare()
        mediaController.play()
        updateProgress()
    }

    fun pauseAudio() {
        mediaController.pause()
    }


    fun setListener(playListener: PlayerMediaEvent) {
//        mediaController.addListener(playListener)
        playerMediaEvent = playListener
    }

    fun resumeAudio() {
        mediaController.play()
    }

    fun releaseAudio() {
        mediaController.stop()
        job?.cancel()
    }

    fun resetItems() {
        mediaController.pause()
    }

    override fun close() {
        mediaController.release()
        job?.cancel()
    }

    fun seekTo(progress: Long) {
        mediaController.seekTo(progress)
    }

    fun forward5() {
        val position = mediaController.currentPosition + 5000
        mediaController.seekTo(position)
    }

    fun back5() {
        val position = mediaController.currentPosition - 5000
        mediaController.seekTo(position)
    }

    fun updateStateAudio(): Boolean {
        val isPlay = mediaController.playWhenReady
        if (isPlay) {
            mediaController.pause()
        } else {
            mediaController.play()
        }
        return isPlay
    }

    fun getCurrentMediaMetaData(): AudioItemInfo? {
        val currentItem = mediaController.currentMediaItem ?: return null
        return AudioItemInfo(currentItem.mediaMetadata.title.toString(), currentItem.mediaId)
    }
}

interface PlayerMediaEvent {
    fun onUpdateProgress(position: Long, duration: Long,title:String)
    fun onPlay(isPlay: Boolean, audioItemInfo: AudioItemInfo)
    fun showPlayer(showPlayer:Boolean)
    fun onError(errorMessage:String)
}

data class AudioItemInfo(
    val title: String,
    val mediaId: String,
)


