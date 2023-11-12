package com.crezent.network.api

import android.util.Log
import com.crezent.common.util.RequestResult
import com.crezent.common.util.getServerErrorResult
import com.crezent.models.DownloadStatus
import com.crezent.models.TempDownload
import com.crezent.network.models.ArtistPlaylistDto
import com.crezent.network.models.JwtToken
import com.crezent.network.models.LoginDto
import com.crezent.network.models.ResponseDto
import com.crezent.network.models.SongDto
import com.crezent.network.models.UserDto
import com.crezent.network.util.RealtimeAction
import com.crezent.network.util.RealtimeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.util.toByteArray
import io.ktor.utils.io.streams.asInput
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.FrameType
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.RandomAccessFile
import java.net.ConnectException
import kotlin.math.min

class KtorBaseApi (
    private val client: HttpClient,
): BaseApi {

    private val _songs: MutableStateFlow<List<SongDto>> = MutableStateFlow(emptyList())
    override val songs: StateFlow<List<SongDto>>
        get() = _songs.asStateFlow()
    private val _followings:MutableStateFlow<List<UserDto>> = MutableStateFlow(emptyList())

    override val followings: StateFlow<List<UserDto>>
        get() = _followings.asStateFlow()

    private val _followers:MutableStateFlow<List<UserDto>> = MutableStateFlow(emptyList())

    override val followers: StateFlow<List<UserDto>>
        get() = _followers.asStateFlow()

    override val artists: StateFlow<List<UserDto>>
        get() = TODO("Not yet implemented")

    private  var webSocketSession: WebSocketSession? = null

    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    init {
        coroutineScope.launch {
          //connect()
        }
    }






    override suspend fun connect() {
        try {
            Log.d("Socket","Connect in Ktorbase Api 1")
            webSocketSession = webSocketSession?:  client.webSocketSession("ws://10.42.0.1:8080/connect")
            webSocketSession!!.incoming
                .consumeEach {
                        frame ->
                    if (frame.frameType == FrameType.TEXT) {
                        //  frame.toString()
//                        val realtimeText = frame.toString()
//
//
//                        val realtimeResponse = realtimeText.toRealTimeResponse()
//                        updateRealtimeValues(realtimeResponse)
//                    }
                    }
                    Log.d("Socket","Each Collection $frame")
                }

//                .consumeAsFlow()
//                //  .filterIsInstance<Frame.Text>()
//                .filterNotNull()
//                .onEach {
//                        updates ->
//                    Log.d("Socket","Each Collection")
//

              //  }
               // .distinctUntilChanged()

        }
        catch (error:Exception){
            Log.d("Connect", "Failed to connect ${error.cause?.message}")
        }


    }

    private fun updateRealtimeValues(realtimeResponse: RealtimeResponse){
        when(realtimeResponse){
            is RealtimeResponse.Songs -> {
                _songs.update {
                    realtimeResponse.songs
                }
            }
            is RealtimeResponse.Followers -> {
                _followers.update {
                    realtimeResponse.followers
                }
            }
            is RealtimeResponse.Followings -> {
                _followings.update {
                    realtimeResponse.followings
                }
            }
        }
    }

    override suspend fun disconnect() : RequestResult<String> {
        return try {
            val closeReason = CloseReason(CloseReason.Codes.NORMAL,"Connection is close")
            webSocketSession?.close(closeReason)
            RequestResult.Success(closeReason.message)
        } catch (error:Exception){
            RequestResult.Error("${error.message}")

        }
    }

    override suspend fun sendAction(action: RealtimeAction):RequestResult<Unit> {
        try {
            val result =  webSocketSession?.outgoing?.trySend(Frame.Text(action.asString()))?:run {
                return  RequestResult.Error("Error")
            }
            return when{
                result.isSuccess -> {
                    RequestResult.Success(Unit)
                }

                result.isFailure -> {
                    val exception =  result.exceptionOrNull()
                    val errorMessage = exception?.message?:"Failed To Perform Action"
                    RequestResult.Error(errorMessage =errorMessage )
                }

                else ->{
                    val exception =  result.exceptionOrNull()
                    val errorMessage = exception?.message?:"Failed To Perform Action"
                    RequestResult.Error(errorMessage =errorMessage )
                }
            }

        }
        catch (error: ConnectException) {
            val errorMessage = error.message ?: "No connection"
           return RequestResult.Error(errorMessage)
        }
        catch (error: Exception) {

            val errorMessage = error.message ?: "Unknown Error"
            return RequestResult.Error(errorMessage)
        }
    }


    override suspend fun deleteAccount(password: String): RequestResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun authenticate(): Flow<RequestResult<Unit>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                val request = client.get(BaseApi.authenticate)
                if (request.status == HttpStatusCode.OK) {
                    emit(RequestResult.Success(Unit))
                } else {
                    emit(getServerErrorResult(request = request))

                }
            } catch (error: ConnectException) {
                emit(RequestResult.Error("${error.message}"))
            } catch (error: Exception) {
                emit(RequestResult.Error("${error.message}"))

            }
        }

    }

    override suspend fun getUserInfo(username: String?): RequestResult<UserDto> {
        return try {
            val url = if (username == null) {
                BaseApi.user
            } else {
                "${BaseApi.user}?username=$username"
            }
            val request = client.get(url)
            if (request.status == HttpStatusCode.OK) {
                val user = request.body<ResponseDto<UserDto>>().data
                RequestResult.Success(user)
            } else {
                getServerErrorResult(request = request)

            }

        } catch (error: ConnectException) {
            RequestResult.Error("${error.message}")
        } catch (error: Exception) {
            RequestResult.Error("${error.message}")

        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        displayName: String,
        emailAddress: String,
        profilePicture: ByteArray?,
        asArtist: String
    ): RequestResult<UserDto> {
        return try {
            val request = client.post(BaseApi.signUp) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("username", username)
                            append("password", password)
                            append("displayName", displayName)
                            append("emailAddress", emailAddress)
                            append("asArtist", asArtist)
                            val contentType = ContentType.MultiPart.FormData
                            profilePicture?.let {
                                append(
                                    "profilePicture", InputProvider(it.size.toLong()) {
                                        it.inputStream().asInput()
                                    }, Headers.build {
                                        append(HttpHeaders.ContentType, contentType.toString())
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            " filename=hello.jpg"
                                        )
                                    })
                            }

                        }
                    )
                )
            }
            if (request.status == HttpStatusCode.OK) {
                val userDto = request.body<UserDto>()
                RequestResult.Success(userDto)
            } else {
                getServerErrorResult(request = request)
            }
        } catch (error: ConnectException) {
            val errorMessage = error.message ?: "No connection"
            RequestResult.Error(errorMessage)
        } catch (error: Exception) {

            val errorMessage = error.message ?: "Unknown Error"
            RequestResult.Error(errorMessage)
        }
    }


    override suspend fun signIn(username: String, password: String): RequestResult<String> {
        return try {
            val loginDto = LoginDto(
                username = username,
                password = password
            )
            val request = client.post(BaseApi.signIn) {
                contentType(ContentType.Application.Json)
                setBody(loginDto)
            }
            if (request.status == HttpStatusCode.OK) {
                val jwtToken = request.body<JwtToken>().token
                RequestResult.Success(jwtToken)
            } else {
                getServerErrorResult(request = request)
            }

        } catch (error: ConnectException) {
            RequestResult.Error("${error.message}")
        } catch (error: Exception) {
            RequestResult.Error("${error.message}")

        }

    }

    override suspend fun updateProfile(
        username: String?,
        password: String?,
        displayName: String?,
        emailAddress: String?,
        asArtist: Boolean?,
        profilePicture: ByteArray?,
        profilePictureRemove: String
    ): RequestResult<UserDto> {
        return try {
            val request = client.put(BaseApi.editProfile) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            password?.let {
                                append("password", password)
                            }
                            displayName?.let {
                                append("displayName", displayName)

                            }
                            emailAddress?.let {
                                append("emailAddress", emailAddress)
                            }
                            asArtist?.let {
                                val isArtistValue = if (asArtist == false) "false" else "true"
                                append("isArtist", isArtistValue)
                            }

                            profilePicture?.let {
                                append("profilePicture", it, Headers.build {
                                    append(HttpHeaders.ContentType, "image/png")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        " filename=username.jpg"
                                    )
                                })
                            }

                        }
                    )
                )
            }
            val updatedProfile = request.body<UserDto>()
            if (request.status == HttpStatusCode.OK) {
                RequestResult.Success(updatedProfile)
            }
            else {
                getServerErrorResult(request = request)
            }

        }
        catch (error: ConnectException) {
            RequestResult.Error("${error.message}")
        } catch (error: Exception) {
            RequestResult.Error("${error.message}")

        }
    }


    override suspend fun getSongs(
        searchQuery: String,
        artistUsername: String?,
    ): RequestResult<List<SongDto>> {
        val requestUrl =  if (   artistUsername != null){
            "${BaseApi.song}?username=$artistUsername"
        }
        else {
            "${BaseApi.song}?search=$searchQuery"
        }
        return try {
            val request = client.get(requestUrl)

            if (request.status == HttpStatusCode.OK){
                val songs = request.body<List<SongDto>>()
                RequestResult.Success(songs)
            }
            else {
                getServerErrorResult(request = request)
            }
        } catch (error:ConnectException){
            RequestResult.Error("${error.message}")
        } catch (error:Exception){
            RequestResult.Error("${error.message}")
        }
    }

    override suspend fun getSongById(songId: String): RequestResult<SongDto> {
        return try {
            val request = client.get("${BaseApi.song}?songId=$songId")
            if (request.status == HttpStatusCode.OK){
                val song = request.body<SongDto>()
                RequestResult.Success(song)
            }
            else {
                getServerErrorResult(request = request)
            }

        } catch (error:ConnectException){
            RequestResult.Error("${error.message}")
        } catch (error:Exception){
            RequestResult.Error("${error.message}")
        }

    }

    override suspend fun getArtistPlaylist(artistUsername: String?): RequestResult<List<ArtistPlaylistDto>> {
        val requestUrl = if (artistUsername != null){
            "${BaseApi.playlist}?username=$artistUsername"
        }
        else{
           BaseApi.playlist
        }
        val request = client.get(requestUrl)
        return if (request.status == HttpStatusCode.OK){
            val playlists = request.body<List<ArtistPlaylistDto>>()
            RequestResult.Success(playlists)
        } else {
            getServerErrorResult(request = request)
        }

    }

    override suspend fun getPlaylistById(playlistId: String): RequestResult<ArtistPlaylistDto> {

        return try {
            val request = client.get("${BaseApi.playlist}?playlistId=$playlistId")
            val playlist = request.body<ArtistPlaylistDto>()
            if (request.status == HttpStatusCode.OK){
                RequestResult.Success(playlist)
            }
            else {
                getServerErrorResult(request = request)
            }

        } catch (error:ConnectException){
            RequestResult.Error("${error.message}")
        } catch (error:Exception){
            RequestResult.Error("${error.message}")
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun downloadAudio(
        songId: String,
        audioName: String,
        imageName: String?,
        tempFileChange: (File, File?) -> Unit,
        tempDownload: TempDownload
    ): Flow<RequestResult<Pair<ByteArray, ByteArray?>>> {


        val tempAudioFile = tempDownload.tempAudioFile!!
        val tempThumbnailFile = tempDownload.tempThumbnailFile

        return withContext(Dispatchers.IO){
            callbackFlow {
                val chunkSize = 1024
                try {

                    //If it is already downloading
                    if (tempDownload.downloadStatus == DownloadStatus.DOWNLOADING) return@callbackFlow
                    val audioUrl = "${BaseApi.downloadAudio}/$audioName"
                    val audioLength = client.head(audioUrl).headers[HttpHeaders.ContentLength]?.toLong() as Long

                    val thumbnailUrl:String? = imageName?.let {
                        "${BaseApi.downloadThumbnail}/$imageName"
                    }

                    val thumbnailLength = thumbnailUrl?.let {
                        client.head(thumbnailUrl).headers[HttpHeaders.ContentLength]?.toLong() as Long
                    }?:0
                    val totalDownloadLength = audioLength + thumbnailLength

                    var audioStart = tempAudioFile.length()

                    val audioLastByte = audioLength-1
                    send(RequestResult.Loading())
                   /// tempDownloadHashmap.replace(songId,tempFile.copy(downloadStatus = DownloadStatus.DOWNLOADING) )

                    val audioRaf =  RandomAccessFile(tempAudioFile,"rw" )

                    while (isActive){
                        val end = min(audioStart+ chunkSize, audioLastByte)// (1000+1024 , 2000)
                        if (audioStart > end) {
                            break
                        }
                        val audioRangeHeader = "bytes=$audioStart-$end" //(1000- 2000)// 1000

                        val request =  client.get(audioUrl){ header("Range", audioRangeHeader) }
                        audioRaf.seek(audioStart)
                        audioRaf.write(request.content.toByteArray())

                        // request.content.copyTo(outputStream)//copy 1000 bytes
                        if (request.status != HttpStatusCode.PartialContent){
                            tempFileChange(tempAudioFile,tempThumbnailFile)
                            send(getServerErrorResult(request = request))
                          //  tempDownloadHashmap.replace(songId,tempFile.copy(downloadStatus = DownloadStatus.ERROR) )
                            cancel()
                            break
                        }
                        val progress = (end.toFloat() /totalDownloadLength) * 100
                        send(RequestResult.Loading(progress = progress.toInt()))
                        if (end >= audioLastByte) {//2000
                            audioRaf.close()
                            break
                        }
                        audioStart += chunkSize
                    }

                    val audioData = tempAudioFile.readBytes()
                    // val audioData = raf.
                    if (imageName == null){
                        send(RequestResult.Success(Pair(audioData, null)))
                        //tempDownloadHashmap.remove(songId)
                        close()
                        return@callbackFlow
                    }

                    val thumbnailRaf =  RandomAccessFile(tempThumbnailFile,"rw" )
                    var thumbnailStart = tempThumbnailFile!!.length()
                    val thumbnailLastByte = thumbnailLength -1

                    while (isActive){
                        val end = min(thumbnailStart+chunkSize , thumbnailLastByte)
                        val request = client.get(thumbnailUrl!!){
                            header("Range", "bytes=$thumbnailStart-$end")
                        }
                        if (request.status != HttpStatusCode.PartialContent){
                            send(getServerErrorResult(request))
                           // downloadStatusChange(DownloadStatus.ERROR)
                            //tempDownloadHashmap.replace(songId,tempFile.copy(downloadStatus = DownloadStatus.ERROR) )
                            cancel()
                        }
                        thumbnailRaf.seek(thumbnailStart)
                        thumbnailRaf.write(request.content.toByteArray())
                        val progress = ((end + audioStart).toFloat()  /totalDownloadLength) * 100
                        send(RequestResult.Loading(progress = progress.toInt()))
                        if (end >=thumbnailLastByte) {
                           // tempDownloadHashmap.remove(songId)
                            thumbnailRaf.close()
                            break
                        }
                        thumbnailStart += chunkSize

                    }
                    val thumbnailData = tempThumbnailFile.readBytes()
                    send(RequestResult.Success(Pair(audioData, thumbnailData)))
                    close()

                }
                catch (error:ConnectException){

                    //tempDownloadHashmap.replace(songId,tempFile.copy(downloadStatus = DownloadStatus.ERROR) )
                    send(RequestResult.Error("${error.message}"))
                } catch (error:Exception){
                    //tempDownloadHashmap.replace(songId,tempFile.copy(downloadStatus = DownloadStatus.ERROR) )
                    send(RequestResult.Error("${error.message}"))

                }
                finally {
                    send(RequestResult.Loading(false))
                    awaitClose {  }

                }
            }
        }


    }

}

