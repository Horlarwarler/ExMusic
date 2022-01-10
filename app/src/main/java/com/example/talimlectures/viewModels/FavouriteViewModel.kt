package com.example.talimlectures.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.repository.lectureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteViewModel  @Inject constructor(val lectureRepository: lectureRepository): ViewModel() {
    private  val _favouriteLectures = MutableStateFlow<List<Favourite>>(emptyList())
    val favouriteLectures: StateFlow<List<Favourite>> = _favouriteLectures
    fun getfavouriteLectures(){
        viewModelScope.launch(Dispatchers.IO) {
           lectureRepository.allFavourite.collect {
               _favouriteLectures.value  = it
           }
        }
    }
}