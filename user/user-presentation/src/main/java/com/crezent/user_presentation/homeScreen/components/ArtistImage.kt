package com.crezent.user_presentation.homeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.R
import com.crezent.models.User

@Composable
fun ArtistImage(
    modifier: Modifier = Modifier,
    user:User? = null,
    imageLoader :ImageLoader,
    onImageClicked:(String) ->Unit = {},

){
    val painter = rememberAsyncImagePainter(
        imageLoader = imageLoader,
        model = user?.profilePicture,
        placeholder = painterResource(id = R.drawable.small_girl),
        error = painterResource(id = R.drawable.small_girl)
    )
    Image(
        modifier = modifier
            .clickable {
                if (user == null) return@clickable
                onImageClicked(user.username)
            }
            .size(48.dp)
            .clip(CircleShape)
        ,
        painter = painter,
        contentDescription = "Profile Picture",
        contentScale = ContentScale.Crop
    )
}