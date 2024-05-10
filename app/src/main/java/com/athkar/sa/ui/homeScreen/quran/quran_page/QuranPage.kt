package com.athkar.sa.ui.homeScreen.quran.quran_page

import AthkarAppTheme
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.athkar.sa.MainViewModel
import com.athkar.sa.components.LayoutControlBar
import com.athkar.sa.uitls.createMediaMetaData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuranPage : Fragment() {


    private val TAG=  javaClass.simpleName
    private val args by navArgs<QuranPageArgs>()
    private val viewModel by viewModels<QuranPageViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val controller by lazy {
        findNavController()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                AthkarAppTheme(state.isDarkMode) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        LayoutControlBar(
                            modifier = Modifier.fillMaxSize(),
                            surahInfo = state.surah,
                            initialPage = args.pageNum-1,
                            allPages = state.allQuranPagesPath,
                            isDarkTheme = state.isDarkMode,
                            getReader = state.quranReader,
                            onEvent = viewModel::onEvent,
                            isLoadingFetchReader = state.isLoadingFetchReader,
                            allSurahReader = state.getAllSurahReader
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPage(args.pageNum)
        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect{
                when(it){
                    is QuranPageEffect.PlayAudio ->{
                        val getCurrentMedia = mainViewModel.getCurrentMediaInfo()
                        if (getCurrentMedia != null && getCurrentMedia.mediaId == it.id){
                            val isPlay =mainViewModel.updateStateAudio()
                            viewModel.updateAudioItemState(it.id,!isPlay)
                        }else{
                            mainViewModel.playAudio(it.id,it.uri, createMediaMetaData(it.title))
                            viewModel.updateAudioItemState(it.id,true)
                            viewModel.resetAudioItemState(it.id)
                        }
                    }


                    null -> {}
                    QuranPageEffect.UpdateStatus -> {
                        val getCurrentMedia = mainViewModel.getCurrentMediaInfo()
                        getCurrentMedia?.let {
                            viewModel.resetAudioItemState(it.mediaId)
                        }
                    }

                    is QuranPageEffect.ShowError -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    QuranPageEffect.CloseQuranPageEffect -> {
                        controller.popBackStack()
                    }
                }
                viewModel.resetEffect()
            }
        }
    }
}