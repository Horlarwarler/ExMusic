package com.example.talimlectures.presentation.homeScreen


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.talimlectures.R
import com.example.talimlectures.presentation.LectureScreens.EmptyScreen
import com.example.talimlectures.presentation.LectureScreens.SearchResult
import com.example.talimlectures.composables.BottomBar
import com.example.talimlectures.composables.DisplayDownloaddDialog
import com.example.talimlectures.composables.SearchTopAppBar
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.domain.music.MusicState
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.*


@Composable

fun HomeScreen(
    navController: NavHostController,
    navigateToLecture: () -> Unit,
    homeScreenModel: HomeScreenModel = hiltViewModel(),
    navigateToPlayer: (previousScreenName:String) -> Unit,
){
    val state = homeScreenModel.homeScreenState
    val musicState = homeScreenModel.musicState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true){
        homeScreenModel.initialized()
    }
    LaunchedEffect(key1 =state.isConnected){
        scaffoldState.snackbarHostState.showSnackbar(

        )
    }



    Scaffold(modifier = Modifier
      .fillMaxSize(),
      topBar = {
              SearchTopAppBar(
                  closeIconVisible = state.searchState != SearchAction.IDLE,
                  searchText = state.searchText,
                  onValueChange = {
                            homeScreenModel.handleChangeEvent(HomeScreenEvents.onTextChange(it))
                  },
                  onSearchClick = {
                     homeScreenModel.handleChangeEvent(HomeScreenEvents.onSearchClick)
                  },
                  onCloseActionClick = {
                      if(state.searchText.isEmpty()){
                          homeScreenModel.homeScreenState = homeScreenModel.homeScreenState.copy(searchState = SearchAction.IDLE)
                      }
                      else{
                          homeScreenModel.homeScreenState = homeScreenModel.homeScreenState.copy(searchText = "")
                      }
                  }
              )
      },
      content = {
                val context = LocalContext.current

            DisplayDownloaddDialog(
                display = state.displayDialog ,
                onConfirmClicked = {
                   homeScreenModel.homeScreenState  = homeScreenModel.homeScreenState.copy(displayDialog = false)
                    Toast.makeText(context,"Downloading the lecture ", Toast.LENGTH_SHORT).show()
                    homeScreenModel.handleChangeEvent(HomeScreenEvents.onDownloadClick(
                        state.selectedMusic!!
                    ))
//
                },
                onCloseClicked = {
                    homeScreenModel.homeScreenState  = homeScreenModel.homeScreenState.copy(displayDialog = false)
                },
                lectureName = state.selectedMusic!!)
            HandleContentMenu(
                selectedItems = { selectedItem->
                    if ((musicState.currentLecture != null) && (musicState.currentLecture.lectureTitle == selectedItem)){
                        //Resuming after pausing
                        if (musicState.isPaused){
                            homeScreenModel.handleChangeEvent(HomeScreenEvents.onResumeClicked)
                        }
                        //Pause
                        else{
                            homeScreenModel.handleChangeEvent(HomeScreenEvents.onMusicPause)
                        }
                    }
                    else{
                        homeScreenModel.handleChangeEvent(HomeScreenEvents.onPlayClick(selectedItem))
                    }
                },

                onFavouriteClicked = {
                    homeScreenModel.homeScreenState = homeScreenModel.homeScreenState.copy(selectedFavourite = it)
                    homeScreenModel.handleChangeEvent(HomeScreenEvents.onFavouriteClick(it))
                },
                onDownloadClick = {
                    if(homeScreenModel.downloadState.downloadJobs.containsKey(it)){
                               homeScreenModel.handleChangeEvent(HomeScreenEvents.stopDownload(it))
                    }
                    else{
                        homeScreenModel.handleChangeEvent(HomeScreenEvents.onDownloadClick(it))
                    }
                },
               homeScreenModel = homeScreenModel
            )
      },
      bottomBar = {
          BottomBar(
              navcontroller = navController,
              onClick = { navigateToLecture() },
              currentTime = musicState.currentTime,
              totalTime = musicState.totalTime,
              lecture = musicState.lecture,
              miniPlayerVisible = musicState.miniPlayerVisible,
              miniPlayerIcon =
              if(
                  !musicState.isPaused && musicState.currentLecture != null)R.drawable.pause_icon
              else R.drawable.play_icon,
              onCloseButtonClicked ={
                                    homeScreenModel.miniPlayerAction(PlayAction.STOP)
              },
              onActionClicked = {
                  playAction ->
                 homeScreenModel.miniPlayerAction(playAction)
              },
              navigate = {
                  homeScreenModel.handleChangeEvent(HomeScreenEvents.onNavigate(it))
                  navigateToPlayer(it) },
              displayCurrentTime = musicState.currentTimeDisplay,
              displayTotalTime = musicState.totalTimeDisplay
          )
                  },
      backgroundColor = BackgroundColor
  )
}

