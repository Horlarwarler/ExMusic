package com.example.talimlectures.viewModels

import android.app.DownloadManager
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.*
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class SharedViewModel @Inject constructor(
    val categoryModel: CategoryModel,
    val favouriteViewModel: FavouriteViewModel,
    val lectureViewModel: LectureViewModel,
    val newAddedLecture: NewAddedLecture,
    val recentPlayedViewModel: RecentPlayedViewModel,
):ViewModel() {


}