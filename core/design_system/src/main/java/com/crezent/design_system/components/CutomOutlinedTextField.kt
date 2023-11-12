package com.crezent.design_system.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun CustomOutlinedTextField(
    value:String,
    onValueChange: (String) -> Unit,
    label:String,
    enabled:Boolean =true,
    placeholder:String,

    errorMessage:String? = null
){
    val isError = errorMessage !=  null
    Column(
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            shape = RoundedCornerShape(4.dp),
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isError)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium
                .copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(0.5F),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            placeholder =  {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary.copy(0.5F)
                )
            }

        )
        if (errorMessage != null){
            Text(
                modifier = Modifier.padding(
                    start = 15.dp
                ),
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

    }


}

@Preview(showBackground = true)
@Composable
private  fun Preview(){

    ExMusicTheme(darkTheme = false) {
        CustomOutlinedTextField(
            value =  "Title",
            onValueChange ={} ,
            label = "Title",
            placeholder =  "This is title Field",
            errorMessage = "Text is null"
        )
    }
}