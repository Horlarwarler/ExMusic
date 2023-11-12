package com.crezent.creator_presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.smallSpacer

@Composable
fun UploadStepHeader(
    index: Int = 1,
    stepTitle:String = "Music File"
){
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(smallSpacer))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            UploadStepProgress(
                stepIndex = index
            )
        }
        Spacer(modifier = Modifier.height(smallSpacer))
        Text(
            text = "Step $index",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            ),
        )

        Spacer(modifier = Modifier.height(smallSpacer))
        Text(
                text = stepTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                ),
            )

        }
    }


@Preview(
    showBackground = true
)
@Composable
private  fun preview(){
    ExMusicTheme(
        darkTheme = false
    ) {
        UploadStepHeader()
    }
}