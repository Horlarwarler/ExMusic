package com.crezent.creator_presentation.upload_file

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.crezent.models.TaskStatus

@Suppress("UnusedMaterialScaffoldPaddingParameter")
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UploadFileScreen(
    state: UploadFileState,
    snackBarHostState: SnackbarHostState,
    navigateToProfile: (String) -> Unit,
    navigateToSettings:() ->Unit,
    event:(UploadFileEvent) -> Unit,
    navigateBack: () -> Unit,
    onNavigate :  (String) -> Unit,
){

    Log.d("LOG", "Upload  ${state.uploadingFile}")

    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val enableInput = state.uploadingFile?.status == TaskStatus.NONE || state.uploadingFile == null

    LaunchedEffect(key1 = state.error){
        state.error?:return@LaunchedEffect


        val errorMessage = state.error.value
        Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
        event(UploadFileEvent.RemoveShownMessage)

    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //Top Bar
        val user = state.user
        com.crezent.design_system.components.ProfileAppbar(
            modifier = Modifier.align(Alignment.TopCenter),
            onProfileSelected = {
                navigateToProfile(user!!.username)
            },
            onSettingsPressed = navigateToSettings,
            onBackButtonPressed = navigateBack,
            text = "Add Files",
            profileUrl = user?.profilePicture
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = com.crezent.design_system.theme.mediumSpacer, horizontal = com.crezent.design_system.theme.mediumSpacer)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 80.dp)
            ){
                item {
                    UploadFileSection(
                        onThumbnailSelected = {
                            event(UploadFileEvent.EditThumbnail(it))
                        },
                        onSongSelected =  {
                            event(UploadFileEvent.EditSong(it))
                        },
                        progress = state.uploadingFile?.progress?:0,
                        taskStatus = state.uploadingFile?.status?: com.crezent.models.TaskStatus.NONE,
                        songName = state.songName,
                        thumbnailUri = state.thumbnailUri,
                        enable = enableInput,
                        fileName = state.thumbnailName
                    )
                }
                item {
                    TrackInformation(
                        title = state.title?:"",
                        description = state.description?:"",
                        editDescription ={
                            event(UploadFileEvent.EditDescription(it))
                        },
                        editTitle = {
                            event(UploadFileEvent.EditTitle(it))
                        },
                        enable = enableInput
                    )
                }
            }
            ///  val intent = Intent(context, UploadService::class.java)

            com.crezent.design_system.components.CustomButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = com.crezent.design_system.theme.largeSpacer),
                text = "Submit",
                enabled = state.uploadButtonEnabled && enableInput
            ) {
                event(UploadFileEvent.Upload)
            }
        }
        com.crezent.design_system.components.BottomBarNavigation(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            currentRoute = "route",
            onNavigate = onNavigate
        )


    }

}

//write a composable function for a card with a text and an icon

@Composable
fun UploadFileSection(
    onThumbnailSelected:(Uri?) -> Unit,
    onSongSelected:(Uri?) -> Unit,
    songName:String? =null,
    thumbnailUri:Uri? = null,
    taskStatus: com.crezent.models.TaskStatus,
    progress: Int = 0,
    enable:Boolean = true,
    fileName: String? = null,
){

  
    val imageLauncher = rememberLauncherForActivityResult(
        contract =ActivityResultContracts.GetContent(),
        onResult = {
            it?.let(onThumbnailSelected)
        }
    )

    val songLauncher = rememberLauncherForActivityResult(
        contract =ActivityResultContracts.GetContent(),
        onResult = {
            it?.let(onSongSelected)
        }
    )

    Column(
        modifier = Modifier
            //.background(MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
            .padding(com.crezent.design_system.theme.mediumSpacer),
        verticalArrangement = Arrangement.spacedBy(com.crezent.design_system.theme.largeSpacer)
    ) {
        if (thumbnailUri == null){
//            UploadPlaceHolderCard(
//                enable = enable,
//                icon = com.crezent.common.R.drawable.person
//                ){
//                if (taskStatus == com.crezent.models.TaskStatus.NONE){
//                    imageLauncher.launch("image/*")
//                }
//            }
        }
        else{
            val painter = rememberAsyncImagePainter(model = thumbnailUri)
            Box(modifier  = Modifier
                .clickable(enabled = enable) {
                    imageLauncher.launch("image/*")
                }
                .fillMaxWidth()
                .height(200.dp)
                ){
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(enabled = enable) {
                            imageLauncher.launch("image/*")
                        }
                        .fillMaxSize()
                    ,
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
//                Icon(
//                    modifier = Modifier
//                        .clickable {
//                            onThumbnailSelected(null)
//                        }
//                        .align(Alignment.TopEnd)
//                        .padding(5.dp),
//
//                    painter = painterResource(id = com.crezent.common.R.drawable.close),
//                    contentDescription =""
//                )
                fileName?.let {
//                    Text(
//                        modifier = Modifier
//                            .background(MaterialTheme.colors.background)
//                            .padding(5.dp)
//                            .align(Alignment.BottomStart),
//                        text = fileName,
//                        style = MaterialTheme.typography.body2,
//                        color = MaterialTheme.colors.onBackground
//                    )
                }

            }


        }

        if (songName == null){
//            UploadPlaceHolderCard(
//                topText = "Select Audio ",
//                descriptionText = "Pick Audio From Your Phone Storage",
//                icon = com.crezent.common.R.drawable.person,
//                enable = enable
//            ){
//                songLauncher.launch("audio/*")
//            }
        }
        else{
        //    Log.d("LOG", "Upload Screen filestatus $fileStatus and progress $progress  ")

//            UploadProgressCard(fileName = songName, progress = progress, enable = enable) {
//                if (taskStatus == com.crezent.models.TaskStatus.NONE){
//                    songLauncher.launch("audio/*")
//                }
//
//            }
        }

    }

}
@Composable
fun TrackInformation(
    modifier: Modifier = Modifier,
    title:String,
    description:String,
    editTitle:(String) ->Unit,
    editDescription:(String) ->Unit,
    enable: Boolean = true
){
    Column(
        modifier = modifier
            .padding(bottom = 100.dp, top = com.crezent.design_system.theme.smallSpacer)
            .fillMaxWidth()
          //  .background(MaterialTheme.colors.surface.copy(0.3f), RoundedCornerShape(15.dp))
            .padding(com.crezent.design_system.theme.mediumSpacer)
        ,

    ) {

        com.crezent.design_system.components.CustomFieldTitle(title = "Track Information")
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))
//        Text(
//            text = "Title",
//            style = MaterialTheme.typography.body2,
//            fontSize = 11.sp,
//            color = MaterialTheme.colors.onSurface
//        )
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))
        com.crezent.design_system.components.CustomInputField(
            value = title,
            placeHolderText = "Song Title",
            onValueChange = editTitle,
            enabled = enable
        )
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))

