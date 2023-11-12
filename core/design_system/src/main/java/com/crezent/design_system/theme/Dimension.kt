package com.crezent.design_system.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val lectureItemHeight = 88.dp

val mediumPadding = 32.dp
val largePadding = 48.dp
val SearchBoxRadius = 19.dp
val InputBoxRadius = 10.dp
val largeSpacer = 30.dp
val mediumSpacer = 20.dp
val smallSpacer = 10.dp
val smallPadding = 16.dp
val circleShapeSize = 56.dp
val largerCornerRadius = 100.dp
val appBarHeight = 60.dp

data class Dimensions(

    val lectureItemHeight: Dp = 88.dp,
    val mediumPadding: Dp = 20.dp,
    val largePadding: Dp = 40.dp,
    val searchBoxRadius: Dp = 19.dp,
    val inputBoxRadius: Dp = 10.dp,
    val largeSpacer: Dp = 30.dp,
    val mediumSpacer: Dp = 20.dp,
    val smallSpacer: Dp = 10.dp,
    val circleShapeSize: Dp = 56.dp,
)

val localSpacing = compositionLocalOf { Dimensions() }

