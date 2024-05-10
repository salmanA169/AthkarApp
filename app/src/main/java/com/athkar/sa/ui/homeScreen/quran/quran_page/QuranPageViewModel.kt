package com.athkar.sa.ui.homeScreen.quran.quran_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.coroutine.DispatcherProvider
import com.athkar.sa.repo.quran.QuranRepoEvent
import com.athkar.sa.repo.quran.QuranRepository
import com.athkar.sa.repo.quran.QuranRepositoryImpl
import com.athkar.sa.uitls.formatTextToGetFileFromServer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SurahItem(
    val surahId: Int,
    val surahName: String,
)

@HiltViewModel
class QuranPageViewModel
@Inject
constructor(
    private val quranRepository: QuranRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), QuranRepoEvent {
    private val _state = MutableStateFlow(QuranPageState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<QuranPageEffect?>(null)
    val effect = _effect.asStateFlow()
    private var currentJob: Job? = null

    private fun changeDarkMode(boolean: Boolean) {
        viewModelScope.launch(dispatcherProvider.io) {
            quranRepository.updateDarkModePage(boolean)
            _state.update {
                it.copy(isDarkMode = boolean)
            }
        }
    }

//    override fun onAudioEventChange(surahAudioItem: SurahAudioItem) {
//        try {
//            val getCurrentSurah = _state.value.getAllSurahReader.toMutableList()
//            val findCurrentSurahPlaying = getCurrentSurah.find {
//                surahAudioItem.id == it.id
//            }
//
//            if (findCurrentSurahPlaying != null) {
//                val getIndex = getCurrentSurah.indexOfFirst { it.id == surahAudioItem.id }
//                getCurrentSurah[getIndex] = findCurrentSurahPlaying.copy(isPlaying = surahAudioItem.isPlaying)
//                _state.update {
//                    it.copy(
//                        getAllSurahReader = getCurrentSurah.toList()
//                    )
//                }
//            }
//        } catch (e: Exception) {
//
//        }
//    }

    fun resetEffect() {
        _effect.update {
            null
        }
    }

    private fun updateMediaStatue() {
        _effect.update {
            QuranPageEffect.UpdateStatus
        }
    }

    fun resetAudioItemState(currentAudioPlay: String) {
        val tempList =
            _state.value.getAllSurahReader.map { it.copy(isPlaying = false) }.toMutableList()
        val getCurrent = tempList.find { it.id == currentAudioPlay } ?: return
        val getIndex = tempList.indexOfFirst { it.id == getCurrent.id }
        tempList[getIndex] = getCurrent.copy(isPlaying = true)
        _state.update {
            it.copy(
                getAllSurahReader = tempList.toList()
            )
        }
    }

    fun updateAudioItemState(currentPlayId: String, isPlay: Boolean) {
        val tempList = _state.value.getAllSurahReader.toMutableList()
        val getCurrent = tempList.find { it.id == currentPlayId } ?: return
        val getIndex = tempList.indexOfFirst { it.id == getCurrent.id }
        tempList[getIndex] = getCurrent.copy(isPlaying = isPlay)
        _state.update {
            it.copy(
                getAllSurahReader = tempList.toList()
            )
        }
    }

    override fun onShowError(message: String) {
        _effect.update {
            QuranPageEffect.ShowError(message)
        }
    }

    fun onEvent(quranPageEvent: QuranPageEvent) {
        when (quranPageEvent) {
            QuranPageEvent.FetchDataReader -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    if (_state.value.quranReader.isEmpty()) {
                        _state.update {
                            it.copy(
                                isLoadingFetchReader = true,
                            )
                        }
                        val getReaderBySurah = quranRepository.getReaders()
                            .onSuccess { result ->
                                _state.update {
                                    it.copy(
                                        quranReader = result.reciters,
                                        isLoadingFetchReader = false,
                                    )
                                }
                            }.onFailure { error ->
                                _effect.update {
                                    QuranPageEffect.ShowError(error.message ?: "Unknown error")
                                }
                            }

                    }
                }
            }

            is QuranPageEvent.LoadPage -> {
                loadPage(quranPageEvent.pageNum)
            }

            is QuranPageEvent.ChangeDarkMode -> {
                changeDarkMode(quranPageEvent.darkMode)
            }

            is QuranPageEvent.GetAllSurahByReader -> {
                currentJob?.cancel()
                currentJob =
                    viewModelScope.launch(dispatcherProvider.io) {
                        quranRepository.getAudioFilesStatus(
                            quranPageEvent.surahList,
                            quranPageEvent.readerId,
                            quranPageEvent.currentMoshf,
                        ).collect { lists ->
                            _state.update {
                                it.copy(
                                    getAllSurahReader = lists,
                                )
                            }
                            loadCurrentAudioIfAvailable()
                        }
                    }
            }

            is QuranPageEvent.DownLoadSurah -> {
                viewModelScope.launch(dispatcherProvider.io) {
//                    quranRepository.download(quranPageEvent.basePth)
                    val server =
                        quranPageEvent.basePth.plus("${formatTextToGetFileFromServer(quranPageEvent.surahId.toString())}.mp3")

                    try {
                        quranRepository.downloadAudio(
                            server,
                            quranPageEvent.readerId,
                            quranPageEvent.currentMoshf,
                            quranPageEvent.surahId,
                            quranPageEvent.readerName
                        )
                    } catch (e: Exception) {
                        _effect.update {
                            QuranPageEffect.ShowError(e.message ?: "Unknown Error")
                        }
                    }
                }
            }

            is QuranPageEvent.PlayAudio -> {
                val basePath = quranPageEvent.downloadedPath
                    ?: quranPageEvent.serverPath.plus(
                        "${
                            formatTextToGetFileFromServer(
                                quranPageEvent.surahId.toString()
                            )
                        }.mp3"
                    )
                val getSurahName = quranRepository.getSurahNameBySurahId(quranPageEvent.surahId)
                _effect.update {
                    QuranPageEffect.PlayAudio(
                        basePath,
                        quranPageEvent.id,
                        getSurahName.plus(" | ${quranPageEvent.readerName}")
                    )
                }
            }


            is QuranPageEvent.OnCancelDownload -> {
                quranRepository.cancelDownload(quranPageEvent.surahId)
            }

            is QuranPageEvent.DeleteDownloadFile -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    quranRepository.deleteDownloadReaderFile(
                        quranPageEvent.readerId,
                        quranPageEvent.moshaf,
                        quranPageEvent.surahId
                    )
                    quranRepository.deleteSurahDownload(quranPageEvent.path)
                }
            }


            QuranPageEvent.PauseAudio -> {
//                quranRepository.pauseAudio()
            }


            QuranPageEvent.RefreshAudioStatus -> {
                updateMediaStatue()
            }

            QuranPageEvent.CloseQuranPageEvent -> {
                _effect.update {
                    QuranPageEffect.CloseQuranPageEffect
                }
            }

        }
    }

    private fun loadCurrentAudioIfAvailable() {
        try {
            updateMediaStatue()
        } catch (e: Exception) {

        }
    }

    init {
        quranRepository.getCloseables().forEach {
            addCloseable(it)
        }
        if (quranRepository is QuranRepositoryImpl) {
            quranRepository.quranRepEvent = this
        }
        viewModelScope.launch(dispatcherProvider.io) {
            val getDarkMode = quranRepository.getDarkModePage()
            _state.update {
                it.copy(
                    isDarkMode = getDarkMode,
                )
            }
        }
    }

    fun loadPage(pageNum: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            val getSurah = quranRepository.getQuranSurahInfo(pageNum)
            val getAllQuranPages = quranRepository.getAllSurahPagesPath()
            _state.update {
                it.copy(
                    getSurah.pagePath,
                    getSurah.currentPage,
                    SurahItem(getSurah.surahId, getSurah.surahName),
                    getAllQuranPages,
                )
            }
        }
    }
}

