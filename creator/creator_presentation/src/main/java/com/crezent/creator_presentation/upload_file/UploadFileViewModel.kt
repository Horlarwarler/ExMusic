package com.crezent.creator_presentation.upload_file


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.CustomError
import com.crezent.common.util.FileHelper
import com.crezent.common.util.LinkedList
import com.crezent.common.util.MimeType
import com.upload.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UploadFileViewModel @Inject constructor(
    private val uploadRepository: UploadRepository,
    private val fileHelper: FileHelper
) : ViewModel(
) {

    private var _state = MutableStateFlow(UploadFileState())

    init {
      uploadRepository.onStart()

    }

    private  var errorLinkedList = LinkedList<String>()




  //  private var songFile:File? = null

    val state = combine(
        uploadRepository.uploads,
        _state
    ){
        uploadingFile, state ->

        state.copy(uploadingFile =null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_state.value)

    fun handleEvent(event: UploadFileEvent){
        when(event){
            is UploadFileEvent.Upload -> {
                upload()
            }

            is UploadFileEvent.EditSong ->{
                selectAudio(event.songUri)
            }

            is UploadFileEvent.EditThumbnail ->{
                selectThumbnail(event.thumbnailUri)
            }

            is UploadFileEvent.EditTitle ->{
                editTitle(event.title)
            }

            is UploadFileEvent.EditDescription -> {
                editDescription(event.description)
            }
            is UploadFileEvent.RemoveShownMessage ->{
                removeShownMessage()
            }
            is UploadFileEvent.OnStart ->{
                uploadRepository.onStart()
            }
        }
    }

    private fun editTitle(title:String){
        _state.update {
            it.copy(title = title)
        }
        enableUploadButton()
    }

    private fun selectAudio(songUri: Uri?) {
        if (songUri == null){
            _state.update {
                it.copy(songUri = null, )
            }
            return
        }
        try {
           // fileHelper.
            val songAttribute = songUri.let { fileHelper.getFileAttribute(it) }
            val songName = songAttribute.name
            val duration = songAttribute.duration
            val songSize = songAttribute.size


            val fileIsAudio = fileHelper.checkType(songUri) == MimeType.AUDIO
            if (!fileIsAudio){
                throw CustomError("Select Only Audio")
            }
            _state.update {
                it.copy(songUri = songUri, songName = songName, duration = duration, songFileSize = songSize )
            }
        }
        catch (error:Exception){
            val errorMessage = error.message?:"Unknown Error"
            errorLinkedList.append(errorMessage)
            val head = errorLinkedList.getHead()
            _state.update {
                it.copy(error = head )
            }
        }
        finally {

            updateSongFile()
        }


    }

    private fun selectThumbnail(uri:Uri?) {
        if (uri == null){
            _state.update {
                state.value.copy(thumbnailUri = null, thumbnailName = null)
            }
            return
        }
        return try {
            val fileIsImage = fileHelper.checkType(uri) == MimeType.IMAGE
            if (!fileIsImage){
                throw CustomError("Select Only Image")
            }
            val thumbnailFileAttribute = fileHelper.getFileAttribute(uri)
            val thumbnailName = thumbnailFileAttribute.name
            val thumbnailSize = thumbnailFileAttribute.size
            _state.update {
                it.copy(
                    thumbnailUri = uri,
                    thumbnailName = thumbnailName,
                    thumbnailFileSize = thumbnailSize
                )
            }
        }
        catch (error:Exception){
            val errorMessage = error.message?:""
             errorLinkedList.prepend(errorMessage)
            val head = errorLinkedList.getHead()

            _state.update {
                it.copy(error = head )
            }
        }

    }

    private fun editDescription(description:String){
        _state.update {
            it.copy(description = description)
        }
        enableUploadButton()
    }

    private fun removeShownMessage() {
        errorLinkedList.removeFirst()
        val head = errorLinkedList.getHead()
        _state.update {
            it.copy(error = head)
        }
    }



    private fun upload(){
        val title = state.value.title!!
        val description = state.value.description!!
        val songUri = state.value.songUri?:return
        val duration = state.value.duration?:return
        val thumbnailUri = state.value.thumbnailUri
        uploadRepository.uploadSong(
            title = title,
            description = description,
            duration = duration,
            songUri = songUri,
            thumbnailUri = thumbnailUri
        )
    }

    private fun updateSongFile(){

        val uri = _state.value.songUri?:return
         try {

             val fileIsAudio = fileHelper.checkType(uri) == MimeType.AUDIO
             if (!fileIsAudio){
                 throw CustomError("Select Only Audio")
             }
             enableUploadButton()


         }
        catch (error:Exception){

        }

    }

    private fun enableUploadButton(){
        val titleEmpty = state.value.title.isNullOrBlank()
        val descriptionEmpty = state.value.description.isNullOrBlank()
        val durationIsNull = state.value.duration == null
        val songUri = state.value.songUri == null
        val fileIsNull = durationIsNull || songUri
        val missingInput = titleEmpty || descriptionEmpty || fileIsNull
        _state.update {
            it.copy(uploadButtonEnabled = !missingInput)
        }
    }




}