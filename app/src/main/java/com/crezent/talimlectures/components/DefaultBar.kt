package com.crezent.talimlectures.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.crezent.talimlectures.R

@Composable
fun Bar(
    modifier: Modifier = Modifier,
    topText: String = "Lectures",
    onNavigationButtonClicked: () -> Unit ={},
    ontopRightButtonClicked: () -> Unit ={},
    @DrawableRes navigationButton: Int? = null,
    @DrawableRes topRightButton: Int? = R.drawable.search

) {

}
