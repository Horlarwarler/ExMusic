package com.crezent.design_system.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largerCornerRadius

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text:String,
    enabled:Boolean = true,
    colors :ButtonColors = ButtonDefaults.textButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(0.5F),
        ),
    color: Color = MaterialTheme.colorScheme.onPrimary,
    paddingValues: PaddingValues = PaddingValues(horizontal = 40.dp, vertical = 10.dp),
    onClick: ()-> Unit,

){
    TextButton(
        modifier = modifier
            .padding(vertical = 10.dp)
        ,
        onClick = onClick,
        colors =colors,
        shape = RoundedCornerShape(largerCornerRadius),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 5.dp
        ),
        contentPadding = paddingValues


    ) {
        Text(
            text =text,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Preview
@Composable
private fun Preview(){

   ExMusicTheme {
       CustomButton(
           text = "Login",) {

       }
   }

}