package com.example.talimlectures.domain.music

import android.app.Activity
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.talimlectures.data.model.FileModel
import com.example.talimlectures.data.model.DatabaseLectureModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class MusicPlayerImpl: MusicInterface, Activity() {

    //instantiate a media player
    private  var  mMediaPlayer: MediaPlayer? = null
    override var musicState by mutableStateOf(MusicState())
    val _error = MutableStateFlow(String())
    val error = _error.asSharedFlow()
    var job:Job? = null
    //val directory = filesDir.listFiles()


    val fileNames:HashMap<Int, FileModel> = HashMap()
   // var downloadedLecture = mutableListOf<String>()


    override fun selectMusic(musicName: DatabaseLectureModel) {
        musicState = musicState.copy(selectedLecture = musicName)
    }

    override suspend fun initialised(lectures: List<DatabaseLectureModel>) {

        withContext(Dispatchers.IO){

            musicState = musicState.copy(lectures = lectures )
            val currentLecture:DatabaseLectureModel? = lectures
                .firstOrNull {
                    it.lectureTitle == musicState.currentLecture!!.lectureTitle
                }
            musicState = musicState.copy(currentLecture = currentLecture)
            val downloadedLecture = mutableListOf<String>()

            filesDir.listFiles()
                .filter {
                it.name.endsWith(".mp3")
                }.forEach {
                downloadedLecture.add(it.name)
                }
            musicState = musicState.copy(downloadLectures =downloadedLecture)

        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun playMusic(
        playFromMusic:Boolean
    ) {

        val selectedMusic  = musicState.selectedLecture

        val fileUrl = if(playFromMusic) "${filesDir.path}/${selectedMusic!!.lectureTitle}" else "Http://localhost/${selectedMusic!!.lectureTitle}.mp3"
        try {
           GlobalScope.launch {
               mMediaPlayer =  MediaPlayer().apply {
                   setAudioAttributes(AudioAttributes.Builder()
                       .setUsage(AudioAttributes.USAGE_MEDIA)
                       .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                       .build()
                   )


                   setDataSource( fileUrl)
                   prepare()
                   start()
               }
               if (mMediaPlayer != null){
                   updateCurrentTime()
                   musicState = musicState.copy(currentLecture = selectedMusic)
               }
           }
        }
        catch (e:Exception){
            mMediaPlayer?.release()
            mMediaPlayer = null
        }

    }

    override suspend fun restartMusic() {
        job?.cancel()
        playMusic()
    }

    override suspend fun resumeMusic() {
        mMediaPlayer?.let {
            it.seekTo(it.currentPosition)
        }
        mMediaPlayer?.start()
        musicState= musicState.copy(isPaused = false)
        updateCurrentTime()
    }

    override fun stopMusic() {
        musicState = musicState.copy(isPaused = false)
        job?.cancel()
        mMediaPlayer?.reset()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
    override suspend fun finishedMusic() {
        delay(3000)
        nextMusic()
    }

    override suspend fun fastForward() {
        try {
            mMediaPlayer?.let{
                    mediaPlayer ->
                val seekTo = mediaPlayer.currentPosition + 15000
                if (seekTo < mediaPlayer.duration){
                    mediaPlayer.seekTo( seekTo)
                }
                else{
                    _error.emit("Reached End of player")
                }
            }
        }
        catch (e:Exception){
            _error.emit("$e Error occurs ")
        }
    }

    override suspend fun skipBackward() {
        try {
            mMediaPlayer?.let{
                mediaPlayer ->
                val seekTo = mediaPlayer.currentPosition - 15000
                if (seekTo > 0){
                    mediaPlayer.seekTo(seekTo )
                }
                else{
                    _error.emit("Reached End of player")
                }
            }
        }
        catch (e:Exception){
            _error.emit("$e Error occurs ")
        }
        }


    override suspend fun nextMusic() {
        val currentIndex = musicState.currentLecture!!.lectureId

        if( currentIndex != 0){
            //musicState = musicState.copy(selectedLecture = fileNames[currentIndex-1]?.name, currentIndex = currentIndex-1)
            val lecture: DatabaseLectureModel = musicState.lectures[currentIndex+1]
            selectMusic(lecture)
            playMusic()
        }
        val maxValue = musicState.lectures.lastIndex
       val  lecture = if(maxValue == currentIndex){
            musicState.lectures[0]
        } else{
            musicState.lectures[currentIndex+1]
        }
        selectMusic(lecture)
        playMusic()


    }

    override suspend fun previousMusic() {
       val currentIndex = musicState.currentLecture!!.lectureId

        if( currentIndex != 0){
         //musicState = musicState.copy(selectedLecture = fileNames[currentIndex-1]?.name, currentIndex = currentIndex-1)
            val lecture: DatabaseLectureModel = musicState.lectures[currentIndex-1]
            selectMusic(lecture)
            playMusic()
        }
        else{
            _error.emit("Reached the end of the player")
        }

    }

    override fun pauseMusic() {
        job?.cancel()
        try {
            if(mMediaPlayer?.isPlaying == true){
                mMediaPlayer?.pause()
                musicState = musicState.copy(isPaused = true)
            }
        }
        catch (e:Exception){
            mMediaPlayer?.release()
            mMediaPlayer  = null
        }
    }

    private  fun MediaPlayer.currentTime(){
        val hour = (this.currentPosition/3600000)% 24
        val min = (this.currentPosition/60000)% 60
        val sec = (this.currentPosition/1000)%60
        val currentTimeDisplay = ("${if(hour > 0){"$hour:"} else {""} } ${if (min<10){"0$min"} else min} :${if (sec<10){"0$sec"} else sec}")
        musicState = musicState.copy(currentTimeDisplay =currentTimeDisplay)

    }
    private  fun MediaPlayer.totalTime(){
        val hour = (this.duration/3600000)% 24
        val min = (this.duration/60000)% 60
        val sec = (this.duration/1000)%60
        val totalTimeDisplay = " ${if(hour > 0){"$hour:"} else {""} }   $min : $sec"
        musicState = musicState.copy(totalTimeDisplay =totalTimeDisplay )

    }
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun updateCurrentTime(){
        mMediaPlayer?.currentTime()
        mMediaPlayer?.totalTime()
        job = GlobalScope.launch {
            musicState = musicState.copy(totalTime = ((mMediaPlayer?.duration ?: 0) /60000.0))

            while (mMediaPlayer != null && !musicState.isPaused){
                delay(1000L)
                if (mMediaPlayer?.currentPosition!! >= mMediaPlayer?.duration!!){
                    finishedMusic()
                    break
                }
                musicState = musicState.copy(currentTime = mMediaPlayer?.currentPosition?.div(60000.0) ?: 0.0)
                mMediaPlayer?.currentTime()
            }
        }
    }
}