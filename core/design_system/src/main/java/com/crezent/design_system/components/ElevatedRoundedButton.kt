package com.crezent.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun ElevatedRoundedButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    buttonText:String,
    enabled:Boolean = true
){
    ElevatedButton(
        modifier = modifier,
        enabled =enabled ,
        onClick = {
                  onButtonClick()
        },
        border = BorderStroke(
            1.dp,MaterialTheme.colorScheme.primaryContainer
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text(
            modifier = Modifier,
            text = buttonText ,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }



}

@Preview
@Composable
private fun Preview(){
    ExMusicTheme {
        ElevatedRoundedButton(onButtonClick = { /*TODO*/ }, buttonText = "Profile" )
    }
}