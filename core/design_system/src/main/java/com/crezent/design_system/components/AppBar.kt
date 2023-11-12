package com.crezent.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun AppBar (
    modifier: Modifier = Modifier,
    topText:String,
    onActionClick: () -> Unit = {},
    showBackButton:Boolean = false,
    style: TextStyle =  MaterialTheme.typography.h3
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        if (showBackButton){
            IconButton(
                modifier = Modifier

                    .align(Alignment.CenterStart)
                ,
                onClick =onActionClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIos,
                    contentDescription ="Back Button",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }

        Text(
            modifier = Modifier.align(
                Alignment.Center
            ),
            text = topText,
            style = style,
            color = MaterialTheme.colors.onBackground
        )
    }

}
