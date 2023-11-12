package com.crezent.user_presentation.music.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.theme.smallSpacer
import com.crezent.user_presentation.R
import java.util.Locale

@Composable
fun ArtistBox(
    user: com.crezent.models.User,
    imageLoader: ImageLoader,

    onClick: () -> Unit,
){

    Column(
        modifier = Modifier.clickable {
            onClick()
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val painter = rememberAsyncImagePainter(
            model = user.profilePicture,
            imageLoader =imageLoader,
            placeholder = painterResource(id = R.drawable.background),
            error = painterResource(id = R.drawable.background)
        )
        Image(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            painter = painter,
            contentDescription = "Logo",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))
        Text(
            text = user.username.capitalize(Locale.ROOT),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))

    }
}