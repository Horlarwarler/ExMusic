package com.crezent.user_presentation.singup

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.CircularProgress
import com.crezent.design_system.components.CustomAppBar
import com.crezent.design_system.components.CustomButton
import com.crezent.design_system.components.CustomInputField
import com.crezent.design_system.components.DefaultAppBarTitle
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largerCornerRadius
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.user_presentation.R


@SuppressLint("SuspiciousIndentation")
@Composable
fun SignUpScreen(
    showBackButton:Boolean = false,
    state: SignUpState,
    event: (SignUpEvent) -> Unit,
    navigateToLogin:() -> Unit,
    onBackClick : () -> Unit = {},

    ){
    val spacerHeight = 15.dp

    val selectedUri = state.profilePicture
    val byteArray = state.profileByteArray
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.signUpSuccessful){
        if (state.signUpSuccessful){
            navigateToLogin()
        }
    }
    val activity = context as ComponentActivity
//    val mediaPicker = remember(activity) {
//        MediaPicker(activity)
//    }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult =  {
            uri ->
            if (uri == null) {
                return@rememberLauncherForActivityResult
            }
            context.contentResolver.openInputStream(uri)
                .use {
                    val  bytes = it?.readBytes()
                    event.invoke(SignUpEvent.OnProfileImageSelected(uri, bytes))

                }
        })

    LaunchedEffect(key1 = state.errors){
        if (state.errors.isNotEmpty()){
            Toast.makeText(context, state.errors[0],Toast.LENGTH_SHORT).show()
            event(SignUpEvent.RemoveShownMessage)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = smallPadding, end = mediumPadding, bottom = mediumPadding)
    ) {
        CustomAppBar(
           middleAction = {
              DefaultAppBarTitle(title =stringResource(id = R.string.signUp))
           },
            leftAction = {
                if (showBackButton){
                    Icon(
                        modifier = Modifier.clickable {
                            onBackClick()
                        },
                        imageVector = ExMusicIcons.backButton,
                        contentDescription = "Navigate Back",
                    )
                }

            }
        )
        Box(modifier =Modifier.weight(0.85f) ){
            LazyColumn(
                modifier = Modifier
            ){
                item() {
                    ProfilePictureSection(
                        selectedByteArray = byteArray,
                        pickImage = {
                            imagePicker.launch("image/*")
                        },
                        enabled = !state.isLoading
                    ) {
                        event.invoke(SignUpEvent.OnProfileImageSelected(null, null))
                    }
                }
                item {
                    CustomInputField(
                        value = state.name ,
                        placeHolderText = "Name",
                        onValueChange = {
                            event(SignUpEvent.NameChange(it))
                        },
                        enabled = !state.isLoading,
                        checkValueField = {
                            event(SignUpEvent.CheckValueField(0))
                        },
                        errorsText = state.nameError,
                        focusDirection = FocusDirection.Next
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomInputField(
                            modifier = Modifier
                                .weight(0.4F)
                                .padding(end = mediumPadding)
                            ,
                            value = state.username,
                            placeHolderText = "Username",
                            onValueChange = {
                                event(SignUpEvent.UsernameChange(it))
                            },
                            enabled = !state.isLoading,
                            checkValueField = {
                                event(SignUpEvent.CheckValueField(1))
                            },
                            focusDirection = FocusDirection.Right,
                            errorsText = state.usernameError

                        )
                        CustomInputField(
                            modifier = Modifier
                                .weight(0.4F)
                            ,
                            value = state.displayName ?: "",
                            placeHolderText = "Display Name",
                            onValueChange = {
                                event(SignUpEvent.DisplayNameChange(it))
                            },
                            enabled = !state.isLoading,
                            checkValueField = {
                                event(SignUpEvent.CheckValueField(2))
                            },
                            errorsText = state.displayNameError
                        )
                    }

                    CustomInputField(
                        value = state.emailAddress ?: "",
                        placeHolderText = "Email Address",
                        onValueChange = {
                            event(SignUpEvent.EmailChange(it))
                        },
                        enabled = !state.isLoading,
                        checkValueField = {
                            event(SignUpEvent.CheckValueField(3))
                        },
                        errorsText = state.emailAddressError
                    )

                    CustomInputField(
                        value = state.password ?: "",
                        placeHolderText = "Password",
                        onValueChange = {
                            event(SignUpEvent.PasswordChange(it))
                        },
                        enabled = !state.isLoading,
                        errorsText = state.passwordError
                    )
                    CustomInputField(
                        value = state.confirmPassword ?: "",
                        placeHolderText = "Confirm Password",
                        onValueChange = {
                            event(SignUpEvent.ConfirmPasswordChange(it))
                        },
                        enabled = !state.isLoading,
                        errorsText = state.confirmPasswordError,
                        imeAction = ImeAction.Done
                    )
                }
            }
            if (state.isLoading){
                CircularProgress()
            }
        }
        CustomButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Continue",
            enabled = state.continueButtonEnabled && !state.isLoading
        ) {
            event(SignUpEvent.SignUp)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Not the first time here?",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color  = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.clickable {
                      navigateToLogin()
                },
                text ="Login",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color  = MaterialTheme.colorScheme.primary
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
private fun ProfilePictureSection(
    selectedByteArray: ByteArray?,
    pickImage: () -> Unit,
    enabled:Boolean = true,
    removeImage: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val  borderColor = if (selectedByteArray == null)MaterialTheme.colorScheme.primary else Color.Transparent
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

                    .size(70.dp),
                contentAlignment = Alignment.Center
            ){
                if (selectedByteArray == null){
                    Icon(
                        modifier = Modifier
                            .padding(smallPadding)
                            .scale(1.5F)
                            .rotate(120F)
                        ,
                        imageVector = ExMusicIcons.selectIcon ,
                        contentDescription = "Pick Image",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                else {
                    val imageBitmap = BitmapFactory.decodeByteArray(selectedByteArray, 0, selectedByteArray.size).asImageBitmap()
                    //  val image = painterResource(id = R.drawable.background)
                    Column(
                    ) {

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

                }
            }
            Text(
                text = stringResource(id = R.string.profilePictureText),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (selectedByteArray != null){
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = smallPadding)
                    .clickable(enabled = enabled) {
                        removeImage()
                        //
                    },
                text = "Remove Image",
                textAlign = TextAlign.Center,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall
            )
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
    ExMusicTheme(darkTheme = false) {
        val state = SignUpState(isLoading = false)
        SignUpScreen(
            state = state,
            navigateToLogin = {

            },
            event = {

            }
        )
    }
}