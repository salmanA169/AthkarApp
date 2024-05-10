package com.athkar.sa.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.athkar.sa.R
import com.athkar.sa.retrofit.DownLoad

@Immutable
data class SurahItemsStatus(
    val id:String,
    val readerId: Int,
    val surahName:String,
    val surahId:Int,
    val filePath: String? = null,
    val isPlaying: Boolean = false,
    val progressDownload: DownLoad = DownLoad.None
)
data class SurahFileItemEvent(
    val icon :Int,
    val onClick: () -> Unit
)
@Composable
fun SurahReaderItems(
    fileStatus:SurahItemsStatus,
    onPlayClick:(Int)->Unit,
    onDelete:(Int,String)->Unit ,
    onCancelClick:(Int) -> Unit,
    onClick:(Int)->Unit
) {

    val itemEvent  = remember(fileStatus){
        if (fileStatus.filePath!= null){
            SurahFileItemEvent(R.drawable.mark_icon,{onDelete(fileStatus.surahId,fileStatus.filePath)})
        }else{
            when(fileStatus.progressDownload){
                is DownLoad.Progress -> SurahFileItemEvent(R.drawable.cancel_24px__1_,{onCancelClick(fileStatus.surahId)})
                else -> SurahFileItemEvent(R.drawable.icons8_download,{onClick(fileStatus.surahId)})
            }
        }
    }
    val playItem = remember(fileStatus){
        if (fileStatus.isPlaying){
            SurahFileItemEvent(R.drawable.pause_circle_24px,{onPlayClick(fileStatus.surahId)})
        }else{
            SurahFileItemEvent(R.drawable.octicon_play,{
                Log.d("QuranPage", "SurahReaderItems: called compose play surah info : $fileStatus")
                onPlayClick(fileStatus.surahId)})
        }
    }
    OutlinedCard(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = fileStatus.surahName,modifier = Modifier.padding(horizontal = 4.dp))
            Spacer(modifier = Modifier.weight(1f))
            FilledTonalIconButton(onClick = playItem.onClick) {
                Icon(painter = painterResource(id = playItem.icon), contentDescription = null)
            }
            FilledTonalIconButton(onClick = itemEvent.onClick)  {
                Icon(painter = painterResource(id = itemEvent.icon), contentDescription = null)
            }
        }
        if (fileStatus.progressDownload is DownLoad.Progress){
            LinearProgressIndicator(
                progress = { fileStatus.progressDownload.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderItem(
    nameReader: String,
    readerId:Int,
    onReaderClick:(Int)->Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), onClick = {
                onReaderClick(readerId)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Image(
                painter = painterResource(id = R.drawable.quran_image),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp, MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = nameReader)
        }
    }
}