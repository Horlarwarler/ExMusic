package com.crezent.design_system.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.common.util.MusicIconEvent
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.R
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.models.MediaItem

@OptIn(ExperimentalMaterial3Api::class,)
@Composable
fun BottomMiniPlayer(
    modifier: Modifier = Modifier,
    sheetState: BottomSheetScaffoldState,
    progress:Float,
    mediaItem: MediaItem,
    navigateToPlayer:() -> Unit,
    imageLoader: ImageLoader,
    onActionClick: (MusicIconEvent) -> Unit,
    isPlaying: Boolean = true
){


   BottomSheetScaffold(
       modifier = modifier,
       scaffoldState = sheetState,
       sheetPeekHeight = 56.dp,
       sheetDragHandle = {

       },
       sheetContainerColor =MaterialTheme.colorScheme.primaryContainer ,
       sheetShape = RectangleShape,
       sheetContent =  {
            SheetContent(
                modifier = modifier,
                progress = progress,
                mediaItem = mediaItem,
                navigateToPlayer = navigateToPlayer,
                imageLoader = imageLoader,
                onActionClick = onActionClick,
                isPlaying = isPlaying
            )
       })
   {
   }
}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    progress:Float,
    mediaItem: MediaItem,
    navigateToPlayer:() -> Unit,
    imageLoader:ImageLoader,
    onActionClick: (MusicIconEvent) ->Unit,
    isPlaying:Boolean = false
){
    val painter = rememberAsyncImagePainter(
        imageLoader = imageLoader,
        model = mediaItem.thumbnail,
        placeholder = painterResource(id =  R.drawable.small_girl),
        error =  painterResource(id =  R.drawable.small_girl),
        )
    val playOrPauseIcon = if (isPlaying) R.drawable.pause else R.drawable.play
    val backGroundLineColor = MaterialTheme.colorScheme.background
    val progressLineColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier
        .padding(start = 5.dp)
        .fillMaxWidth()
        .height(3.dp)
        ){
        val progressPosition = size.width * progress
        val startYValue = 51.dp.toPx()
        drawLine(
            color = backGroundLineColor,
            start = Offset(x = 0F, y =startYValue),
            end = Offset(x=size.width, y = startYValue),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Butt
        )
        drawLine(
            color = progressLineColor,
            start = Offset(x = 0F, y =startYValue),
            end = Offset(x=progressPosition, y = startYValue),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = progressLineColor,
            radius = 3.dp.toPx(),
            center = Offset(x = progressPosition + 2.dp.toPx(), y = startYValue)
        )
    }


    Row(
        modifier = modifier
            .wrapContentHeight()

            .fillMaxWidth()
            .clickable {
                navigateToPlayer()
            }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start =  5.dp,end= 5.dp,),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(24.dp)
                .clickable {
                    onActionClick(MusicIconEvent.STOP)
                }
            ,
            imageVector = ExMusicIcons.close,
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.primary,
        )
        Image(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .clip(RoundedCornerShape(15.dp))
                .size(40.dp)
            ,
            painter =painter,
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
            ,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mediaItem.title?:"",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Text(
                text =mediaItem.artist?:"",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
        }
        Icon(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(24.dp)
                .clickable {
                    onActionClick(MusicIconEvent.PREVIOUS)
                }

            ,
            painter = painterResource(id = R.drawable.previous),
            contentDescription = "Play",
            tint = MaterialTheme.colorScheme.primary,

        )

        Icon(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(24.dp)
                .clickable {
                    onActionClick(MusicIconEvent.PLAY_PAUSE)
                }
            ,
            painter = painterResource(id =playOrPauseIcon),
            contentDescription = "Play",
            tint = MaterialTheme.colorScheme.primary,

            )
        Icon(
            modifier = Modifier
                .padding(end = 10.dp)
                .size(24.dp)
                .clickable {
                    onActionClick(MusicIconEvent.NEXT)
                }
            ,
            painter = painterResource(id = R.drawable.next),
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.primary,
            )
    }


}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun preview(){
    ExMusicTheme {
        val context = LocalContext.current
        val imageLoader = ImageLoader(context = context)
        val sheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = false
        )
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = SheetState(
                initialValue = SheetValue.PartiallyExpanded,
                skipHiddenState = false,
                skipPartiallyExpanded = false
            )
        )
        val mediaItem = MediaItem(
            title = "Title",
            description = "Description 1",
            artist = "Artist"

        )


        BottomMiniPlayer(
            sheetState = scaffoldState,
            imageLoader = imageLoader,
            mediaItem =mediaItem ,
            progress = 0.4f,
            navigateToPlayer = {},
            onActionClick = {}
            )

    }

}