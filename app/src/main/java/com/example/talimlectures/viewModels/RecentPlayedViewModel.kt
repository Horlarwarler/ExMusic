package com.example.talimlectures.viewModels

import androidx.lifecycle.ViewModel
import com.example.talimlectures.data.model.RecentlyPlayed
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RecentPlayedViewModel  @Inject constructor(val lectureRepository: lectureRepository):ViewModel() {
    private val _allRecentPlayed = MutableStateFlow<List<RecentlyPlayed>>(emptyList())
    val recentlyPlayed: StateFlow<List<RecentlyPlayed>> = _allRecentPlayed
}