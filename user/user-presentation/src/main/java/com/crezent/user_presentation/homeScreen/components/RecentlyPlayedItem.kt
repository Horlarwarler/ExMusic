package com.crezent.user_presentation.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.models.RecentlyPlayed

@Composable
fun RecentlyPlayedItem(
    recentlyPlayed: RecentlyPlayed,
    onItemClick:(item:String)->Unit,
    onDownloadClick: (selectedItem: String) -> Unit
){
    Box(modifier = Modifier
        .padding(end = 20.dp)
        .size(110.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.background)
        .padding(10.dp)){
        Text(
            text = recentlyPlayed.title,
            maxLines = 2,
            lineHeight = 26.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,

            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Icon(
            imageVector = Icons.Rounded.Headset,
            contentDescription = "Headset",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable {
                    onDownloadClick(recentlyPlayed.songId)
                }
                .align(Alignment.BottomStart)
        )
        Text(
            text = "Play",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 15.dp, vertical = 6.dp)
                .clickable {
                    onItemClick(recentlyPlayed.songId)
                }
        )
    }
}

@Preview
@Composable
fun Test(){
    Text(text = "hello")
}