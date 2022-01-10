package com.example.talimlectures.viewModels

import androidx.lifecycle.ViewModel
import com.example.talimlectures.data.model.Category
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CategoryModel  @Inject constructor(val lectureRepository: lectureRepository): ViewModel() {

    private val _allCategory = MutableStateFlow<List<Category>>(emptyList())
    val allCategory: StateFlow<List<Category>> = _allCategory
}