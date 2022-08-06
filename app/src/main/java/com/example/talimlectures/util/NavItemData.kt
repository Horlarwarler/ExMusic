package com.example.talimlectures.util

import androidx.annotation.DrawableRes

data class NavItemData(
    val route: String,
    @DrawableRes val icon: Int,
    val text: String,
)
