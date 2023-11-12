package com.crezent.user_presentation.music

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.crezent.design_system.components.AppBar
import com.crezent.user_presentation.music.components.ArtistBox
import com.crezent.user_presentation.music.components.MusicSongItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MusicScreen(
    event:(MusicEvent) -> Unit,
    state: MusicScreenState,
    onNavigate:(String) -> Unit,
    navigateToProfile: (String) -> Unit,
    navigateToPlayer: (songId:String) -> Unit,
    currentRoute:String,
    imageLoader: ImageLoader

){
    val context = LocalContext.current
    var currentIndex by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = state.errors){
        if (state.errors.isNotEmpty()){
            Toast.makeText(context, state.errors[0], Toast.LENGTH_SHORT).show()
            event(MusicEvent.RemoveShownMessage)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ){
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            topText = "Music"
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(vertical = com.crezent.design_system.theme.mediumSpacer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = com.crezent.design_system.theme.mediumSpacer),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0..2){
                    val text = when(i){
                        0 ->{
                            "Songs"
                        }
                        1 -> {
                            "Artist"
                        }
                        2 ->{

                            "Playlist"
                        }

                        else -> {
                            "Songs"
                        }
                    }
                    val isSelected = currentIndex == i
                    TextButton(
                        onClick ={
                            currentIndex = i
                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected)MaterialTheme.colorScheme.primary else Color.Transparent
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(0.5f))
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 15.dp, vertical =3.dp ),
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected)MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(com.crezent.design_system.theme.largeSpacer))
            MusicScreenContent(
                state = state,
                currentIndex = currentIndex,
                navigateToProfile = navigateToProfile,
                navigateToPlayer = navigateToPlayer,
                imageLoader = imageLoader

            )
        }
        com.crezent.design_system.components.BottomBarNavigation(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )

    }


}


@Composable
fun MusicScreenContent(
    state: MusicScreenState, currentIndex: Int,
    navigateToProfile: (String) -> Unit,
    navigateToPlayer: (songId:String) -> Unit,
    imageLoader: ImageLoader


) {
    when(currentIndex){
        0 ->{
            SongsContent(
                songs = state.songs,
                imageLoader = imageLoader,
                navigateToPlayer = navigateToPlayer
            )
        }
        1 -> {
            ArtistContent(
                artists = state.artists,
                imageLoader = imageLoader,
                navigateToProfile = navigateToProfile)
        }
        
        2 ->{
          //  PlaylistContent(playlists = state.playlists)
        }
    }
}

@Composable
fun SongsContent(
    songs:List<com.crezent.models.Song>,
    navigateToPlayer: (songId:String) -> Unit,
    imageLoader: ImageLoader
){
    LazyColumn(
        modifier = Modifier
            .padding(start = com.crezent.design_system.theme.mediumSpacer, end = com.crezent.design_system.theme.mediumSpacer, bottom = com.crezent.design_system.theme.largeSpacer)
    ){
        items(songs){
            song ->
            MusicSongItem(
                song =  song,
                imageLoader = imageLoader,

            ){
                navigateToPlayer(song.songId)
            }
        }
    }

}

@Composable
fun DialogAction(
    actionClick :() -> Unit,
    icon:Int,
    actionText:String
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            actionClick()
        }
    ) {
        Icon(painter = painterResource(id = icon), contentDescription =actionText )
        Text(text = actionText, style = MaterialTheme.typography.bodyMedium)
    }
}
@Composable
 fun ArtistContent(
    artists:List<com.crezent.models.User>,
    navigateToProfile: (String) -> Unit,
    imageLoader: ImageLoader
){
     val state = rememberLazyGridState()
     LazyVerticalGrid(
         columns = GridCells.Fixed(2),
         horizontalArrangement = Arrangement.SpaceBetween,
         state = state,
         ){
            items(artists){
                ArtistBox(
                    user = it,
                    imageLoader = imageLoader
                    ){
                    navigateToProfile(it.username)
                }
            }
     }

 }


