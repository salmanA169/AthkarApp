package com.athkar.sa.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.athkar.sa.R
import com.athkar.sa.quran.dto.Reciter
import com.athkar.sa.ui.homeScreen.quran.quran_page.QuranPageEvent
import com.athkar.sa.ui.homeScreen.quran.quran_page.SurahItem


val colorBarLight = Color(0xffD8DAE0)
val colorBarDark = Color(0xff32353A)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LayoutControlBar(
    modifier: Modifier = Modifier,
    surahInfo: SurahItem,
    initialPage: Int,
    allPages: List<String>,
    getReader: List<Reciter>,
    isDarkTheme: Boolean = false,
    onEvent: (QuranPageEvent) -> Unit,
    isLoadingFetchReader: Boolean = false,
    allSurahReader: List<SurahItemsStatus>
) {

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()

    var shouldShow by remember {
        mutableStateOf(true)
    }
    val pagerState = rememberPagerState(initialPage = initialPage) {
        allPages.size
    }
    var currentPage by remember {
        mutableIntStateOf(0)
    }
    val lazyState = rememberLazyListState()
    var currentReaderId by remember {
        mutableIntStateOf(-1)
    }
    val currentReader = remember(currentReaderId) {
        getReader.find { it.id == currentReaderId }
    }
    var currentMoshaf by remember(currentReaderId) {
        mutableStateOf(currentReader?.moshaf?.getOrNull(0))
    }

    var currentSurahIdSelected by remember {
        mutableIntStateOf(-1)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    var currentPath by remember{
        mutableStateOf<String?>(null)
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
            currentSurahIdSelected = -1
            currentPath = null
        }, confirmButton = {
            TextButton(onClick = {
                onEvent(
                    QuranPageEvent.DeleteDownloadFile(
                        currentReader!!.id,
                        currentMoshaf!!.name,
                        currentSurahIdSelected,
                        currentPath?:""
                    )
                )
                showDialog = false
            }) {
                Text(text = "حذف")
            }
        }, title = {
            Text(text = "هل انت متأكد لحذف الملف ؟")
        })
    }

    LaunchedEffect(key1 = currentMoshaf) {
        snapshotFlow {
            currentMoshaf
        }.collect { moshaf ->
            moshaf?.let {
                onEvent(
                    QuranPageEvent.GetAllSurahByReader(
                        currentReaderId,
                        it.name,
                        it.surah_list.split(",")
                    )
                )
            }
        }
    }
    LaunchedEffect(key1 = true) {
        snapshotFlow { pagerState.currentPage }
            .collect {
                onEvent(QuranPageEvent.LoadPage(it + 1))
                currentPage = it + 1
            }
    }
    Scaffold(modifier = modifier, topBar = {
        AnimatedVisibility(visible = shouldShow, enter = fadeIn(), exit = fadeOut()) {
            CenterAlignedTopAppBar(
                title = { Text(text = surahInfo.surahName) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                ), actions = {
                    IconButton(onClick = { onEvent(QuranPageEvent.ChangeDarkMode(!isDarkTheme)) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.circum_dark),
                            contentDescription = ""
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {onEvent(QuranPageEvent.CloseQuranPageEvent) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    }, bottomBar = {
        AnimatedVisibility(visible = shouldShow, enter = fadeIn(), exit = fadeOut()) {
            BottomAppBar {
                IconButton(onClick = {
                    showBottomSheet = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.octicon_play),
                        contentDescription = "",
                    )
                }
            }
        }
    }) {
        it
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                LaunchedEffect(key1 = true) {
                    onEvent(QuranPageEvent.FetchDataReader)
                }

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Crossfade(targetState = currentReaderId, label = "") {
                        when (it) {
                            -1 -> {
                                if (isLoadingFetchReader) {
                                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                }
                                LazyColumn(state = lazyState, content = {
                                    items(getReader, key = { it.id }, contentType = { it.id }) {
                                        ReaderItem(
                                            nameReader = it.name,
                                            readerId = it.id,
                                            onReaderClick = {
                                                currentReaderId = it
                                            }
                                        )
                                    }
                                })
                            }

                            else -> {
                                var expandMenu by remember {
                                    mutableStateOf(false)
                                }
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp)
                                    ) {
                                        FilledTonalIconButton(
                                            onClick = { currentReaderId = -1 },
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.back_icon),
                                                contentDescription = ""
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))

                                        Card(modifier = Modifier
                                            .height(42.dp)
                                            .width(150.dp), onClick = {
                                            expandMenu = true
                                        }) {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                Text(
                                                    text = currentMoshaf?.name ?: "",
                                                    modifier = Modifier.align(Alignment.Center),
                                                    maxLines = 1
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandMenu,
                                                onDismissRequest = { expandMenu = false }) {
                                                currentReader?.moshaf?.forEach {
                                                    DropdownMenuItem(
                                                        text = { Text(text = it.name) },
                                                        onClick = {
                                                            currentMoshaf = it
                                                            expandMenu = false
                                                        })
                                                }
                                            }
                                        }

                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        contentPadding = PaddingValues(4.dp)
                                    ) {
                                        allSurahReader.let { allSurah ->
                                            items(allSurah) { surahFile ->
                                                SurahReaderItems(surahFile, onPlayClick = {
                                                    onEvent(
                                                        QuranPageEvent.PlayAudio(
                                                            surahFile.id,
                                                            currentReader!!.name,
                                                            currentMoshaf!!.server,
                                                            surahFile.filePath,
                                                            it
                                                        )
                                                    )
                                                }, onCancelClick = {
                                                    onEvent(QuranPageEvent.OnCancelDownload(it))
                                                }, onDelete = { id,uri->
                                                    showDialog = true
                                                    currentSurahIdSelected = id
                                                    currentPath = uri
                                                }) {
                                                    onEvent(
                                                        QuranPageEvent.DownLoadSurah(
                                                            currentReader!!.id,
                                                            currentMoshaf!!.name,
                                                            currentMoshaf!!.server,
                                                            it,
                                                            currentReader.name
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalPager(state = pagerState, key = { it }) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    shouldShow = !shouldShow
                }
                .background(MaterialTheme.colorScheme.surface)) {
                AsyncImage(
                    model = allPages[it],
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = if (isDarkTheme) ColorFilter.tint(Color.White) else null
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = surahInfo.surahName, fontSize = 18.sp)
                }

                Text(
                    text = currentPage.toString(), modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .systemBarsPadding(), fontSize = 20.sp
                )
            }
        }
    }
}