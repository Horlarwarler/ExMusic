package com.crezent.creator_presentation.upload_dashboard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.crezent.common.util.FileHelper
import com.crezent.common.util.LinkedList
import com.crezent.common.util.MimeType
import com.crezent.music.PlaybackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UploadDashboardViewModel @Inject constructor(
   private val playbackRepo : PlaybackRepository,
   // private val uploadRepository: UploadRepository,
    private val fileHelper: FileHelper

) : ViewModel(
) {
    private var _state = MutableStateFlow(UploadDashboardState())

    private  val errorLinkedList = LinkedList<String>()

    val state = _state.asStateFlow()

    private var selectedThumbnailUri:Uri? = null


//    val state = combine(
//
//        _state
//    ){
//        state ->
//        state.copy()
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_state.value)
//
    init {


    _state.update {
        it.copy(error = errorLinkedList.getHead())
    }

}

    fun handleEvent(event: UploadDashboardEvent){
        when(event){
            is UploadDashboardEvent.DescriptionChange -> descriptionChange(event.description)
            UploadDashboardEvent.PreviewSelectedAudio -> previewAudio()
            UploadDashboardEvent.RemoveShownMessage -> removeShownMessage()
            UploadDashboardEvent.RemoveThumbnailFile -> removeThumbnail()
            is UploadDashboardEvent.SelectAudioFile -> selectAudio(event.selectedUri)
            is UploadDashboardEvent.SelectThumbnailFile -> selectThumbnailFile(event.selectedUri)
            is UploadDashboardEvent.TitleChange -> titleChange(event.title)
        }
    }

    private fun descriptionChange(description: String){
        _state.update {
            it.copy(
                description = description
            )
        }
    }

    private fun titleChange (title: String){
        _state.update {
            it.copy(
                title = title
            )
        }
    }

    private fun previewAudio(){
        val selectedUri = state.value.selectedAudioUri?:return
        Log.d("Preview","Selected uri $selectedUri")
      playbackRepo.previewSong(selectedUri)
    }

    private fun removeThumbnail(){
        _state.update {
            selectedThumbnailUri = null
            it.copy(
                imageAttribute = null,
                imageByteArray = null
            )
        }
    }


    private fun selectAudio(selectedUri: Uri) {

        try {
            // fileHelper.
            val mediaAttribute = selectedUri.let { fileHelper.getFileAttribute(it) }



            val fileIsAudio = fileHelper.checkType(selectedUri) == MimeType.AUDIO
            Log.d("AUDIO", "IS AUDIO IS $fileIsAudio")

            if (!fileIsAudio){
               errorLinkedList.append("Select Audio File Only")
                return
            }
            _state.update {
                it.copy(
                    selectedAudioUri = selectedUri,
                    audioAttribute = mediaAttribute
                )
            }
        }
        catch (error:Exception){
            val errorMessage = error.message?:"Unknown Error"
            errorLinkedList.append(errorMessage)
        }
        finally {

            //updateSongFile()
        }


    }

    private fun selectThumbnailFile(selectedUri: Uri) {
        Log.d("Thumbnail", "Should Thumbnail is }")


        return try {
            val fileIsImage = fileHelper.checkType(selectedUri) == MimeType.IMAGE
            if (!fileIsImage){
               // throw CustomError("Select Only Image")
                errorLinkedList.append("Select Image File Only")
                return

            }
            val thumbnailFileAttribute = fileHelper.getFileAttribute(selectedUri)
            val thumbnailByteArray = fileHelper.byteFromUri(selectedUri)
            Log.d("Thumbnail", "Thumbnail is null ${thumbnailByteArray == null}")
            selectedThumbnailUri = selectedUri
            _state.update {
                it.copy(
                   imageAttribute = thumbnailFileAttribute,
                    imageByteArray = thumbnailByteArray
                )
            }
        }
        catch (error:Exception){
            val errorMessage = error.message?:""
          //  Log.d("Thumbnail", errorMessage)
            errorLinkedList.append(errorMessage)
            _state.update {
                it.copy(
                    error = errorLinkedList.getHead()
                )
            }
        }

    }

    private fun removeShownMessage() {
        errorLinkedList.removeFirst()
        _state.update {
           it.copy(
               error = errorLinkedList.getHead()
           )
        }
    }



    private fun upload(){
//        val title = state.value.title!!
//        val description = state.value.description!!
//        val songUri = state.value.songUri?:return
//        val duration = state.value.duration?:return
//        val thumbnailUri = state.value.thumbnailUri
//        uploadRepository.uploadSong(
//            title = title,
//            description = description,
//            duration = duration,
//            songUri = songUri,
//            thumbnailUri = thumbnailUri
//        )
    }

//    private fun updateSongFile(){
//
//        val uri = _state.value.songUri?:return
//        try {
//
//            val fileIsAudio = fileHelper.checkType(uri) == MimeType.AUDIO
//            if (!fileIsAudio){
//                throw CustomError("Select Only Audio")
//            }
//            enableUploadButton()
//
//
//        }
//        catch (error:Exception){
//
//        }
//
//    }

//    private fun enableUploadButton(){
//        val titleEmpty = state.value.title.isNullOrBlank()
//        val descriptionEmpty = state.value.description.isNullOrBlank()
//        val durationIsNull = state.value.duration == null
//        val songUri = state.value.songUri == null
//        val fileIsNull = durationIsNull || songUri
//        val missingInput = titleEmpty || descriptionEmpty || fileIsNull
//        _state.update {
//            it.copy(uploadButtonEnabled = !missingInput)
//        }
//    }






}