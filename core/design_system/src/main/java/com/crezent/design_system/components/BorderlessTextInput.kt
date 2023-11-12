package com.crezent.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun BorderlessTextInput(
    value:String,
    onValueChange: (String) -> Unit,
    label:String,
    enabled:Boolean = false,
    placeHolderText:String,
    focusDirection: FocusDirection = FocusDirection.Next,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val passwordIcon = if (showPassword) ExMusicIcons.visibleIcon else  ExMusicIcons.invisibleIcon
    val isPasswordField = keyboardType == KeyboardType.Password
    val visualTransformation  = when{
        !isPasswordField -> {
            VisualTransformation.None
        }
        !showPassword -> {

            VisualTransformation.None
        }
        else -> {
            PasswordVisualTransformation()
        }


    }
    val color = if (isSystemInDarkTheme())  Color(0xFFFFFFFF) else  Color(0xFF1A1C1E)
    BasicTextField(
        modifier = Modifier.background(Color.Transparent),
        value = value,
        onValueChange =  onValueChange,
        enabled = enabled,
        decorationBox =  {
            innerTextField ->
            Column(
                modifier = Modifier.drawWithContent {
                    drawContent()
                    drawLine(
                        color = color,
                        start = Offset(x = 0F, y = size.height -1.dp.toPx()),
                        end = Offset(x =size.width, y = size.height -1.dp.toPx()),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = 3.dp,
                        horizontal = 5.dp
                    ),
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 20.5.sp
                    )
                Row(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ){
                        if (value.isEmpty()){
                            Text(
                                text = placeHolderText,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.8F)
                            )
                        }
                        innerTextField()
                    }
                    if (isPasswordField){
                        Icon(
                            modifier = Modifier.clickable {
                                  showPassword =  !showPassword
                            },
                            imageVector = passwordIcon ,
                            contentDescription = "Hide /Show Password",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }



                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        visualTransformation =visualTransformation ,
        cursorBrush = Brush.verticalGradient(colors =
        listOf(
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.onSurface)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(focusDirection)
            }
        )

    )
}

@Preview
@Composable
private fun PreviewBorderless(){
   ExMusicTheme {
       BorderlessTextInput(
           value = "",
           onValueChange ={} ,
           label = "Email" ,
           placeHolderText = "dcrescn@gmail.com",
       )
   }
}