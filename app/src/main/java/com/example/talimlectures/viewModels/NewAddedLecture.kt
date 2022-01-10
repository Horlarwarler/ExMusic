package com.example.talimlectures.viewModels

import androidx.lifecycle.ViewModel
import com.example.talimlectures.data.model.NewlyAdded
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NewAddedLecture  @Inject constructor(val lectureRepository: lectureRepository): ViewModel() {
    private val _allNewAddedLectures = MutableStateFlow<List<NewlyAdded>>(emptyList())
    val allNewlyAddedLecture: StateFlow<List<NewlyAdded>> = _allNewAddedLectures
}