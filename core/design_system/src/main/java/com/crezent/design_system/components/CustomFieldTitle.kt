package com.crezent.design_system.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CustomFieldTitle(
    title: String,
    color: Color = MaterialTheme.colors.background
){
    Text(
        text = title,
        style = MaterialTheme.typography.h3,
        color = color,
    )
    Spacer(
        modifier =  Modifier.height(3.dp)
    )
}