package com.example.talimlectures.presentation.LectureScreens

import com.example.talimlectures.data.model.*
import com.example.talimlectures.util.SearchAction

data class LectureScreenState(
    val searchText:String = "Search Music",
    val selectedMusic: String? = null,
    val selectedDownload:String?= null,
    val playingMusic:String? = null,
    val searchResult: List<DatabaseLectureModel> = emptyList(),
    val displayDialog:Boolean = false,
    val searchState: SearchAction = SearchAction.IDLE,
    val lectures:List<DatabaseLectureModel> = emptyList(),
    val categorySelected:Int? = null,
    val categories:List<DatabaseCategoryModel> = emptyList(),
    val isLoading:Boolean = false,
    val error:String = ""
)
