package com.crezent.talimlectures.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
@Immutable
data class ScaffoldTopBarState(
    val modifier: Modifier = Modifier,
    val leftAction : @Composable () -> Unit = {},
    val middleAction: @Composable () -> Unit = {},
    val rightAction: @Composable () -> Unit = {}
)