//        Text(
//            text = "Description",
//            style = MaterialTheme.typography.body2,
//            fontSize = 11.sp,
//            color = MaterialTheme.colors.onSurface
//        )
        Spacer(modifier = Modifier.height(com.crezent.design_system.theme.smallSpacer))
        com.crezent.design_system.components.CustomInputField(
            value = description,
            placeHolderText = "Song Description",
            onValueChange = editDescription,
            singleLine = false,
            enabled = enable
        )

    }
}

@Composable
fun UploadPlaceHolderCard(
    topText:String = "Upload Thumbnail",
    descriptionText: String ="Aspect Ratio Square 1:1",
    icon:Int ,
    enable: Boolean = true,
    onClick: () -> Unit = {},
    ){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enable) {
                onClick()
            }
            .drawBehind {
                val stroke = Stroke(
                    width = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                )
                drawRoundRect(
                    color = Color.White.copy(0.5f),
                    cornerRadius = CornerRadius(20f),
                    style = stroke
                )
            }
        ,
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = com.crezent.design_system.theme.mediumSpacer, horizontal = com.crezent.design_system.theme.mediumSpacer)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                modifier = Modifier
//                   .padding(end = smallSpacer)
//                    .background(MaterialTheme.colors.primary, RoundedCornerShape(2.dp))
//                    .padding(3.dp)
//                ,
//                painter = painterResource(id = icon),
//                contentDescription = "Upload",
//                tint = MaterialTheme.colors.onPrimary
//
//            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)

            ) {
//                Text(
//                    text = topText,
//                    style = MaterialTheme.typography.body1,
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colors.onSurface
//                )
//                Text(
//                    text = descriptionText,
//                    style = MaterialTheme.typography.body2,
//                    fontSize = 10.sp,
//
//                    lineHeight = 30.sp,
//                    color = MaterialTheme.colors.onSurface
//                )
            }
        }
    }
}

//@Composable
//fun UploadProgressCard(
//    fileName:String = "Audio_file3292.mp3",
//    progress: Int =0,
//    icon: Int = com.crezent.common.R.drawable.music,
//    enable: Boolean = true,
//    onClick: () -> Unit,
//    ){
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(enable) {
//                onClick()
//            },
//        backgroundColor = MaterialTheme.colors.primary,
//        elevation = 3.dp,
//        border = BorderStroke(0.dp, MaterialTheme.colors.primary),
//        shape = RoundedCornerShape(15.dp)
//    ) {
//        Column(
//            modifier = Modifier
//            //.padding(vertical = mediumSpacer, horizontal = mediumSpacer)
//            ,
//            verticalArrangement = Arrangement.spacedBy(15.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = icon),
//                contentDescription = "Music Icon",
//                tint = MaterialTheme.colors.onPrimary
//            )
//            Text(
//                text = fileName,
//                style = MaterialTheme.typography.body1,
//                fontSize = 12.sp,
//                color = MaterialTheme.colors.onSurface
//            )
//             Spacer(modifier = Modifier.height(smallSpacer))
//
//            if (progress != 0){
//                HorizontalProgressBar(progress = progress )
//                Spacer(modifier = Modifier.height(smallSpacer))
//            }
//            Text(
//                text = if (progress==0)"" else "$progress%" ,
//                style = MaterialTheme.typography.body2,
//                fontSize = 10.sp,
//                color = MaterialTheme.colors.onSurface
//            )
//        }
//    }
//}


//@Composable
//fun HorizontalProgressBar(progress: Int){
//    val progressToFloat = (progress/100.0).toFloat()
//    val offsetX  by animateFloatAsState(targetValue = progressToFloat, label = "")
//    Canvas(modifier = Modifier
//        .height(2.dp)
//        .fillMaxWidth()
//        ,){
//        drawLine(
//            color = Color.Gray,
//            start = Offset(x= 0F, y = 0F),
//            end = Offset(x= size.width, y = 0F),
//            cap = StrokeCap.Round,
//            strokeWidth = 30f
//        )
//        drawLine(
//            color = Color.White,
//            start = Offset.Zero,
//            end = Offset(x= size.width* offsetX, y = 0F),
//            cap = StrokeCap.Round,
//            strokeWidth = 30f
//        )
//    }
//}





//@Preview
//@Composable
//private fun Preview(){
////    ExMusicTheme(darkTheme = true) {
////        var progress = 0.4f
////        UploadFileScreen(
////            state =  UploadFileState() ,
////            navigateToProfile = {} ,
////            navigateToSettings = { /*TODO*/ },
////            event = {}
////        ) {
////
////        }
////
////    }
//
//}

