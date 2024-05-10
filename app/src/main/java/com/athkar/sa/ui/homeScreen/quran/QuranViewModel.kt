package com.athkar.sa.ui.homeScreen.quran

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.coroutine.DispatcherProvider
import com.athkar.sa.quran.dto.Moshaf
import com.athkar.sa.repo.quran.QuranRepository
import com.athkar.sa.worker.utils.QuranWorkerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class QuranPagers{
    SURAH,FAVORITE,DOWNLOADS
}

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val quranRepository: QuranRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(QuranState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<QuranEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val allSurah= quranRepository.getAllSurah()
            _state.update {
                it.copy(
                    allSurah = allSurah
                )
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            quranRepository.getSurahDownloadFlow().collect{allSurah->
                _state.update {
                    it.copy(
                        surahDownloads = allSurah
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            quranRepository.observeDownloadStatus().collect{progressInfo->
                if (progressInfo == null ) return@collect
                _state.update {
                    it.copy(
                        dialogStatus = DialogState(progressInfo.isStartDownload,progressInfo,progressInfo.extracting),
                        showAlertDialog = false
                    )
                }
            }
        }

    }

    fun onEvent(quranEvent: QuranEvent){
        when(quranEvent){
            QuranEvent.ShowAlertDialog -> {
                _state.update {
                    it.copy(
                        showAlertDialog = true
                    )
                }
            }
            QuranEvent.StartDownload -> {
                quranRepository.startDownload()
            }

            is QuranEvent.NavigateQuranPage -> {
                _effect.update {
                    val getSurahPage = quranRepository.getPageBySurahId(quranEvent.surahId)
                    QuranEffect.NavigateQuranPageEffect(getSurahPage)
                }
            }

            is QuranEvent.DeleteSurah -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    quranRepository.deleteDownloadReaderFile(quranEvent.readerId,quranEvent.moshaf,quranEvent.surahId)
                    quranRepository.deleteSurahDownload(quranEvent.uri)
                }
            }
            is QuranEvent.PlayAudio -> {
                _effect.update {
                    QuranEffect.PlayAudioEffect(quranEvent.title,quranEvent.uri)
                }
            }

            QuranEvent.ClosePageEvent -> {
                _effect.update {
                    QuranEffect.ClosePageEffect
                }
            }
        }
    }
    fun resetEffect(){
        _effect.update {
            null
        }
    }
     fun checkQuranData(){
        viewModelScope.launch (dispatcherProvider.io){
            if (!quranRepository.checkQuranData()){
                onEvent(QuranEvent.ShowAlertDialog)
            }
        }
    }
}
sealed class QuranEffect{
    data class NavigateQuranPageEffect(val pageNum:Int):QuranEffect()
    data class PlayAudioEffect(val title:String,val uri:String):QuranEffect()
    data object ClosePageEffect:QuranEffect()
}
sealed class QuranEvent{
    data object ShowAlertDialog:QuranEvent()
    data object StartDownload:QuranEvent()
    data class NavigateQuranPage(val surahId:Int) : QuranEvent()

    data class PlayAudio(val title:String,val uri:String):QuranEvent()
    data class DeleteSurah(val surahId:Int,val uri:String,val moshaf: String,val readerId:Int):QuranEvent()
    data object ClosePageEvent:QuranEvent()
}