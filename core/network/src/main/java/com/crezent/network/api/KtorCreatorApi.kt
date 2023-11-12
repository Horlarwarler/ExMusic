package com.crezent.network.api

import android.util.Log
import com.crezent.common.util.RequestResult
import com.crezent.common.util.getServerErrorResult
import com.crezent.models.ArtistPlaylist

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException

class KtorCreatorApi(private val client:HttpClient) :
    CreatorApi {

    override fun uploadSong(
        title:String,
        description:String,
        length: Double,
        songByteArray:ByteArray,
        thumbnailByteArray:ByteArray?
    ): Flow<RequestResult<Unit>> {
       return channelFlow {
           send(RequestResult.Loading())

           try {
               val request =  client.post(CreatorApi.uploadSong){
                   setBody(
                       MultiPartFormDataContent(
                           formData {
                               append("title",title)
                               append("description",description)
                               append("length",length)
                               val contentType = ContentType.MultiPart.FormData
                               thumbnailByteArray?.let {
                                   append("thumbnail", thumbnailByteArray, Headers.build {
                                       append(HttpHeaders.ContentType,contentType.toString())
                                       append(HttpHeaders.ContentDisposition," filename=thumbnail")
                                   })
                               }
                                   append("song", songByteArray, Headers.build {
                                       append(HttpHeaders.ContentType,contentType.toString())
                                       append(HttpHeaders.ContentDisposition," filename=song")
                                   })

                           }
                       )
                   )
                   onUpload { bytesSentTotal, contentLength ->
                       val percent = (bytesSentTotal.toFloat()/contentLength.toFloat()) * 100
                       Log.d("LOG", "percent ${percent.toInt()}")
                       send(RequestResult.Loading(progress = percent.toInt()))
                   }
               }
                if (request.status == HttpStatusCode.OK){
                   send( RequestResult.Success(Unit))
               } else{
                   val errorResult =  getServerErrorResult<Unit>(request = request)
                    send(errorResult)


               }
           }
           catch (error: ConnectException){
               Log.e("TAG", "CONNECT EXCEPTION")
               send( RequestResult.Error("${error.message}"))
           }
           catch (error:Exception){
               Log.e("TAG", "CONNECT ${error.message}")
               send( RequestResult.Error("${error.message}"))
           }
           finally {
               send(RequestResult.Loading(false))
           }
       }
    }

    override  fun updateSong(
        songId: String,
        title:String?,
        description:String?,
        length: Double?,
        songByteArray: ByteArray?,
        thumbnailByteArray: ByteArray?
    ): Flow<RequestResult<Unit>> {
        return callbackFlow {
            send(RequestResult.Loading())
            try {
                val request =  client.put(CreatorApi.editSong){
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                title?.let {
                                    append("title",title)

                                }
                                description?.let {
                                    append("description",description)

                                }
                                length?.let {
                                    append("length",length)

                                }
                                append("songId",songId)


                                val contentType = ContentType.MultiPart.FormData
                                thumbnailByteArray?.let {
                                    append("thumbnail", thumbnailByteArray, Headers.build {
                                        append(HttpHeaders.ContentType,contentType.toString())
                                        append(HttpHeaders.ContentDisposition," filename=thumbnail")
                                    })

                                }
                                songByteArray?.let {
                                    append("song", songByteArray, Headers.build {
                                        append(HttpHeaders.ContentType,contentType.toString())
                                        append(HttpHeaders.ContentDisposition," filename=song")
                                    })
                                }


                            }
                        )
                    )
                    onUpload { bytesSentTotal, contentLength ->
                        val percent = (contentLength/bytesSentTotal) * 100
                        send(RequestResult.Loading(progress = percent.toInt()))
                    }
                }
                if (request.status == HttpStatusCode.OK){
                    send(RequestResult.Success(Unit))
                } else{

                   send(getServerErrorResult(request = request))

                }
            }
            catch (error: ConnectException){
                Log.e("TAG", "CONNECT EXCEPTION")
                send( RequestResult.Error("${error.message}"))
            }
            catch (error:Exception){
                Log.e("TAG", "CONNECT ${error.message}")
                send( RequestResult.Error("${error.message}"))
            }
            finally {
                send(RequestResult.Loading(false))
            }
        }
    }

    override fun deleteSong(songId: String): Flow<RequestResult<Unit>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                val request =  client.delete("${CreatorApi.editSong}?songId=$songId")
                if (request.status == HttpStatusCode.OK){
                   emit(RequestResult.Success(Unit))
                } else{
                    emit( getServerErrorResult(request = request))

                }
            }
            catch (error: ConnectException){
                Log.e("TAG", "CONNECT EXCEPTION")
                 emit( RequestResult.Error("${error.message}"))
            }
            catch (error:Exception){
                Log.e("TAG", "CONNECT ${error.message}")

                emit( RequestResult.Error("${error.message}"))

            }
            finally {
                emit(RequestResult.Loading(false))
            }
        }

    }

    override fun createPlaylist(playlistName:String): Flow<RequestResult<ArtistPlaylist>> {
        return   flow {
            emit(RequestResult.Loading())
            try {
                val request =  client.post("${CreatorApi.createPlaylist}?playlistName=$playlistName")
                if (request.status == HttpStatusCode.OK){
                    val playlist = request.body<ArtistPlaylist>()
                    emit(RequestResult.Success(playlist))
                } else{
                    emit(getServerErrorResult(request = request))

                }
            }
            catch (error: ConnectException){
                Log.e("TAG", "CONNECT EXCEPTION")
                emit( RequestResult.Error("${error.message}"))
            }
            catch (error:Exception){
                Log.e("TAG", "CONNECT ${error.message}")

                emit( RequestResult.Error("${error.message}"))

            }
            finally {
                emit(RequestResult.Loading(false))
            }
        }
    }

    override fun deletePlaylist(playlistId: String): Flow<RequestResult<Unit>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                val request =  client.delete("${CreatorApi.deletePlaylist}?playlistId=$playlistId")
                if (request.status == HttpStatusCode.OK){
                   emit( RequestResult.Success(Unit))
                } else{
                    emit( getServerErrorResult(request = request))
                }
            }
            catch (error: ConnectException){
                Log.e("TAG", "CONNECT EXCEPTION")
                emit( RequestResult.Error("${error.message}"))
            }
            catch (error:Exception){
                Log.e("TAG", "CONNECT ${error.message}")

                emit( RequestResult.Error("${error.message}"))

            }
            finally {
                emit(RequestResult.Loading(false))
            }
        }
    }

    override fun addToPlaylist(songId: String): Flow<RequestResult<Unit>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                val request =  client.delete("${CreatorApi.editSong}?songId=$songId")
                if (request.status == HttpStatusCode.OK){
                    emit( RequestResult.Success(Unit))
                } else{
                   emit( getServerErrorResult(request = request))
                }
            }
            catch (error: ConnectException){
                Log.e("TAG", "CONNECT EXCEPTION")
                emit( RequestResult.Error("${error.message}"))
            }
            catch (error:Exception){
                Log.e("TAG", "CONNECT ${error.message}")

                emit( RequestResult.Error("${error.message}"))

            }
            finally {
                emit(RequestResult.Loading(false))
            }
        }
        }

}