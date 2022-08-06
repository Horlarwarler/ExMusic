package com.example.talimlectures.viewModels
import android.content.ContentValues.TAG
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.*
import com.example.talimlectures.data.local.repository.NetworkRepository
import com.example.talimlectures.data.local.repository.LectureRepository
import com.example.talimlectures.domain.dto.NetworkLectures
import com.example.talimlectures.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val lectureRepository: LectureRepository, private val networkRepository: NetworkRepository):ViewModel(
) {
    // Creating mutableStateOf object and Assign it to a immutable
    private val _allDatabaseCategoryModel = MutableStateFlow<List<DatabaseCategoryModel>>(emptyList())
    val allDatabaseCategoryModel: StateFlow<List<DatabaseCategoryModel>> = _allDatabaseCategoryModel
    val selectedCategory = mutableStateOf(0)
    private  val _favouriteLectures = MutableStateFlow<List<DatabaseLectureModel>>(emptyList())
    val favouriteLectures: StateFlow<List<DatabaseLectureModel>> = _favouriteLectures
    private  val _allLectures = MutableStateFlow<List<DatabaseLectureModel>>(emptyList())

    val allLectures: StateFlow<List<DatabaseLectureModel>> = _allLectures
    private  val _searchLectures = MutableStateFlow<List<DatabaseLectureModel>>(emptyList())
    val searchLectures:StateFlow<List<DatabaseLectureModel>> = _searchLectures
    private  val _categoryLectures = MutableStateFlow<List<DatabaseLectureModel>>(emptyList())
    val categoryLectures:StateFlow<List<DatabaseLectureModel>> = _categoryLectures
    private val _selectedLecture:MutableStateFlow<DatabaseLectureModel?> = MutableStateFlow(null)
    val selectedLecture:StateFlow<DatabaseLectureModel?> = _selectedLecture
    private val _allNewAddedLectures = MutableStateFlow<List<NewlyAdded>>(emptyList())
    val allNewlyAddedLecture: StateFlow<List<NewlyAdded>> = _allNewAddedLectures

    private val _allRecentPlayed = MutableStateFlow<List<RecentlyPlayed>>(emptyList())
    val recentlyPlayed: StateFlow<List<RecentlyPlayed>> = _allRecentPlayed
    //   To be reviewd
//    private val _selectedFavourite:MutableStateFlow<Favourite?> = MutableStateFlow(null)
//    private val selectedFavourite:StateFlow<Favourite?> = _selectedFavourite
//    private val _selectedNewlyAdded:MutableStateFlow<NewlyAdded?> = MutableStateFlow(null)
//    val selectedNewlyAdded:StateFlow<NewlyAdded?> = _selectedNewlyAdded

    val isFavourite = mutableStateOf(false)


    private val _selectedRecentPlayed:MutableStateFlow<RecentlyPlayed?> = MutableStateFlow(null)
    val miniPLayerVisible = mutableStateOf(false)
     val searchActionState = mutableStateOf(SearchAction.IDLE)
  //  val playActionState = mutableStateOf(Pla)
    val playActionState = mutableStateOf(PlayAction.IDLE)
    val searchText = mutableStateOf("")
    val closeButtonVisible = mutableStateOf(false)
    val playingLectureId:MutableStateFlow<Int?> = MutableStateFlow(null)
    val isPlaying = mutableStateOf(false)
    private  var  mMediaPlayer:MediaPlayer? = null
    val paused = mutableStateOf(false)
    private val dir = "/storage/emulated/0/talimlecture/"

     var lecturePath:String? = null
    private val _currentPosition = MutableStateFlow(0.0)
    val currentPosition:StateFlow<Double> = _currentPosition
    val _totalTime = MutableStateFlow(0.1)
    val totalTime:StateFlow<Double> = _totalTime
    private val _displayTimeStart = MutableStateFlow(": :")
    val displayTimeStart:StateFlow<String> = _displayTimeStart
    private val _displayTotalTime = MutableStateFlow(": :")
    val displayTotalTime:StateFlow<String> = _displayTotalTime
   val previousLecture:MutableStateFlow<Int?> = MutableStateFlow(null)
    val displayDownloadDialog:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val lectureExit:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val databaseAction = mutableStateOf(DatabaseAction.NO_ACTION)
    val favouriteId:MutableStateFlow<Int?> = MutableStateFlow(null)
    val downloadState:MutableStateFlow<DownloadState> = MutableStateFlow(DownloadState.IDLE)
    val downloadLectureId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val downloadedLectureId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val downloadngLectureId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val downloadAnimation :MutableStateFlow<Boolean> = MutableStateFlow(false)
    val downloadQue:MutableStateFlow<MutableList<Map<Int,String>>> = MutableStateFlow(mutableListOf())
    val downloadIds:MutableStateFlow<MutableList<Int>> = MutableStateFlow(mutableListOf())
    val networkLectures:MutableStateFlow<NetworkLectures?> =  MutableStateFlow(null)
    val requestState:MutableStateFlow<RequestState>   = MutableStateFlow(RequestState.IDLE)
    val networkState = mutableStateOf(false)


    init {
        Log.d("Init", "The init message is called")
        getCategoryLecture(0)
        getNetworkLecture()
        getAllLectures()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getNetworkState(context:Context){


        val connectivityManager: ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.getActiveNetworkInfo()
        networkState.value = activeNetwork!=null && activeNetwork.isConnectedOrConnecting
        if(!networkState.value){
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network : Network) {
                    Log.e(TAG, "The default network is now: " + network)
                    networkState.value = true
                }

                override fun onLost(network : Network) {
                    networkState.value = false
                }


            })
        }



    }


    fun loadingScreen(){
        requestState.value = RequestState.LOADING
        if(allLectures.value.isNotEmpty()){
            requestState.value = RequestState.SUCCESS
        }
        else{

        }
    }

     fun getNetworkLecture(){
        requestState.value = RequestState.LOADING

//        viewModelScope.launch {
//            networkRepository.getAllLecture().enqueue(
//                object : Callback<NetworkLectures?> {
//                    override fun onResponse(
//                        call: Call<NetworkLectures?>,
//                        response: Response<NetworkLectures?>,
//                    ) {
//                        if(response.isSuccessful){
//                           networkLectures.value =  response.body()
//                            databaseAction.value = DatabaseAction.ADD_LECTURE
//
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<NetworkLectures?>, t: Throwable) {
//                        if(t is HttpException){
//                            println("This is error")
//                        }
//                    }
//                }
//            )
//        }
    }

    fun getAllCategory(){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.allCategory.collect { category->
//                _allCategory.value = category
//
//            }
//        }
    }

    fun  getSelectedCategory(categoryId:Int){
        selectedCategory.value = categoryId
    }
    fun insertCategory(databaseCategoryModel: DatabaseCategoryModel){
//        viewModelScope.launch(Dispatchers.IO){
//            lectureRepository.insertCategory(category= category)
//        }
    }
    fun deleteCategory(databaseCategoryModel: DatabaseCategoryModel){
        viewModelScope.launch(Dispatchers.IO){
            lectureRepository.deleteCategory(databaseCategoryModel= databaseCategoryModel)
        }
    }
    fun updateCategory(databaseCategoryModel: DatabaseCategoryModel){
        viewModelScope.launch(Dispatchers.IO){
            lectureRepository.updateCategory(databaseCategoryModel= databaseCategoryModel)
        }
    }

    fun favouriteLectures(){
//        viewModelScope.launch(Dispatchers.IO) {
//           lectureRepository.favouriteLecture.collect {
//               _favouriteLectures.value = it
//           }
//        }
    }
//    fun  getFavouriteLecture(id:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getSelectedFavourite(selectedFavourite = id).collect{
//                    favourite->
//                _selectedFavourite.value = favourite
//                favouriteId.value = null
//            }
//        }
//    }
   @OptIn(DelicateCoroutinesApi::class)
   suspend fun checkAllLecture(){
    //getNetworkLecture()
       viewModelScope.launch(Dispatchers.IO) {
           for (lectureIndex in 0 until 20){
                val lectureId = lectureIndex

               if (!lectureInDb(lectureId)){
                   println("Lecture doesnot exist $lectureId")
               }
               else {
                   println("Lecture exist $lectureId")
               }



            }

       }.join()

    }
    @OptIn(DelicateCoroutinesApi::class)
    private suspend   fun lectureInDb(
        lectureId:Int):Boolean {
        Log.d("Lecture in db"," Starting repo")
        var lectureExist = false

        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getSelectedLecture(lectureId).collect {
//                    lecture ->
//                Log.d("Lecture in db"," In the  repo")
//                lectureExist = lecture != null
//            }
            delay(2000)
            if(lectureId % 2 == 0){
                lectureExist = true
            }
        }.join()
        Log.d("Lecture in db"," After repo")

        return lectureExist

    }
    fun addToFavourite(){
//        viewModelScope.launch(Dispatchers.IO){
//            val newlecture = Lecture(
//                lectureId = selectedLecture.value!!.lectureId,
//                lectureTitle = selectedLecture.value!!.lectureTitle ,
//                lectureDescription = selectedLecture.value!!.lectureDescription,
//                categoryId = selectedLecture.value!!.categoryId,
//                length = selectedLecture.value!!.length,
//                date = selectedLecture.value!!.date,
//                favourite = 1,
//                 path = selectedLecture.value!!.path,
//                metaData = selectedLecture.value!!.metaData
//            )
//            delay(1000)
//            lectureRepository.updateLecture(newlecture)
//        }
    }
    fun deleteFromFavourite(){
//        println("This is the  _selected value ${favouriteId.value}")
//        viewModelScope.launch(Dispatchers.Main) {
//            if(favouriteId.value != null){
//               getSelectedLecture(favouriteId.value!!)
//                delay(1000)
//            }
//            val newlecture = Lecture(
//                lectureId = selectedLecture.value!!.lectureId,
//                lectureTitle = selectedLecture.value!!.lectureTitle ,
//                lectureDescription = selectedLecture.value!!.lectureDescription,
//                categoryId = selectedLecture.value!!.categoryId,
//                length = selectedLecture.value!!.length,
//                date = selectedLecture.value!!.date,
//                favourite = 0,
//                path = selectedLecture.value!!.path,
//                metaData = selectedLecture.value!!.metaData
//
//            )
//            lectureRepository.updateLecture(newlecture)
//
//
//
//        }
    }

    fun getAllLectures(){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.allLecture.collect {lectures->
//                _allLectures.value = lectures
//                lectures.forEach{
//                        lecture ->
//                    if(lecture.favourite == 1){
//                        isFavourite.value = true
//                    }
//                }
//            }
//        }
    }
    fun searchLectures(searchQuery:String){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.searchLecture("%$searchQuery%").collect {
//                _searchLectures.value = it
//            }
//        }
    }
    fun getCategoryLecture(categoryId:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getAllLectures(categoryId =categoryId ).collect {
//                _categoryLectures.value = it
//            }
//        }

    }
    fun  getSelectedLecture(lectureId:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getSelectedLecture(lectureId = lectureId).collect{
//                _selectedLecture.value = it
//            }
//        }

    }
    fun addLecture(lecture:DatabaseLectureModel){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.addLecture(lecture= lecture)
//        }
    }
    fun updateLecture(lecture:DatabaseLectureModel){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.updateLecture(lecture= lecture)
        }
    }
    fun deleteLecture(lecture:DatabaseLectureModel){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.deleteLecture(lecture= lecture)
        }
    }
    fun getNewAddedLectures(){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.allNewLectures.collect {
//                _allNewAddedLectures.value  = it
//            }
//        }
    }
//    fun  getSelectedNewLecture(id:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getSelectedNewLectures(idSelected = id).collect{
//                    newLectures->
//                _selectedNewlyAdded.value = newLectures
//            }
//        }
//    }

    fun deleteNewLecture(newlyAdded: NewlyAdded){
//        viewModelScope.launch(Dispatchers.IO){
//            lectureRepository.deleteNewLectures(newlyAdded = newlyAdded)
//        }
    }

    fun getRecentlyPlayed(){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.allRecentPlayed.collect {
//                _allRecentPlayed.value  = it
//            }
//        }
    }
//    fun  getSelectedRecentPlayed(id:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            lectureRepository.getSelectedRecentPlayed(idSelected = id).collect{
//                    recentPlayed->
//                _selectedRecentPlayed.value = recentPlayed
//            }
//        }
//    }
    fun addRecentPlayed(){

//        val newRecentlyPlay = RecentlyPlayed(
////            lectureId = selectedLecture.value!!.lectureId,
////            description = selectedLecture.value!!.lectureDescription,
////            lectureTitle = selectedLecture.value!!.lectureTitle
//        )
        viewModelScope.launch(Dispatchers.IO){
            try {
                println("addRecentPlayed: delete1 ")
                //lectureRepository.insertRecentLecture(recentlyPlayed =newRecentlyPlay )

                if(recentlyPlayed.value.size == 5){
                   deleteRecentlyPlayed()
                }
                println("addRecentPlayed: delete2 ")

            }
            catch(e:Exception){

            }
        }
    }
    fun deleteRecentlyPlayed(){
        viewModelScope.launch(Dispatchers.IO){
            val lectureDelete = recentlyPlayed.value.last()
            println("addRecentPlayed: delete3 ")
          //  lectureRepository.deleteRecentPlayItem(recentlyPlayed =lectureDelete )
        }
    }

    fun handleDatabaseAction(action: DatabaseAction){
        when(action){
            DatabaseAction.ADD_LECTURE ->{
                println("Here i was called")
               // addLecture()
            }
            DatabaseAction.DELETE_LECTURE ->{

            }
            DatabaseAction.ADD_FAVOURITE ->{
                    addToFavourite()
            }
            DatabaseAction.DELETE_FAVOURITE ->{
                println("I have been favourite")
                deleteFromFavourite()
            }
            DatabaseAction.ADD_NEWLY_ADDED ->{

            }
            DatabaseAction.DELETE_NEWLY_ADDED ->{

            }
            DatabaseAction.ADD_RECENTLY_PLAY ->{
                addRecentPlayed()
            }
            DatabaseAction.DELETE_RECENTLY_PLAY ->{
                deleteRecentlyPlayed()
            }
            else -> {

            }
        }
        databaseAction.value = DatabaseAction.NO_ACTION
    }

    fun handlePlayAction(playAction: PlayAction){

        when(playAction){
            PlayAction.FORWARD ->{
                forward()
            }
            PlayAction.PLAY->{
                println("I am playing")
                play()
            }
            PlayAction.NEXT ->{
                next()
            }
            PlayAction.PAUSE ->{
                println("I have paused")
                pause()
            }
            PlayAction.PREVIOUS ->{
                previous()
            }
            PlayAction.REWIND ->{
                rewind()
            }
            PlayAction.STOP ->{
                stop()
            }
            else -> {

            }
        }
        playActionState.value = PlayAction.IDLE


    }

    fun handleDownloadState(state: DownloadState){
        when(state){
            DownloadState.START_DOWNLOAD->{
                    startDownload()
            }
            DownloadState.DOWNLOADING -> {
                    downloading()
            }
            DownloadState.CANCEL_DOWNLOAD ->{
               downloadCancel()
            }
            DownloadState.FINISH_DOWNLOAD ->{
                   finishDownload()
            }
            else  ->{

            }
        }

    }

    fun lectureExits(lectureId:Int):Boolean{
        lecturePath ="$dir$lectureId.mp3"
        val lectureToPlay = File(lecturePath)
        return (lectureToPlay.exists())
    }
    private fun startDownload(){
        downloadAnimation.value = true
        downloadLectureId.value?.let {
            val downloadLecture = mapOf(downloadLectureId.value!! to "http//:${downloadLectureId.value}")
            downloadQue.value.add(downloadLecture)
            downloadIds.value.add(downloadLectureId.value!!)
            downloadLectureId.value = null
        }
        downloadState.value = DownloadState.DOWNLOADING

    }
    private fun downloading(){
        println("I am downloading ")
        downloadngLectureId.value =  downloadIds.value[0]
        viewModelScope.launch(Dispatchers.IO) {
            for(i in 1..10){
                delay(2000)
            }
            downloadState.value = DownloadState.FINISH_DOWNLOAD

        }
    }

    fun finishDownload(){
        downloadedLectureId.value =  downloadIds.value[0]
        downloadIds.value.removeAt(0)
        downloadQue.value.removeAt(0)
        if (downloadQue.value.size == 0){
            downloadAnimation.value = false
        }
        else{
            downloadState.value = DownloadState.START_DOWNLOAD
          //  downloadngLectureId.value = downloadIds.value[0]
        }

    }
    fun downloadCancel(){
        downloadAnimation.value = true
    }


    // Play function
    private fun play()
    {
        // If a  new lecture is selected
        if (mMediaPlayer != null  && selectedLecture.value?.lectureId != previousLecture.value ){
            println("I am restarting")
            paused.value = false
            previousLecture.value = selectedLecture.value!!.lectureId
            restart()
        }
        // If the lecture is playing
        else if(paused.value){
            println("I am resuming")
            mMediaPlayer?.let {
                it.seekTo(it.currentPosition)
            }
            mMediaPlayer?.start()
            paused.value = false
            updateCurrentTime()
            println("${selectedLecture.value!!.lectureId} and this is ${previousLecture.value}")
        }

        // Starting a new lecture and Initialinzing the Media Player
        else{
            println("I am playing a new item")
            try {
                val lectureId = selectedLecture.value!!.lectureId
                if (!lectureExits(lectureId)){
                    if(networkState.value){
                        lecturePath = "https://www.tonesmp3.com/ringtones/main-tumhara-dil-bechara.mp3"
                    }
                    else{
                        lecturePath = null
                    }
                }
                lecturePath?.let{
                    lecturePath ->
                    mMediaPlayer =  MediaPlayer().apply {
                        setAudioAttributes(AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                        )
                        setDataSource( lecturePath)
                        prepare()
                        start()
                    }
                }
                if (mMediaPlayer != null){
                    println("I have been initailized")
                    updateCurrentTime()
                    previousLecture.value = selectedLecture.value!!.lectureId
                    databaseAction.value = DatabaseAction.ADD_RECENTLY_PLAY
                }
            }
            catch(e:Exception){
                println("Error exists $e ")
                mMediaPlayer?.release()
                mMediaPlayer = null
            }
        }

   }
    //Pause function
    private  fun pause(){
        try {
            if(mMediaPlayer?.isPlaying == true){
                mMediaPlayer?.pause()
                paused.value = true
            }
        }
        catch (e:Exception){
            mMediaPlayer?.release()
            mMediaPlayer  = null
        }
    }
    private fun forward(){
        if (mMediaPlayer != null){
            try {
                mMediaPlayer?.let{
                    it.seekTo(it.currentPosition + 15000 )
                }
            }
            catch (e:Exception){
            }
        }
    }
    private fun rewind(){
        if (mMediaPlayer != null){
            try {
                mMediaPlayer?.let{
                    it.seekTo(it.currentPosition - 15000 )
                }
            }
            catch (e:Exception){
            }
        }

    }
    private  fun next(){
        println("${selectedLecture.value!!.lectureId} lecture anf the size is  ${allLectures.value.size} ")
        for (lectureId in selectedLecture.value!!.lectureId+1..10 ){
            println("Lecture exists with $lectureId id  ")
            if(lectureExits(lectureId)){
                println("Lecture exists with $lectureId id  ")
                handlePlayAction(PlayAction.STOP)
                getSelectedLecture(lectureId)
                playingLectureId.value  = lectureId
                handlePlayAction(PlayAction.PLAY)
                break
            }
            else{
                println("Lecture exists with $lectureId id  ")
            }
        }
    }
    private  fun previous(){
        for (lectureId in selectedLecture.value!!.lectureId-1  downTo   0 ){
            println("Lecture exists with $lectureId id  ")
            if(lectureExits(lectureId)){
                println("Lecture exists with $lectureId id  ")
                handlePlayAction(PlayAction.STOP)
                getSelectedLecture(lectureId)
                playingLectureId.value  = lectureId
                handlePlayAction(PlayAction.PLAY)
                break
            }
            else{
                println("Lecture exists with $lectureId id  ")
            }

        }
    }
    private  fun stop(){
        paused.value = false

        mMediaPlayer?.reset()
        updateCurrentTime()
        mMediaPlayer?.release()
        mMediaPlayer = null

    }
    private  fun restart(){
        stop()
        play()
    }
    private fun updateCurrentTime(){

        viewModelScope.launch {

            _totalTime.value = ((mMediaPlayer?.duration ?: 0) /60000.0)
            _displayTotalTime.value = mMediaPlayer?.totalTime().toString()
            while (mMediaPlayer != null && !paused.value){
                delay(1000L)
                if (mMediaPlayer?.currentPosition!! >= mMediaPlayer?.duration!!){
                    println("I have stoped")
                   stop()
                    break
                }
                _currentPosition.value = mMediaPlayer?.currentPosition?.div(60000.0) ?: 0.0
                mMediaPlayer?.currentTime()
            }
        }

    }
    private  fun MediaPlayer.currentTime(){
        val hour = (this.currentPosition/3600000)% 24
        val min = (this.currentPosition/60000)% 60
        val sec = (this.currentPosition/1000)%60
        _displayTimeStart.value = ("${if(hour > 0){"$hour:"} else {""} } ${if (min<10){"0$min"} else min} :${if (sec<10){"0$sec"} else sec}")

    }
    private  fun MediaPlayer.totalTime():String{
        val hour = (this.duration/3600000)% 24
        val min = (this.duration/60000)% 60
        val sec = (this.duration/1000)%60
        return(" ${if(hour > 0){"$hour:"} else {""} }   $min : $sec")

    }

}