sealed class QuranPageEffect {
    data class PlayAudio(val uri: String, val id: String, val title: String) : QuranPageEffect()
    data object UpdateStatus : QuranPageEffect()
    data class ShowError(val message: String) : QuranPageEffect()
    data object CloseQuranPageEffect : QuranPageEffect()
}

sealed class QuranPageEvent {
    data object FetchDataReader : QuranPageEvent()

    data class LoadPage(val pageNum: Int) : QuranPageEvent()

    data class ChangeDarkMode(val darkMode: Boolean) : QuranPageEvent()

    data class GetAllSurahByReader(
        val readerId: Int,
        val currentMoshf: String,
        val surahList: List<String>,
    ) : QuranPageEvent()

    data class OnCancelDownload(val surahId: Int) : QuranPageEvent()
    data class DownLoadSurah(
        val readerId: Int,
        val currentMoshf: String,
        val basePth: String,
        val surahId: Int,
        val readerName: String
    ) : QuranPageEvent()

    data class PlayAudio(
        val id: String,
        val readerName: String,
        val serverPath: String,
        val downloadedPath: String?,
        val surahId: Int,
    ) : QuranPageEvent()

    data object PauseAudio : QuranPageEvent()
    data class DeleteDownloadFile(
        val readerId: Int,
        val moshaf: String,
        val surahId: Int,
        val path: String
    ) : QuranPageEvent()

    data object RefreshAudioStatus : QuranPageEvent()
    data object CloseQuranPageEvent : QuranPageEvent()
}
