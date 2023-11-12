package com.crezent.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.design_system.theme.smallPadding

@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    value: String,
    placeHolderText:String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isPasswordField:Boolean = false,
    enabled:Boolean = true,
    errorsText:List<String> = emptyList(),
    imeAction: ImeAction = ImeAction.Next,
    focusDirection: FocusDirection = FocusDirection.Down,
    fieldTitle:String? = null,
    checkValueField: () -> Unit = {},

){

    val focusManager = LocalFocusManager.current


    var showPassword by remember {
        mutableStateOf(false)
    }

    val hideInnerTextField by remember(value) {
        mutableStateOf( value.isEmpty() || value.isBlank())
    }


    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(bottom = smallPadding)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text =fieldTitle?:placeHolderText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .height(50.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(7.dp)),
            value = value,
            decorationBox = { innerTextField ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 45.dp),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(0.dp, Color.Transparent),
                    elevation = CardDefaults.elevatedCardElevation(
                        pressedElevation = 5.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 5.dp),
                        contentAlignment = Alignment.CenterStart
                    ){

                        if (value.isEmpty()){
                            Text(
                                text = placeHolderText,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5F)
                            )
                        }
                        innerTextField()

                    }

                }

            },
            onValueChange = {
                onValueChange(it)
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    checkValueField()
                },
                onNext = {
                    focusManager.moveFocus(
                        focusDirection = focusDirection
                    )
                    checkValueField()
                }
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = singleLine,
            enabled = enabled,
            cursorBrush = Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.onSurface))
            )
        errorsText.forEach {
            errorText ->
            Text(
                modifier = Modifier.padding(bottom = 3.dp),

                text =errorText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }


    }

}


@Preview
@Composable
private fun Preview(){
    CustomInputField(
        value = "", placeHolderText = "Hello",
        onValueChange = {},
        errorsText = listOf("Hello","Hi")
        )
}