@Composable
fun HandleContentMenu(
    selectedItems: (selectedItem: String) -> Unit,
    onFavouriteClicked: (selectedItem: String) -> Unit,
    homeScreenModel: HomeScreenModel,
    onDownloadClick: (selectedItem: String) -> Unit

    ){
    //val searchAction = homeScreenModel.homeScreenState.searchState
    val homeScreenState = homeScreenModel.homeScreenState
    val musicState= homeScreenModel.musicState
    val downloadState = homeScreenModel.downloadState
    when(homeScreenState.searchState){
        SearchAction.TRIGGERED ->{
            if(homeScreenState.searchResult.isEmpty()){
                EmptyScreen()
            }
            else{
                SearchResult(
                    searchLectures = homeScreenState.searchResult,
                    onLectureClicked = selectedItems,
                    miniPlayerVisible = musicState.miniPlayerVisible,
                    onDownloadClicked = {},
                    musicState = musicState,
                    downloadState = downloadState
                )
            }
        }
        else -> {
            ContentMenu(selectedItems = selectedItems,
                onFavouriteClicked = onFavouriteClicked,
                musicState =musicState,
                onDownloadClick =onDownloadClick,
                state = homeScreenState
            )
        }
    }
}

@Composable
fun ContentMenu(
    selectedItems: (selectedItem: String) -> Unit,
    state: HomeScreenState,
    onFavouriteClicked: (selectedItem: String) -> Unit,
    musicState: MusicState,
    onDownloadClick: (selectedItem: String) -> Unit
    ){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp,
                end = 20.dp,
                bottom = if (musicState.playerVisible) 150.dp else 80.dp)
    ) {
        item {
            NewSections(newlyAdded =state.newlyAdded , selectedItems = selectedItems, onDownloadClick =  onDownloadClick)
            RecentSection(recentlyPlayed = state.recentlyPlayed , selectedItems = selectedItems,  onDownloadClick = onDownloadClick)

        }
        item {
            Row {
                FavouriteSection(
                    itemPlayed = selectedItems,
                    onFavouriteClicked = onFavouriteClicked,
                   state = state,
                    musicState = musicState
                    )
            }
        }

    }
}




