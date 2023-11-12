package com.crezent.user_presentation.signIn

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.BorderlessTextInput
import com.crezent.design_system.components.CircularProgress
import com.crezent.design_system.components.CustomAppBar
import com.crezent.design_system.components.CustomButton
import com.crezent.design_system.components.DefaultAppBarTitle
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.user_presentation.R
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    state: SignInState = SignInState(),
    showBackButton :Boolean = false,
    event: (SignInEvent) -> Unit,
    navigateToHome:() -> Unit,
    onBackClick : () -> Unit,
    navigateToSignUp: () -> Unit,
    snackbarHostState: SnackbarHostState


){
    val spacerHeight = 15.dp
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val head = state.error
    LaunchedEffect(key1 = state.signInSuccessful){
        if (state.signInSuccessful){
            navigateToHome()
        }
    }
    LaunchedEffect(key1 = state.error){

        if (head == null){
            Log.d("Temp","Error empty")
            return@LaunchedEffect
        }
        scope.launch {
            snackbarHostState.showSnackbar(
                "ERROR ${head.value}"
            )
            event(SignInEvent.RemoveShownMessage)
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = smallPadding, end = mediumPadding, bottom = mediumPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomAppBar(
            middleAction = {
                DefaultAppBarTitle(title =stringResource(id = R.string.signIn))
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
            ,
        ) {

            BorderlessTextInput(
                value =state.emailAddress , 
                onValueChange = {
                   event(SignInEvent.EmailAddressChange(it))             
                } , 
                label = "Email Address",
                placeHolderText = "abcdefgh@gmail.com",
                keyboardType = KeyboardType.Email,
                enabled =  !state.isLoading
                
                )
            Spacer(modifier = Modifier.height(mediumPadding))
            BorderlessTextInput(
                value =state.password ,
                onValueChange = {
                    event(SignInEvent.PasswordChange(it))
                } ,
                label = "Password",
                placeHolderText = "Password",
                keyboardType = KeyboardType.Password,
                enabled =  !state.isLoading

            )
            CustomButton(
                modifier = Modifier
                    .padding(top = 80.dp),
                text = "Login",
                enabled = state.loginButtonEnabled && !state.isLoading,
                paddingValues = PaddingValues(horizontal = 80.dp, vertical = 15.dp)

            ) {
                event(SignInEvent.SignIn)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "Don't have An Account?",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color  = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.clickable {
                          navigateToSignUp()
                    },
                    text ="Sign Up",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color  = MaterialTheme.colorScheme.primary
                )
            }
            if (state.isLoading){
                CircularProgress()
            }

        }

       

    }

}

@Preview
@Composable
private fun Preview(){
//    ExMusicTheme {
//        SignInScreen(
//            event = {},
//            navigateToHome = { /*TODO*/ },
//            onBackClick = {
//
//            },
//            ) {
//
//        }
//    }


}