package com.crezent.ExMusic.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerAppBar(
    onBackButtonClicked: () -> Unit
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = com.crezent.design_system.theme.mediumPadding, end = com.crezent.design_system.theme.mediumPadding, top = com.crezent.design_system.theme.mediumPadding)
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            text = "Now Playing",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,

           // color = com.crezent.design_system.theme.TitleColor
        )
//        NavigationIcon(
//            modifier = Modifier.align(Alignment.CenterStart),
//            onBackButtonClicked = {onBackButtonClicked()})

    }
}

