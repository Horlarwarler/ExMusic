package com.example.talimlectures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.model.Lecture
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LectureViewModel @Inject constructor(val lectureRepository: lectureRepository): ViewModel() {
    private  val _allLectures = MutableStateFlow<List<Lecture>>(emptyList())
    val allLectures: StateFlow<List<Lecture>> = _allLectures
    private  val _searchLectures = MutableStateFlow<List<Lecture>>(emptyList())
    val searchLectures:StateFlow<List<Lecture>> = _searchLectures
    private val _selectedLecture:MutableStateFlow<Lecture?> = MutableStateFlow(null)
    val selectedLecture:StateFlow<Lecture?> = _selectedLecture
    private  val _isFavourite = MutableStateFlow<Boolean>(false)
    val isFavourite:StateFlow<Boolean> = _isFavourite

    fun getAllLectures(){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.allLecture.collect {lectures->
                _allLectures.value = lectures
                lectures.forEach{
                    lecture ->
                    if(lecture.favourite == 1){
                        _isFavourite.value = true
                    }
                }
            }
        }
    }
    fun  getSelectedLecture(lectureId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getSelectedLecture(lectureId = lectureId).collect{
                _selectedLecture.value = it
            }
        }
    }
    fun searchLectures(searchQuery:String){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.searchLecture(searchQuery).collect {
                _searchLectures.value = it
            }
        }
    }
}
