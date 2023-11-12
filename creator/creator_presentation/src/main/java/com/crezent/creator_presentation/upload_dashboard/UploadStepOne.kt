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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.common.util.MediaAttribute
import com.crezent.creator_presentation.R
import com.crezent.creator_presentation.components.FileSection
import com.crezent.creator_presentation.components.MediaProperties
import com.crezent.creator_presentation.components.UploadStepHeader
import com.crezent.design_system.components.CustomButton
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largePadding
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.design_system.theme.smallSpacer

@Composable
fun UploadStepOne(
    isEnabled:Boolean = false,
    mediaAttribute: MediaAttribute? = null,
    choseFile : () -> Unit,
    onPreview : () -> Unit,
    onNext : () -> Unit

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
        UploadStepHeader()
        Spacer(modifier = Modifier.height(smallSpacer))
        Text(
            text = stringResource(id = R.string.step_one),
            style = MaterialTheme.typography.bodyMedium,
            color =  MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 30.sp,
            softWrap = true
        )
        Spacer(modifier = Modifier.height(mediumPadding))

        FileSection(
            onButtonClick = onPreview,
            choseFile = choseFile,
            enable = true,
            filePreview = {
                if (mediaAttribute != null){
                    MediaProperties(mediaAttribute = mediaAttribute)
                }
            },
            buttonText = if (mediaAttribute== null) null else "Preview"
        )
        val nextButtonPadding = if (mediaAttribute == null) largePadding else smallPadding
        Spacer(modifier = Modifier.height(nextButtonPadding))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CustomButton(
                enabled = isEnabled,
                text = "Next",
                paddingValues = PaddingValues(
                    horizontal = 100.dp,
                    vertical = 15.dp
                ),
                onClick = onNext
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
       UploadStepOne(
           choseFile =  {},
           isEnabled = false,
           onPreview = {

           }

       ){

       }
    }
}