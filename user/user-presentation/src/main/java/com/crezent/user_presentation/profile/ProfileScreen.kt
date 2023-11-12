package com.crezent.user_presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.CustomAppBar
import com.crezent.design_system.components.ElevatedRoundedButton
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.design_system.theme.smallSpacer
import com.crezent.models.PersonalPlaylist
import com.crezent.models.User
import com.crezent.user_presentation.R
import com.crezent.user_presentation.homeScreen.components.ArtistPicture
import com.crezent.user_presentation.profile.components.PlaylistCard


@Composable
fun ProfileScreen(
    state: ProfileState,
    imageLoader: ImageLoader,
    event:(ProfileEvent) -> Unit,
    navigateBack:() -> Unit,
    updateProfile:() -> Unit,
){
    val profile = state.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = smallPadding, vertical = smallPadding)
        ,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        CustomAppBar(
            //title = stringResource(id = R.string.profile),
            leftAction = {
                Icon(
                    modifier = Modifier.clickable {
                       navigateBack()
                    },
                    imageVector = ExMusicIcons.backButton,
                    contentDescription = "Navigate Back",
                )

            }
        )
       // Spacer(modifier = Modifier.height(smallPadding))
        ArtistPicture(
            user = state.user!!,
            imageLoader = imageLoader,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        ) {}
        Spacer(modifier = Modifier.height(smallPadding))
        val onButtonClick : () -> Unit = {
            if (state.isCurrentUser){
              updateProfile()
            }
            else {
                //TODO
            }
        }
        val buttonText = if (state.isCurrentUser){
            "Update Profile"
        }
        else {
            "Follow"
        }

        ElevatedRoundedButton(
            onButtonClick =onButtonClick ,
            buttonText = buttonText
        )
        Spacer(modifier = Modifier.height(smallSpacer))
        UserInfoSection(profile = state.user)

        Spacer(modifier = Modifier.height(smallSpacer))

        if (!state.isCurrentUser ){
            return@Column
        }
        PlaylistSession(playlists = state.playlists, imageLoader = imageLoader)

    }



}

@Composable
private fun UserInfoSection(
    profile:User?
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DataCount(count = profile?.followers?.size?:0, name = "Followers" )
            DataCount(count = profile?.following?.size?:0, name = "Following" )
            DataCount(count = profile?.following?.size?:0, name = "Test" )
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}
@Composable
private fun DataCount(
    count:Int,
    name:String
){
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,

        )
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer

        )
    }
}

@Composable private fun PlaylistSession(
    playlists:List<PersonalPlaylist>,
    imageLoader: ImageLoader
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = mediumPadding)
    ) {

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(id = R.string.personalPlaylist),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        LazyColumn(){
            items(items = playlists){
                playlist ->
                PlaylistCard(
                    playlist = playlist,
                    imageLoader
                ){
                    downloadAction ->
                    //TODO
                }

            }
        }
    }
}
@Preview
@Composable
private fun PreviewProfile(){
    ExMusicTheme {
        val user1 = User(username = "Mikail", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
        val playlist = PersonalPlaylist(songId = "Testing", artistName = "Ramadan", name = "Playlist One", songUrl = "Tesvting", thumbnail = "Test")
       val playlists = listOf(playlist,playlist,playlist,playlist)
        val state = ProfileState(
            user = user1,
            playlists = playlists,
            isCurrentUser = true
        )
        val context = LocalContext.current
        val imageLoader = ImageLoader(context = context)
        ProfileScreen(state = state,
            imageLoader =imageLoader ,
            event = {}, navigateBack = { /*TODO*/ },


        ) {
            
        }
    }
}

//fun startService(context:Context, playlist:PersonalPlaylist, action:String){
//    val intent = Intent()
//    val songTitle = playlist.name
//    val songId = playlist.songId
//    val songUrl = playlist.songUrl
////    intent.apply {
////        putExtra(SONG_ID, songId)
////        putExtra(SONG_TITLE, songTitle)
////        putExtra(AUDIO_URL, songUrl)
////        putExtra(DOWNLOAD_ACTION, action)
////    }
////    context.startService(intent)
//}