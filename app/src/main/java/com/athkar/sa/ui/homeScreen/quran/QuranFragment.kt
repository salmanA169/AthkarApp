package com.athkar.sa.ui.homeScreen.quran

import AthkarAppTheme
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.exitUntilCollapsedScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.athkar.sa.MainViewModel
import com.athkar.sa.R
import com.athkar.sa.db.entity.SurahDownloadEntity
import com.athkar.sa.uitls.createMediaMetaData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuranFragment : Fragment() {

    val controller by lazy { findNavController() }
//    private val controller = findNavController()

    private val viewModel by viewModels<QuranViewModel>()

    private val mainViewModel by activityViewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val effect by viewModel.effect.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = effect) {
                    when (effect) {
                        is QuranEffect.NavigateQuranPageEffect -> {
                            controller.navigate(
                                QuranFragmentDirections.actionQuranFragmentToQuranPage(
                                    (effect as QuranEffect.NavigateQuranPageEffect).pageNum
                                )
                            )
                            viewModel.resetEffect()
                        }

                        null -> {

                        }

                        is QuranEffect.PlayAudioEffect -> {
                            val castEffect = effect as QuranEffect.PlayAudioEffect
                            mainViewModel.playAudio(
                                castEffect.title,
                                castEffect.uri,
                                createMediaMetaData(castEffect.title)
                            )
                        }

                        QuranEffect.ClosePageEffect -> {
                            controller.popBackStack()
                        }
                    }
                }
                LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
                    viewModel.checkQuranData()
                }
                CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
                    QuranScreen(state, viewModel::onEvent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuranScreen(
    quranState: QuranState,
    onEvent: (QuranEvent) -> Unit
) {

    val scrollBe = exitUntilCollapsedScrollBehavior()

    val quranScreens = remember {
        QuranPagers.entries
    }
    val pagerState = rememberPagerState {
        quranScreens.size
    }
    val selectedIndex by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }
    val scope = rememberCoroutineScope()
    AthkarAppTheme {
        Scaffold(topBar = {
            Column {
                CenterAlignedTopAppBar(
                    scrollBehavior = scrollBe,
                    title = { Text(text = "القراّن") },
                    navigationIcon = {
                        IconButton(onClick = {
                            onEvent(QuranEvent.ClosePageEvent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_icon),
                                contentDescription = ""
                            )
                        }
                    })
                TabRow(selectedTabIndex = selectedIndex) {
                    Tab(selected = selectedIndex == 0, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    }, text = {
                        Text(text = "السورة")
                    })
                    Tab(selected = selectedIndex == 1, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }, text = {
                        Text(text = "المفضلة")
                    })
                    Tab(selected = selectedIndex == 2, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }, text = {
                        Text(text = "التحميلات")
                    })
                }
            }
        }) {

            if (quranState.showAlertDialog) {
                AlertDialog(onDismissRequest = { }, confirmButton = {
                    TextButton(onClick = { onEvent(QuranEvent.StartDownload) }) {
                        Text(text = "تحميل")
                    }
                }, icon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.icons8_download),
                            contentDescription = ""
                        )
                    }
                }, title = { Text(text = "تحميل البيانات") }, text = {
                    Text(text = "نحتاج تحميل بعض الملفات المهمة للقرأن,إذا تجاهلت ذلك لايمكن التطبيق العمل بشكل جيد")
                })
            }

            if (quranState.dialogStatus.showDialogProgress && quranState.dialogStatus.progressStatusInfo != null) {
                BasicAlertDialog(onDismissRequest = { /*TODO*/ }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.icons8_download),
                                contentDescription = "Download icon",
                                modifier = Modifier
                                    .size(38.dp)
                                    .align(Alignment.CenterHorizontally),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (quranState.dialogStatus.isExtractingFiles) "جاري استخراج الملفات..." else "جاري تحميل الملفات....",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(26.dp))
                            if (quranState.dialogStatus.isExtractingFiles) {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth(),
                                    strokeCap = StrokeCap.Round
                                )
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = quranState.dialogStatus.progressStatusInfo.downloadedSize,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = quranState.dialogStatus.progressStatusInfo.fileSize,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { quranState.dialogStatus.progressStatusInfo.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(6.dp),
                                    strokeCap = StrokeCap.Round
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                when (quranScreens[it]) {
                    QuranPagers.SURAH -> {

                        SurahScreen(
                            nestedScrollConnection = scrollBe.nestedScrollConnection,
                            quranState = quranState, onEvent = onEvent
                        )

                    }

                    QuranPagers.FAVORITE -> {

                    }

                    QuranPagers.DOWNLOADS -> {
                        DownloadsScreen(
                            allSurahDownload = quranState.surahDownloads,
                            onEvent = onEvent,
                            nestedScrollConnection = scrollBe.nestedScrollConnection
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadsScreen(
    allSurahDownload: List<SurahDownloadEntity>,
    onEvent: (QuranEvent) -> Unit, nestedScrollConnection: NestedScrollConnection
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var currentReaderId by remember {
        mutableIntStateOf(-1)
    }
    var currentUri by remember {
        mutableStateOf<String?>(null)
    }
    var currentMoshaf by remember {
        mutableStateOf<String?>(null)

    }
    var surahId by remember {
        mutableIntStateOf(-1)
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
            currentUri = null
            currentMoshaf = null
            currentReaderId = -1
            surahId = -1
        }, confirmButton = {
            TextButton(onClick = {
                onEvent(
                    QuranEvent.DeleteSurah(
                        surahId,
                        currentUri ?: return@TextButton,
                        currentMoshaf ?: return@TextButton,
                        currentReaderId
                    )
                )
                showDialog = false
            }) {
                Text(text = "تأكيد")
            }
        }, title = {
            Text(text = "هل أنت متأكد من حذف السورة")
        })
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        items(allSurahDownload) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .combinedClickable(onLongClick = {
                        currentMoshaf = it.moshaf
                        currentUri = it.surahPath
                        currentReaderId = it.readerId
                        surahId = it.surahId
                        showDialog = true
                    }) {
                        onEvent(QuranEvent.PlayAudio(it.title, it.surahPath))

                    },
                shape = MaterialTheme.shapes.extraLarge,

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape
                            )
                            .size(50.dp)
                    ) {
                        Text(
                            text = it.surahId.toString(),
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(text = it.title)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahScreen(
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection,
    quranState: QuranState,
    onEvent: (QuranEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
//        item {
//            if (quranState.katmahState == null) {
//                Button(
//                    onClick = { },
//                    contentPadding = PaddingValues(12.dp),
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(25)
//                ) {
//                    Text(text = "إنشاء ختمة")
//                }
//            } else {
//                ElevatedCard(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(6.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = "من قوله تعالى")
//                        Text(text = "الورد الحالي")
//                        Text(text = "الجزء الأول")
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(6.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = "الحمدالله رب العالمين")
//                        IconButton(onClick = { /*TODO*/ }) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.forward_fill_icon),
//                                contentDescription = ""
//                            )
//                        }
//
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    LinearProgressIndicator(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(20.dp)
//                            .padding(6.dp),
//                        progress = quranState.katmahState.calculateProgress(),
//                        strokeCap = StrokeCap.Round,
//                        trackColor = MaterialTheme.colorScheme.surface
//                    )
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(6.dp),
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = "الأوراد السابقة:${quranState.katmahState.previousWard}")
//                        Text(text = "الأوراد التالية:${quranState.katmahState.nextWard}")
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//        }
        items(quranState.allSurah) {
            SurahItems(
                numberSurah = it.id,
                nameSurah = it.name,
                verseCount = it.verseNumber,
                it.pageNumb,
                onEvent
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahItems(
    numberSurah: Int,
    nameSurah: String,
    verseCount: Int,
    pageNum: Int,
    onEvent: (QuranEvent) -> Unit
) {
    ElevatedCard(
        onClick = { onEvent(QuranEvent.NavigateQuranPage(numberSurah)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp), shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    )
                    .size(50.dp)
            ) {
                Text(
                    text = numberSurah.toString(),
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "سوره $nameSurah")
                Text(text = "عدد الايات $verseCount")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = pageNum.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true, locale = "AR",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SurahPreview() {
    AthkarAppTheme {
//        QuranScreen(navC)
    }
}