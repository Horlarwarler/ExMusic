package com.crezent.creator_presentation.upload_dashboard

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.design_system.components.AppBar
import com.crezent.design_system.theme.ExMusicTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UploadDashboardScreen(
    state: UploadDashboardState,
    event:(UploadDashboardEvent) -> Unit,
    snackbarHostState: SnackbarHostState

){


   // val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val head = state.error

    var onScreenMessage by remember {
        mutableStateOf<String?>(null)
    }

    var index by remember {
        mutableIntStateOf(1)
    }
    LaunchedEffect(head){
        if (head == null){
            Log.d("Temp","Error empty")
            return@LaunchedEffect
        }
        scope.launch {
            snackbarHostState.showSnackbar(
                "ERROR ${head.value}"
            )
            event(UploadDashboardEvent.RemoveShownMessage)
        }
    }
    LaunchedEffect(key1 = onScreenMessage){
        if (onScreenMessage == null){
            return@LaunchedEffect
        }
        scope.launch {
            snackbarHostState.showSnackbar(
                message = onScreenMessage!!,
                duration = SnackbarDuration.Short
            )
            onScreenMessage = null
        }


    }

    val topText by remember(index) {
        mutableStateOf("$index of Step 3")

    }

    val onBackButtonClick : ()  -> Unit = {
        if (index in 2..3){
            index--
        }
    }

    val audioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = {
            uri: Uri? ->


            if (uri == null){
                onScreenMessage = "No Audio File Selected"
                return@rememberLauncherForActivityResult
            }
            event(UploadDashboardEvent.SelectAudioFile(uri))
        }
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = {
                uri: Uri? ->

            if (uri == null){
                onScreenMessage = "No Image File Selected"
                return@rememberLauncherForActivityResult
            }
            event(UploadDashboardEvent.SelectThumbnailFile(uri))
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (index != 4){
                AppBar(
                    topText = topText,
                    showBackButton = index != 1,
                    onActionClick = onBackButtonClick,
                    style = MaterialTheme.typography.labelSmall
                )
                when (index){
                    1 ->{

                        val isEnabled = state.selectedAudioUri != null && state.error == null
                        UploadStepOne(
                            choseFile = {
                                audioPicker.launch("audio/*")
                            },
                            onPreview = {
                                event(UploadDashboardEvent.PreviewSelectedAudio)
                            },
                            mediaAttribute = state.audioAttribute,
                            isEnabled = isEnabled,
                            onNext = {
                                index++
                            }
                        )
                    }
                    2 -> {
                        val isEnabled = state.description.isNotEmpty() && state.title.isNotEmpty() && state.error == null
                        UploadStepTwo(
                            descriptionValue =state.description,
                            titleValue = state.title,

                            onDescriptionChange = {
                                    description ->
                                event(UploadDashboardEvent.DescriptionChange(description))
                            },
                            onTitleChange = {
                                    title->
                                event(UploadDashboardEvent.TitleChange(title))
                            },
                            onNextButtonClick =  {
                                index++
                            },
                            nextButtonEnabled = isEnabled,

                            )
                    }
                    3 -> {
                        UploadStepThree(
                          //  selectedUri = state.selectedThumbnailUri,
                            choseFile = {
                                imagePicker.launch("image/*")
                            },
                            onRemove = {
                                event(UploadDashboardEvent.RemoveThumbnailFile)
                            },
                            onUpload = {
                                index ++
                                // event(UploadDashboardEvent.)
                            },
                            selectedByteArray = state.imageByteArray
                        )
                    }
                    else ->{

                    }
                }

            }
        }
    }

}

//write a composable function for a card with a text and an icon

//@Composable
//fun ProgressCard(
//    upload: CurrentUpload,
//
//){
//    val context = LocalContext.current
//
//    val uri = upload.uriString?.let {
//        Uri.parse(it)
//    }
//    Column {
//        Spacer(modifier = Modifier.height(smallSpacer))
//        Row(
//            modifier = Modifier
//                .padding(vertical = 5.dp)
//                .fillMaxWidth()
//                .height(100.dp)
//            ,
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            val request = ImageRequest.Builder(context)
//                .data(uri)
//                .placeholder(R.drawable.logo)
//                .scale(Scale.FIT)
//                .build()
//            val painter = rememberAsyncImagePainter(model = request )
//
//            Image(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(10.dp))
//                    .size(80.dp),
//                painter = painter,
//                contentDescription = ""
//            )
//            Column(
//                modifier = Modifier,
//                verticalArrangement = Arrangement.spacedBy(5.dp)
//            ) {
//
//                Text(
//                    text = upload.title,
//                    style = MaterialTheme.typography.h3,
//                    color = MaterialTheme.colors.onBackground
//                )
//                Text(
//                    text = upload.uploadDate,
//                    style = MaterialTheme.typography.body2,
//                    color = MaterialTheme.colors.onBackground,
//                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(5.dp),
//                    modifier = Modifier
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_access_time_24),
//                        tint = MaterialTheme.colors.onPrimary,
//                        contentDescription = "Download"
//                    )
//
//                    Text(
//                        text = upload.elapsedTime ,
//                        style = MaterialTheme.typography.button,
//                        color = MaterialTheme.colors.onPrimary,
//                        fontWeight = FontWeight.Bold
//
//                    )
//                }
//
//
//            }
//
//
//        }
//
//        Spacer(modifier = Modifier.height(smallSpacer))
//        Divider(Modifier.height(1.dp))
//
//
//    }
//
//}

@Preview(showBackground = true)
@Composable
private fun preview(

){
    ExMusicTheme(darkTheme = false) {
        val topText = "1 of Step 3"
        val index  = 0
        val onBackButtonClick : () -> Unit = {}


    }

}

