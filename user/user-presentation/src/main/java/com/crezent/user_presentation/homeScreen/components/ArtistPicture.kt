package com.crezent.user_presentation.homeScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.models.User

@Composable
fun ArtistPicture(
    modifier: Modifier = Modifier,
    user: User,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onBackground,
    pictureSize: Dp = 100.dp,
    imageLoader: ImageLoader,
    onClick:() -> Unit
){
    Column(
        modifier = modifier
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val painter : Painter = rememberAsyncImagePainter(
            model = user.profilePicture,
            //  placeholder = painterResource(id = com.crezent.common.R.drawable.person),
            //   error = painterResource(id = com.crezent.common.R.drawable.person),
            imageLoader = imageLoader
        )
        ArtistImage(
            modifier = Modifier.size(pictureSize),
            imageLoader =imageLoader,
            user = user
        )

        Text(
            modifier = Modifier.padding(vertical = 3.dp),
            text = user.displayName,
            color = color,
            style = style
        )

    }

}