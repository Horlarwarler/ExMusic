package com.crezent.user_presentation.music.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.theme.mediumSpacer
import com.crezent.user_presentation.R

@Composable
fun MusicSongItem(
    song: com.crezent.models.Song,
    imageLoader: ImageLoader,
    onClick:() ->Unit,

    ){
    var expand by remember { mutableStateOf(false) }

    val painter : Painter = rememberAsyncImagePainter(
        model = song.thumbnailUrl,
        imageLoader =  imageLoader,
        placeholder = painterResource(id = R.drawable.background)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = 5.dp)

        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .size(80.dp),
                painter =painter,
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
                ,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = song.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = song.artistUsername,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Icon(
                modifier = Modifier.clickable {
                    expand = !expand
                    onClick()
                },
                imageVector = Icons.Rounded.MoreHoriz,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Download"
            )

        }
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.mediumSpacer))

    }

}