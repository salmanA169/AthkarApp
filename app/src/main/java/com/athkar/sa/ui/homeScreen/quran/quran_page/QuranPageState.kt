package com.athkar.sa.ui.homeScreen.quran.quran_page

import androidx.compose.runtime.Immutable
import com.athkar.sa.components.SurahItemsStatus
import com.athkar.sa.quran.dto.Reciter

@Immutable
data class QuranPageState(
    val quranPagePath:String= "",
    val currentPage:String = "",
    val surah: SurahItem = SurahItem(0,""),
    val allQuranPagesPath:List<String> = listOf(),
    val isDarkMode:Boolean = false,
    val quranReader:List<Reciter> = emptyList(),
    val isLoadingFetchReader:Boolean = false,
    val getAllSurahReader:List<SurahItemsStatus> = emptyList()
)
