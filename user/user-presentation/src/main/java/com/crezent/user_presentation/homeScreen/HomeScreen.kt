package com.crezent.user_presentation.homeScreen


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.BottomBarNavigation
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.smallPadding
import com.crezent.models.RecentlyPlayed
import com.crezent.models.Song
import com.crezent.models.User
import com.crezent.user_presentation.R
import com.crezent.user_presentation.homeScreen.components.ArtistImage
import com.crezent.user_presentation.homeScreen.components.ArtistPicture
import com.crezent.user_presentation.homeScreen.components.SectionHeader


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun HomeScreen(
    state: HomeScreenState,
    snackBarHostState:SnackbarHostState,
    imageLoader: ImageLoader,
    navigateTo: (String) -> Unit,
    navigateToProfile:(String) -> Unit,
    event: (HomeScreenEvents) -> Unit,

){
    val bottomPadding = 60.dp
    LaunchedEffect(key1 = state.errors){
        if (state.errors.isNotEmpty()){
            val currentMessage = state.errors[0]
            snackBarHostState.showSnackbar(message = currentMessage, duration = SnackbarDuration.Short )
            event(HomeScreenEvents.RemoveShownMessage)
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(
                    start = smallPadding,
                    end = smallPadding,
                    bottom = bottomPadding,
                    top = smallPadding
                )
        ) {

            HomeScreenAppBar(
                user =state.loggedInUser,
                imageLoader = imageLoader,
                onProfileSelected =  navigateToProfile
            )

            ArtistSection(
                followedArtist = state.followedArtist,
                recommendedArtist = state.recommendedArtist,
                onClick = navigateToProfile,
                imageLoader = imageLoader
            )
            if (state.recentlyPlayed.isNotEmpty()){
                RecentSection(
                    recents =state.recentlyPlayed ,
                    imageLoader = imageLoader)
            }
            TopDailyPlayList(
                trendingSongs =state.trendingSongs,
                imageLoader = imageLoader
            )

        }
        BottomBarNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            onNavigate =navigateTo )

    }


}
@Composable
private fun HomeScreenAppBar(
    modifier: Modifier = Modifier,
    user: User? = null,
    imageLoader: ImageLoader,
    onProfileSelected: (username:String) -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //Text
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            val displayName = user?.displayName?:"Guest"
            Text(
                text = "Welcome, $displayName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp
            )
            Text(
                text = stringResource(id = R.string.homePageSecondTitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        ArtistImage(
            user= user,
            imageLoader =imageLoader
        ) {
            onProfileSelected(it)
        }
    }


}


@Composable
private fun ArtistSection(
    followedArtist : List<User>,
    recommendedArtist:List<User> = emptyList(),
    onClick: (String) -> Unit,
    imageLoader: ImageLoader
){
    val lazyState = rememberLazyListState()
    Column(
    ) {
        val noArtistFollowed = followedArtist.isEmpty()
        val title = if (noArtistFollowed) "Recommended Artist To Follow" else "Followed Artist"
        val artists = if (noArtistFollowed) recommendedArtist else followedArtist
        SectionHeader(title = title){

        }
        LazyRow(
            state = lazyState,
        ){
            itemsIndexed(
                items =  artists,

            ){
             index, item ->
                val paddingStart = if (index == 0)0.dp else 5.dp
                val paddingEnd = if (index == followedArtist.size-1)0.dp else 5.dp
                ArtistPicture(
                    modifier = Modifier
                        .padding(start = paddingStart, end = paddingEnd),
                    user = item,
                    imageLoader = imageLoader ) {
                    onClick(item.username)
                }
            }
        }
    }
}



@Composable
private fun RecentSection(
    recents : List<RecentlyPlayed>,
    imageLoader: ImageLoader
){
    val lazyState = rememberLazyListState()
    Column(
    ) {
        SectionHeader(
            title = "Recently Played",
        ){
        }
        LazyRow(
            state = lazyState,
        ){
            itemsIndexed(
                items =  recents,

                ){
                    index, recent ->
                val paddingStart = if (index == 0)0.dp else 5.dp
                val paddingEnd = if (index == recents.size-1)0.dp else 5.dp
                MusicCard(
                    modifier = Modifier.padding(start = paddingStart, end = paddingEnd),
                    recent = recent,
                    imageLoader = imageLoader
                )


            }
        }
    }
}
@Composable
private fun MusicCard(
    modifier: Modifier = Modifier,
    recent: RecentlyPlayed,
    imageLoader: ImageLoader
){
    val painter = rememberAsyncImagePainter(
        imageLoader = imageLoader,
        model = recent.thumbnail,
        placeholder = painterResource(id = com.crezent.design_system.R.drawable.small_girl),
        error = painterResource(id = com.crezent.design_system.R.drawable.small_girl)
    )
    Column(
        modifier = modifier
            .width(120.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .size(120.dp)
                .fillMaxSize(),
            painter = painter,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(vertical = 3.dp),
            text = recent.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
            )

    }
}

@Composable
private fun TopDailyPlayList(
    trendingSongs:  List<Song>,
    imageLoader: ImageLoader
){
    Column {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Trending",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        LazyColumn(

        ){
            itemsIndexed(items = trendingSongs ){
                index: Int, item: Song ->
                TrendingCard(
                    song = item,
                    imageLoader = imageLoader
                ){
                    shouldPlay ->

                }

            }
        }
    }
}

