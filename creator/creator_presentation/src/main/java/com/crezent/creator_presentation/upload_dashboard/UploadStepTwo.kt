package com.crezent.creator_presentation.upload_dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.creator_presentation.components.UploadStepHeader
import com.crezent.design_system.components.CustomButton
import com.crezent.design_system.components.CustomOutlinedTextField
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largePadding
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding

@Composable
fun UploadStepTwo(
    titleValue:String,
    descriptionValue:String,
    onTitleChange : (String) -> Unit,
    onDescriptionChange :(String) -> Unit,
    nextButtonEnabled:Boolean = false,
    onNextButtonClick: () -> Unit,
    titleErrorMessage:String? = null,
    descriptionErrorMessage:String? = null
){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = smallPadding,
                end = smallPadding
            )
            .verticalScroll(scrollState)
    ) {
        UploadStepHeader(
            index = 2,
            stepTitle = "Song Information"
        )
        Spacer(modifier = Modifier.height(largePadding))
        CustomOutlinedTextField(
            value = titleValue,
            onValueChange =onTitleChange ,
            label = "Title",
            placeholder = "The song title",
            errorMessage = titleErrorMessage,

        )
        Spacer(modifier = Modifier.height(smallPadding))

        CustomOutlinedTextField(
            value = descriptionValue,
            onValueChange =onDescriptionChange ,
            label = "Description",
            placeholder = "The Song Description",
            errorMessage = descriptionErrorMessage
        )
        Spacer(modifier = Modifier.height(mediumPadding))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CustomButton(
                text = "Next",
                paddingValues = PaddingValues(
                    horizontal = 100.dp,
                    vertical = 15.dp,
                ),
                onClick = onNextButtonClick,
                enabled = nextButtonEnabled,

            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private  fun preview(){
    ExMusicTheme(darkTheme = false) {
       UploadStepTwo(
           descriptionValue = "",
           titleValue = "",

           onDescriptionChange =  {

           },
           onNextButtonClick =  {

           },
           nextButtonEnabled = true,
           onTitleChange = {

           }
       )
    }
}