@Composable
fun NewSections(
    newlyAdded: List<DatabaseLectureModel>,
    selectedItems: (selectedItem: String) -> Unit,
    onDownloadClick: (lectureName: String) -> Unit
    ){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 25.dp)) {
            Text(
                text = "Newly Added",
                fontSize = 15.sp,
                color = TextColor
            )
            Spacer(modifier = Modifier.height(15.dp))
            LazyRow{
               items(
                   items = newlyAdded,
                   key = {
                       item ->
                       item.lectureId
                   }
               ){
                   lecture->
                        LectureItem(item =lecture , onItemClick =selectedItems, onDownloadClick = onDownloadClick  )
               }
            }

        
    }
}
@Composable
fun RecentSection(
    recentlyPlayed: List<DatabaseLectureModel>,
    selectedItems: (selectedItem: String) -> Unit,
    onDownloadClick: (selectedItem: String) -> Unit
    ){

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 25.dp)) {
        Text(
            text = "Recently Played",
            fontSize = 15.sp,
            color = TextColor
        )
        if(recentlyPlayed.isEmpty()){
          EmptyLecture(message = "No lecture has been played")
        }
        else{
            Spacer(modifier = Modifier.height(15.dp))
            LazyRow{
                items(
                    items = recentlyPlayed,
                    key = {
                            item ->
                        item.lectureId
                    }
                ){
                        lecture->
                    LectureItem(item = lecture, onItemClick =selectedItems, onDownloadClick = onDownloadClick )
                }
            }
        }



    }
}
@Composable
fun FavouriteSection(
    itemPlayed: (selectedItem: String) -> Unit,
    onFavouriteClicked: (selectedItem: String) -> Unit,
    state: HomeScreenState,
    musicState: MusicState
){
    val favouritesLecture = state.favourite
        Column(modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Favourite",
                fontSize = 15.sp,
                color = TextColor
            )
            if(favouritesLecture.isEmpty()){
                EmptyLecture(message = "No lecture has been Favourite")
            }
            else{
                Spacer(modifier = Modifier.height(10.dp))
                favouritesLecture.forEach {
                        favourite ->
                    FavouriteItem(
                        itemPlayed = itemPlayed,
                        onFavouriteClicked = onFavouriteClicked,
                        favourite =favourite ,
                        state = state,
                        musicState = musicState
                    )
                }
            }


    }



}
@Composable
fun FavouriteItem(
    itemPlayed: (selectedItem: String) -> Unit,
    onFavouriteClicked: (selectedItem: String) -> Unit,
    favourite: DatabaseLectureModel,
    state: HomeScreenState,
    musicState: MusicState



    ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 5.dp)
            .clickable {
                itemPlayed(favourite.lectureTitle)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1.5f),
            contentAlignment = Alignment.CenterStart

        ) {
            Icon(
                //If a lecture is playing, it should display a pause icon
                painter = painterResource(id =
                    if(state.playingMusic == favourite.lectureTitle && !musicState.isPaused) {
                    R.drawable.pause_icon
                }
                //Then if a lecture is paused then it should display a play icon
                else {
                    R.drawable.play_icon
                }),
                contentDescription = "Play Icon",
                tint = SelectedCategoryColor,

                modifier = Modifier
                    .size(18.dp)
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f)
        ) {
            Text(
                text = favourite.lectureTitle,
                fontSize = 13.sp,
                color = TitleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = favourite.lectureDescription,
                fontSize = 10.sp,
                color = DescriptionColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.favourite),
                contentDescription = "Play Icon",
                tint = FavouriteColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onFavouriteClicked(favourite.lectureTitle)
                    }
                    .align(Alignment.CenterStart)
            )
            Icon(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Download",
                tint = SelectedCategoryColor,
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterEnd)

            )
        }

    }

}

@Composable
fun LectureItem(
    item: DatabaseLectureModel,
    onItemClick:(item:String)->Unit,
    onDownloadClick: (selectedItem: String) -> Unit
){
    Box(modifier = Modifier
        .padding(end = 20.dp)
        .size(110.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(10.dp))
        .background(SearchBackgroundColor)
        .padding(10.dp)){
        Text(
            text = item.lectureTitle,
            maxLines = 2,
            lineHeight = 26.sp,
            color = TextColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,

            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Icon(
            painter = painterResource(id = R.drawable.headset),
            contentDescription = "Headset",
            tint = IconColor,
            modifier = Modifier
                .clickable {
                    onDownloadClick(item.lectureTitle)
                }
                .align(Alignment.BottomStart)
        )
        Text(
            text = "Play",
            color = MiniPlayerBackgroundColor,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(5.dp))
                .background(IconColor)
                .padding(horizontal = 15.dp, vertical = 6.dp)
                .clickable {
                    onItemClick(item.lectureTitle)
                }
        )
    }
}
@Composable
fun EmptyLecture(
    message:String
){
    Text(
        text = message,
        fontSize =15.sp,
        color= TextColor,
        modifier = Modifier.padding(5.dp)
    )
}
