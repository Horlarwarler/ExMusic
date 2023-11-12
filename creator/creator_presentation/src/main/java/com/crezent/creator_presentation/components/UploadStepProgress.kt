package com.crezent.creator_presentation.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun UploadStepProgress(
    stepIndex: Int = 1
){
    val progress = (stepIndex/ 3.0).toFloat()
    LinearProgressIndicator(
        progress = progress,
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.primaryContainer,
        strokeCap = StrokeCap.Round,
    )
}

@Composable
@Preview()
private  fun preview(){
    ExMusicTheme(darkTheme = false) {
        UploadStepProgress()
    }
}