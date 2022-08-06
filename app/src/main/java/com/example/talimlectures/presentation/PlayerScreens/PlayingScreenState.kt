package com.example.talimlectures.presentation.PlayerScreens

import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.model.DatabaseLectureModel


data class PlayingScreenState(
    val currentLecture: DatabaseLectureModel? = null,
    val displayDialog:Boolean = false,
    val favourites:List<Favourite> = emptyList(),
    val previousScreenName:String? = null
)
