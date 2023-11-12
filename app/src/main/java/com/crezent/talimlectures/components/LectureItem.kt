package com.crezent.talimlectures.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LectureItem(

){
    Row(
        modifier = Modifier
            .height(com.crezent.design_system.theme.lectureItemHeight)
            .fillMaxWidth()
            .padding(com.crezent.design_system.theme.mediumPadding)
            .background(MaterialTheme.colors.secondary)
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = com.crezent.design_system.theme.Shapes.large
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           // Canvas(modifier = Modifier.size(circleShapeSize))
        }
    }
}

@Preview
@Composable
private fun PreviewLectureItem(){
    LectureItem()
}