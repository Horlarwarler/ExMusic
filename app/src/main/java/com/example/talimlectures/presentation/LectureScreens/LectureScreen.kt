package com.example.talimlectures.presentation.LectureScreens


import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.talimlectures.R
import com.example.talimlectures.composables.BottomBar
import com.example.talimlectures.composables.DisplayDownloaddDialog
import com.example.talimlectures.composables.LectureAppBar
import com.example.talimlectures.data.model.DatabaseCategoryModel
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.domain.download.DownloadInterfaceState
import com.example.talimlectures.domain.music.MusicState
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.SearchAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun LectureScreen(
    navController: NavHostController,
    lectureScreenModel: LectureScreenModel= hiltViewModel(),
    navigateToHome:()->Unit,
    navigateToPlayer: (previousScreenName: String) -> Unit
){
    LaunchedEffect(key1 = true ){
        lectureScreenModel.initialized()
    }
    val state = lectureScreenModel.lectureScreenState
    val musicState = lectureScreenModel.musicState

    val scope  = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = BackgroundColor,
        modifier =Modifier.fillMaxSize(),
        topBar = {
            LectureAppBar(
                searchBarState = state.searchState,
                closeIconVisible =state.searchState != SearchAction.IDLE ,
                searchText =state.searchText ,
                onValueChange = {
                    lectureScreenModel.handleChangeEvent(LectureScreenEvent.onTextChange(it))
                                },
                onSearchClick ={
                   lectureScreenModel.handleChangeEvent(LectureScreenEvent.onSearchClick)
                } ,
                onCloseActionClick = {
                    if(state.searchText.isEmpty()){
                        lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(searchState = SearchAction.IDLE)
                    }
                    else{
                        lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(searchText = "")
                    }
                },
                onSearchButtonClicked = {
                    lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(searchState = SearchAction.OPEN)
                }
            )

        },
        content = {
            val context = LocalContext.current
            DisplayDownloaddDialog(
                display = state.displayDialog ,
                onConfirmClicked = {
                    lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(displayDialog = false)
                    Toast.makeText(context,"Downloading the lecture ", Toast.LENGTH_SHORT).show()
                    lectureScreenModel.handleChangeEvent(LectureScreenEvent.onDownloadClick(
                        state.selectedMusic!!
                    ))
//
                },

                onCloseClicked = {
                    lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(displayDialog = false)
                },
                lectureName = state.selectedMusic!!)
                HandleLectureContent(
                    onCategorySelected ={
                        lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(categorySelected = it)

                    } ,
                    onLectureClicked ={selectedLecture->
                        if (lectureScreenModel.lectureScreenState.searchState != SearchAction.IDLE){
                            lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(searchState = SearchAction.IDLE)
                        }
                        if ((musicState.currentLecture != null) && (musicState.currentLecture.lectureTitle == selectedLecture)){
                            //Resuming after pausing
                            if (musicState.isPaused){
                                lectureScreenModel.handleChangeEvent(LectureScreenEvent.onResumeClicked)
                            }
                            //Pause
                            else{
                                lectureScreenModel.handleChangeEvent(LectureScreenEvent.onMusicPause)
                            }
                        }
                        else{
                            lectureScreenModel.handleChangeEvent(LectureScreenEvent.onPlayClick(selectedLecture))
                        }

                    },

                    onDownloadClicked = {lecture->
                        lectureScreenModel.handleChangeEvent(LectureScreenEvent.onDownloadClick(lecture))
                        lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(displayDialog = true)

                    },
                    lectureScreenModel = lectureScreenModel
                )
        },
        bottomBar = {
            BottomBar(
                navcontroller = navController,
                onClick = {
                    navigateToHome()
                    lectureScreenModel.lectureScreenState = lectureScreenModel.lectureScreenState.copy(searchState = SearchAction.IDLE)
                          },

                lecture = lectureScreenModel.musicState.currentLecture,
                miniPlayerVisible = lectureScreenModel.musicState.miniPlayerVisible,
                miniPlayerIcon =if(lectureScreenModel.musicState.isPaused)R.drawable.play_icon else R.drawable.pause_icon,
                onCloseButtonClicked ={
                    lectureScreenModel.handleChangeEvent(LectureScreenEvent.onStop)
                },
                onActionClicked = {action->
                    lectureScreenModel.miniPlayerAction(action)
                },
                navigate = navigateToPlayer,

                currentTime = lectureScreenModel.musicState.currentTime,
                totalTime = lectureScreenModel.musicState.totalTime,
                displayCurrentTime = lectureScreenModel.musicState.currentTimeDisplay,
                displayTotalTime = lectureScreenModel.musicState.totalTimeDisplay
            )
        }
    )
}
@Composable
fun HandleLectureContent(
    onCategorySelected: (Int) -> Unit,
    onLectureClicked: (selectedLecture:String) -> Unit,
    lectureScreenModel: LectureScreenModel,
    onDownloadClicked: (selectedLecture:String) -> Unit,

){
    val lectureScreenState = lectureScreenModel.lectureScreenState
    val musicState = lectureScreenModel.musicState

    when (lectureScreenState.searchState) {
        SearchAction.IDLE -> {
            ContentScreen(
                categoryLectures = lectureScreenState.lectures,
                onCategorySelected = onCategorySelected,
                onLectureClicked = onLectureClicked,
                categories = lectureScreenState.categories,
                selectedCategory = lectureScreenState.categorySelected,
                miniPlayerVisible = musicState.miniPlayerVisible,
                onDownloadClicked = onDownloadClicked,
                lectureScreenModel = lectureScreenModel
            )

        }
        else ->{
           if(lectureScreenState.lectures.isEmpty()){
               EmptyScreen()
           }
            else {
             SearchResult(
                 searchLectures =lectureScreenState.lectures ,
                 onLectureClicked =onLectureClicked,
                 miniPlayerVisible = musicState.miniPlayerVisible,
                 onDownloadClicked = onDownloadClicked,
                 musicState = musicState,
                 downloadState = lectureScreenModel.downloadState
             )
            }

        }

    }
}
@Composable
fun ContentScreen(
    categoryLectures: List<DatabaseLectureModel>,
    onCategorySelected: (Int) -> Unit,
    onLectureClicked: (selectedLecture:String) -> Unit,
    categories: List<DatabaseCategoryModel>,
    selectedCategory: Int?,
    miniPlayerVisible:Boolean,
    lectureScreenModel: LectureScreenModel,

    onDownloadClicked: (selectedLecture:String) -> Unit,
){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = if (miniPlayerVisible) 150.dp else 70.dp,
                top = 20.dp,
                end = 30.dp,
                start = 20.dp)
        ){
            CategoryItems(databaseCategoryModel = categories , onCategorySelected =onCategorySelected , selected =selectedCategory )
            SongList(
                lectures = categoryLectures,
                onLectureClicked = onLectureClicked,
                modifier = Modifier.fillMaxSize(),
                onDownloadClicked = onDownloadClicked,
               musicState = lectureScreenModel.musicState,
                downloadState = lectureScreenModel.downloadState
            )

        }

}
@Composable
fun CategoryItems(
    databaseCategoryModel: List<DatabaseCategoryModel>,
    onCategorySelected:(Int)->Unit,
    selected: Int?
){

    val lazyState = rememberLazyListState()
    LazyRow(
        state = lazyState,

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ){
        items(
            items = databaseCategoryModel,
            key = {
                category->
                category.categoryId
            }
        ){


            categoryItem->

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val visibility = selected == categoryItem.categoryId
                Text(
                    text = categoryItem.categoryName,
                    color =  if (
                        categoryItem.categoryId == selected)
                        SelectedCategoryColor else TextColor,
                    modifier = Modifier
                        .clickable {
                            onCategorySelected(categoryItem.categoryId)
                        }
                        .padding(top = 5.dp,
                            end = if (databaseCategoryModel[databaseCategoryModel.size - 1].categoryId != categoryItem.categoryId) 8.dp else 0.dp,
                            bottom = 5.dp)
                )
                if (visibility){
                    Box(
                        modifier = Modifier.height(5.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Canvas(modifier = Modifier.fillMaxWidth()){
                            drawLine(
                                color = SelectedCategoryColor,
                                start = Offset(x = 0F, y = 0F),
                                end = Offset(x = 80f, y = 0f),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round

                            )

                        }
                    }
                }

            }


        }
    }
}
@Composable
fun SongList(
    modifier: Modifier,
    lectures:List<DatabaseLectureModel>,
    onLectureClicked: (selectedLecture: String) -> Unit,
    onDownloadClicked: (selectedLecture: String) -> Unit,
    musicState: MusicState,
    downloadState: DownloadInterfaceState

){
    LazyColumn(
        modifier =modifier,
    ){
        items(
            items =lectures,
            key = {
                lecture->
                lecture.lectureId
            }
        ){
            lecture ->
            SongItem(
                lecture = lecture,
                onLectureClicked = onLectureClicked,

                onDownloadClicked = onDownloadClicked,
                musicState = musicState,
                downloadState = downloadState
            )
        }
    }
}
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SongItem(
    lecture:DatabaseLectureModel,
    onLectureClicked: (selectedLecture: String) -> Unit,
    onDownloadClicked:(selectedItem:String) -> Unit,
    musicState: MusicState,
    downloadState:DownloadInterfaceState


)
{


    var alpha by remember {
        mutableStateOf(1f)
    }

//    LaunchedEffect(key1 = sharedViewModel.downloadAnimation.value,key2 =downloadingLectureId){
//        alpha = 0.1f
//        delay(2000)
//        alpha = 1f
//    }
    val alphaState by animateFloatAsState(
        targetValue = alpha,
        infiniteRepeatable(animation = tween(2000), repeatMode = RepeatMode.Reverse)
    )

    Row(modifier = Modifier
        .clickable {
            onLectureClicked(lecture.lectureTitle)
        }
        .fillMaxWidth()
        .padding(top = 15.dp, bottom = 5.dp),

        
        horizontalArrangement = Arrangement.SpaceBetween)
    {


        Column(
            modifier = Modifier.weight(8f)
        ) {
            Text(
                text =lecture.lectureTitle,
                fontSize = 13.sp,
                color = TitleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = lecture.lectureDescription,
                fontSize = 10.sp,
                color = DescriptionColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(2f)){
            Icon(
                painter = painterResource(id =
                if(musicState.currentLecture == lecture && !musicState.isPaused) {
                    R.drawable.pause_icon
                }
                //Then if a lecture is paused then it should display a play icon
                else {
                    R.drawable.play_icon
                }),
                contentDescription = "Play Icon",
                tint = SelectedCategoryColor,

                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterStart)

            )
            Icon(
                painter = painterResource(id = if(downloadState.downloadJobs.containsKey(lecture.lectureTitle))R.drawable.download else R.drawable.downloded ),
                contentDescription = "Download",
                tint = SelectedCategoryColor,
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        if (downloadState.downloadJobs.containsKey(lecture.lectureTitle)){
                                onDownloadClicked(lecture.lectureTitle)
                        }
//
                    }
                    .alpha(
                       if (downloadState.downloadJobs.containsKey(lecture.lectureTitle)) alphaState else 1f
                    )

            )
        }

    }
}

@Composable
fun SearchResult(
    searchLectures:List<DatabaseLectureModel>,
    onLectureClicked: (selectedMusic: String) -> Unit,
    miniPlayerVisible: Boolean,
    onDownloadClicked: (selectedMusic: String) -> Unit,
    musicState: MusicState,
    downloadState: DownloadInterfaceState

){
    SongList(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = if (miniPlayerVisible) 150.dp else 70.dp),
        lectures = searchLectures,
        onLectureClicked =onLectureClicked,
        onDownloadClicked = onDownloadClicked,
        musicState = musicState,
        downloadState = downloadState
    )

}
@Composable
fun EmptyScreen(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Icon(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "empty",
            modifier = Modifier.size(240.dp),
            tint = IconColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "No Lecture Found ", fontSize = 24.sp, color = TextColor)
    }
}


fun DisplaySnackBar(
    scope:CoroutineScope,
    message:String,
    scaffoldState: ScaffoldState
){
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = message
        )
    }
}
@Preview
@Composable
fun previewItem(){
    EmptyScreen()
}