@Composable
private fun TrendingCard(
    song: Song,
    imageLoader: ImageLoader,
    isPlaying:Boolean = false,
    mediaActionClick: (Boolean) -> Unit
){
    val painter = rememberAsyncImagePainter(
        imageLoader = imageLoader,
        model = song.thumbnailUrl,
        placeholder = painterResource(id = com.crezent.design_system.R.drawable.small_girl),
        error = painterResource(id = com.crezent.design_system.R.drawable.small_girl)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(65.dp),
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
                modifier = Modifier.padding(vertical = 3.dp),
                text = song.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(vertical = 3.dp),
                text =song.artistUsername,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,


            )
        }
        if (!isPlaying){
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        mediaActionClick(true)
                        //
                    },
                imageVector =ExMusicIcons.playIcon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Download"
            )
        }
        else {

        }

    }
}


@Composable
fun EmptyLecture(
    message:String
){
    Text(
        text = message,
        fontSize =15.sp,
        color= MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(5.dp)
    )
}
@Preview(showBackground = true)
@Composable
private fun previewComposable(){
    val user1 = User(username = "Mikail", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
    val user2 = User(username = "Mikail", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
    val user3 = User(username = "Mikail", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
    val user4 = User(username = "Mikail", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
    val song1 = Song(songId = "abcdedf", title = "Mikail", description = "Description", artistUsername = "User1", audioUrl = "", date = "", length = 1.2)
    val song2 = Song(songId = "abcdef", title = "Mikail", description = "Description", artistUsername = "User1", audioUrl = "", date = "", length = 1.2)
    val song3 = Song(songId = "abcdes", title = "Mikail", description = "Description", artistUsername = "User1", audioUrl = "", date = "", length = 1.2)
    val recent = RecentlyPlayed(songId = "Abc", description = "Description", thumbnail = "", title = "Bismillah Rahman Raheem")
    val followedArtist = listOf(user1, user2, user3, user4,user1, user2, user3, user4,)
    val songs = listOf(song1,song2,song3,song1,song2,song3,song1,song2,song3)
    val recents = listOf(recent,recent,recent,recent,recent,recent,recent,recent,recent)
    val context = LocalContext.current
    val imageLoader = ImageLoader(context = context)
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val state = HomeScreenState(followedArtist = followedArtist, trendingSongs = songs, recentlyPlayed = recents)
    ExMusicTheme {
        HomeScreen(
            state = state,
            snackBarHostState = snackbarHostState,
            imageLoader = imageLoader,
            navigateTo = {},
            navigateToProfile ={} ,
            event = {}
        )
    }

}