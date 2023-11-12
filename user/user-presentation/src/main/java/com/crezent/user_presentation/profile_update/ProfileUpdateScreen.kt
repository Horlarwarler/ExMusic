package com.crezent.user_presentation.profile_update

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.ImageLoader
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.CustomAppBar
import com.crezent.design_system.components.CustomInputField
import com.crezent.design_system.components.DefaultAppBarTitle
import com.crezent.design_system.components.ElevatedRoundedButton
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largerCornerRadius
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.models.User
import com.crezent.user_presentation.R
import com.crezent.user_presentation.homeScreen.components.ArtistImage

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileUpdateScreen(
    state: ProfileUpdateState,
    imageLoader: ImageLoader,
    event: (ProfileUpdateEvent) -> Unit,
    navigateToLogin:() -> Unit,
    navigateToOnboard:() -> Unit,
    snackBarHostState: SnackbarHostState
){

    val user = state.user
    val userRemovedPicture = state.userRemovedPicture




    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = state.updateSuccessful){
        if (state.updateSuccessful){
            snackBarHostState.showSnackbar(
                message = "Profile Updated",
                duration = SnackbarDuration.Short
            )
        }
    }
    LaunchedEffect(key1 = state.errors){
        if (state.errors.isNotEmpty()){
            val message =  state.errors[0]
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            event(ProfileUpdateEvent.RemoveShownMessage)
        }
    }
    val profilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                context.contentResolver.openInputStream(it).use {
                    stream ->
                    if (stream == null){
                        return@rememberLauncherForActivityResult
                    }
                    val bytes = stream.readBytes()
                    event(ProfileUpdateEvent.OnProfileImageSelected(bytes))
                    stream.close()
                }
            }

        })

    DisposableEffect(key1 = true, key2 = lifeCycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            when(event){
                Lifecycle.Event.ON_START -> {
                    event(ProfileUpdateEvent.Refresh)
                }

                Lifecycle.Event.ON_RESUME ->{
                    event(ProfileUpdateEvent.Refresh)

                }

                else -> {
                    //
                }

            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = smallPadding, end = mediumPadding, bottom = mediumPadding)
    ) {
        CustomAppBar(
            //title = stringResource(id = R.string.editProfile),
            middleAction = {
                DefaultAppBarTitle(title = stringResource(id = R.string.editProfile))
            },
            leftAction = {
               Text(
                   text = "Cancel",
                   style = MaterialTheme.typography.labelMedium,
                   fontWeight = FontWeight.SemiBold,
                   color = MaterialTheme.colorScheme.onBackground
               )
            },
            rightAction = {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
        )

        LazyColumn(
            modifier = Modifier
        ){

            item {

                Row(
                    modifier = Modifier.padding(bottom = smallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePictureSection(
                        user = user,
                        profileByteArray =state.profilePicture,
                        imageLoader = imageLoader,
                        enabled = !state.isLoading,
                        profilePictureRemove = userRemovedPicture
                        ){
                        profilePicker.launch("image/*")
                    }
                    if (state.profilePicture != null || state.user?.profilePicture != null ){
                        ElevatedRoundedButton(
                            onButtonClick = {
                                event(ProfileUpdateEvent.OnProfileImageSelected(null))
                            },
                            buttonText = "Remove Picture" )

                    }

                }
                CustomInputField(
                    modifier = Modifier
                        .weight(0.4F)
                        .padding(end = mediumPadding)
                    ,
                    value = state.username?:"",
                    fieldTitle = "Username",
                    placeHolderText = state.user?.username?:"Username",
                    onValueChange = {
                        event(ProfileUpdateEvent.UsernameChange(it))
                    },
                    enabled = !state.isLoading,
                    checkValueField = {
//                            event(SignUpEvent.CheckValueField(1))1
                    },
                    focusDirection = FocusDirection.Right,

                    )
                CustomInputField(
                    modifier = Modifier
                        .weight(0.4F)
                    ,
                    value = state.displayName ?: "",
                    fieldTitle = "Display Name",

                    placeHolderText =state.user?.displayName?:"Display Name",
                    onValueChange = {
                        event(ProfileUpdateEvent.DisplayNameChange(it))
                    },
                    enabled = !state.isLoading,
                )
                CustomInputField(
                    value = state.emailAddress ?: "",
                    fieldTitle = "Email Address",

                    placeHolderText = state.user?.emailAddress?:"Email Address",
                    onValueChange = {
                        event(ProfileUpdateEvent.EmailChange(it))
                    },
                    enabled = !state.isLoading,
                    checkValueField = {
                        event(ProfileUpdateEvent.CheckEmailAddressField)
                    },
                    errorsText = state.emailAddressError
                )

                CustomInputField(
                    value = state.password ?: "",
                    placeHolderText = "Password",
                    fieldTitle = "Password",

                    onValueChange = {
                        event(ProfileUpdateEvent.PasswordChange(it))
                    },
                    enabled = !state.isLoading,
                    errorsText = state.passwordError
                )

            }
        }
    }
}

@Composable
private fun ProfilePictureSection(
    user: User?,
    profileByteArray:ByteArray?,
    imageLoader: ImageLoader,
    enabled:Boolean = true,
    profilePictureRemove:Boolean = false,
    pickImage: () ->Unit
){
    val shouldDisplayEmptyImage = profilePictureRemove || ( profileByteArray == null && user?.profilePicture == null)
    val shouldUseSelectedImage = profileByteArray != null
    val  borderColor = if (shouldDisplayEmptyImage)MaterialTheme.colorScheme.primary else Color.Transparent


    Box(
        modifier = Modifier
            .padding(horizontal = smallPadding)
            .clickable(enabled = enabled) {
                pickImage()
            }
            .border(
                shape = RoundedCornerShape(largerCornerRadius),
                width = 3.dp,
                color = borderColor
            )
            .size(100.dp)
        ,
        contentAlignment = Alignment.Center
    ){
        if (shouldDisplayEmptyImage){
            Icon(
                modifier = Modifier
                    .padding(smallPadding)
                    .scale(2F)
                    .rotate(120F)
                ,
                imageVector = ExMusicIcons.selectIcon ,
                contentDescription = "Pick Image",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        else if (shouldUseSelectedImage) {
            val imageBitmap = BitmapFactory.decodeByteArray(profileByteArray, 0, profileByteArray!!.size).asImageBitmap()
            //  val image = painterResource(id = R.drawable.background)
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(largerCornerRadius))
                ,
                bitmap = imageBitmap,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop
            )

        }
        else {
            ArtistImage(
                imageLoader = imageLoader,
                user = user,
                modifier = Modifier.size(100.dp),
                )
        }
    }
}

@Composable
private fun StrongPassword(
    passwordStrength:Int? = null
){
    if (passwordStrength != null){
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Strong")
            for (i in 0 until  passwordStrength){
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.onPrimary)
                )
            }
        }

        
    }
}

@Composable
private fun VerifyField(
   isAccepted:Boolean = false,
   text:String
){
    val background = if (isAccepted)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(8.dp)
                .background(background)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground
            )

    }

}

@Preview
@Composable
private  fun Preview(){
    val context = LocalContext.current
    val imageLoader = ImageLoader(context = context)
    val user1 = User(username = "Mikail", profilePicture = "tes", displayName = "Mikail Ramadan", emailAddress = "Test",password = "Test", registeredDate = "")
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    ExMusicTheme(darkTheme = false) {
        val state = ProfileUpdateState(isLoading = false, user = user1)
        ProfileUpdateScreen(
            state = state,
            navigateToLogin = {

            },
            navigateToOnboard = {

            },
            event = {

            },
            imageLoader = imageLoader,
            snackBarHostState = snackbarHostState

        )
    }
}