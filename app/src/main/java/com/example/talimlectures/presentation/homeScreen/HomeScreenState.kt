package com.example.talimlectures.presentation.homeScreen

import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.util.SearchAction

data class HomeScreenState(
    val searchText:String = "Search Music",
    val newlyAdded: List<DatabaseLectureModel> = emptyList(),
    val recentlyPlayed: List<DatabaseLectureModel> = emptyList(),
    val favourite: List<DatabaseLectureModel> = emptyList(),
    val selectedMusic: String? = null,
    val selectedFavourite:String? = null,
    val selectedDownload:String?= null,
    val playingMusic:String? = null,
    val searchResult: List<DatabaseLectureModel> = emptyList(),
    val displayDialog:Boolean = false,
    val searchState:SearchAction = SearchAction.IDLE,
    val lectures:List<DatabaseLectureModel> = emptyList(),
    val isLoading:Boolean = false,
    val error:String ="",
    val isConnected:Boolean = false
)
