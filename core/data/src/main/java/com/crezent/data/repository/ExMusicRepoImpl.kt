package com.crezent.data.repository


import com.crezent.common.preference.Preference
import com.crezent.common.util.RequestResult
import com.crezent.data.mapper.toEntity
import com.crezent.data.mapper.toSong
import com.crezent.database.dao.ArtistPlaylistDao
import com.crezent.database.dao.PlaylistDao
import com.crezent.database.dao.RecentDao
import com.crezent.database.dao.SongDao
import com.crezent.database.dao.UserDao
import com.crezent.database.model.mapToModel
import com.crezent.database.model.toArtistPlaylist
import com.crezent.database.model.toPersonalPlaylist
import com.crezent.database.model.toSong
import com.crezent.database.model.toUser
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import java.net.ConnectException

class ExMusicRepoImpl (
    private val userDao: UserDao,
    private val songDao: SongDao,
    private val baseApi: BaseApi,
    private val preference: Preference,
    private val playlistDao: PlaylistDao,
    private val artistPlaylistDao: ArtistPlaylistDao,
    private val recentDao: RecentDao
) : ExMusicRepo {
   // private val songDao = database.songDao

    override suspend fun insertUser(user: com.crezent.models.User) {
       userDao.insertUser(user.toEntity())
    }

    override suspend fun getPersonalPlaylistById(songId: String): com.crezent.models.PersonalPlaylist? {
        return playlistDao.getPlaylistById(songId)?.toPersonalPlaylist()
    }

    override suspend fun addToPersonalPlaylist(personalPlaylist: com.crezent.models.PersonalPlaylist) : RequestResult<Unit>{
       return try {
           playlistDao.addToPlaylist(personalPlaylist.toEntity())
           RequestResult.Success(Unit)

        }
        catch (error:Exception){
            val errorMessage = error.message?:"Unknown"
            RequestResult.Error(errorMessage)
        }
    }

    override suspend fun removeFromPersonalPlaylist(songId: String): RequestResult<Unit> {
        return try {
            playlistDao.deleteFromPersonalPlaylist(songId)
            RequestResult.Success(Unit)

        }
        catch (error: Exception) {
            val errorMessage = error.message ?: "Unknown"
            RequestResult.Error(errorMessage)
        }

    }

    override suspend fun updatePersonalPlaylist(personalPlaylist: com.crezent.models.PersonalPlaylist) :RequestResult<Unit> {
        return try {
            playlistDao.updatePlaylist(personalPlaylist.toEntity())
            RequestResult.Success(Unit)

        }
        catch (error: Exception) {
            val errorMessage = error.message ?: "Unknown"
            RequestResult.Error(errorMessage)
        }
    }

    override fun getArtistPlaylist(artistUsername: String?): Flow<RequestResult<List<com.crezent.models.ArtistPlaylist>>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                try {
                    val artistsPlaylistDto = baseApi.getArtistPlaylist(artistUsername)
                    if (artistsPlaylistDto is RequestResult.Success){
                        val artistsPlaylistEntity = artistsPlaylistDto.resource.map {
                            it.toEntity()
                        }
                        artistPlaylistDao.clearAllArtistPlaylist(artistUsername)
                        artistPlaylistDao.insertArtistPlaylist(artistsPlaylistEntity)
                    }

                }
                catch (error:ConnectException){
                    emit(RequestResult.Error("Network Error"))
                }
                finally {
                    val artistsPlaylist = artistPlaylistDao.getAllPlaylist(artistUsername).map {
                        it.toArtistPlaylist()
                    }
                    emit(RequestResult.Success(artistsPlaylist))
                }
            }
            catch (error:Exception){
                val errorMessage = error.message?:"Unknown Error"
                emit(RequestResult.Error(errorMessage))
            }
            finally {
                emit(RequestResult.Loading(false))
            }
        }
    }

    override fun getArtists(): Flow<RequestResult<List<com.crezent.models.User>>> {
        return callbackFlow {
            send(RequestResult.Loading())
            try {
                try {
                    baseApi.artists.onEach {
                        artists ->
                        val artistsEntity = artists.map {
                            it.toEntity()
                        }
                        userDao.insertTempArtist(artistsEntity)
                    }
                }
                catch (error:ConnectException){
                    val errorMessage = error.message?:"Can't connect To server"
                    //get the cache one here
                    send(RequestResult.Error(errorMessage))
                }
                finally {
                    userDao.getCachedArtists()
                        .onEach {
                            val artists = it.map {
                                userEntity->
                                userEntity.toUser()
                            }
                            send(RequestResult.Success(artists))
                        }
                }
            }
            catch (error:IOException){
                val errorMessage = error.message?:"Failed To load from cached"
                //get the cache one here
                send(RequestResult.Error(errorMessage))
            }
            catch (error:Exception){
                val errorMessage = error.message?:"Failed To load artist"
                //get the cache one here
                send(RequestResult.Error(errorMessage))
            }

            finally {
                send(RequestResult.Loading(false))
            }

        }
    }

    override fun getSongs(
        searchQuery: String,
        artistUsername: String?
    ): Flow<RequestResult<List<com.crezent.models.Song>>> {
        return callbackFlow {
            send(RequestResult.Loading())
            try {
                try {
                    baseApi.songs.onEach { newSongs ->
                        val tempSongs = newSongs.map {
                            it.toEntity()
                        }
                        songDao.insertTempSongs(tempSongs)
                    }
                }
                catch (error:ConnectException){
                    val errorMessage = error.message?:"Can't connect To server"
                    //get the cache one here
                    send(RequestResult.Error(errorMessage))
                }
                finally {
                    songDao.getAllSong(searchQuery, artistUsername).onEach { songs ->
                        val toSongs = songs.map { it.toSong() }
                        send(RequestResult.Success(toSongs))
                    }
                }
            }
            catch (error:IOException){
                val errorMessage = error.message?:"Failed To load from cached"
                //get the cache one here
                send(RequestResult.Error(errorMessage))
            }
            catch (error:Exception){
                val errorMessage = error.message?:"Failed To load artist"
                //get the cache one here
                send(RequestResult.Error(errorMessage))
            }

            finally {
                send(RequestResult.Loading(false))
            }

        }

    }

    override suspend fun getSongById(songId: String): com.crezent.models.Song? {
     return try {
         try {
             return songDao.getSongById(songId).toSong()

         }
         catch (error: IOException) {
             val request = baseApi.getSongById(songId)
             if (request is RequestResult.Success) {
                 return request.resource.toSong()
             }
             null
         }

         }
         catch (error:Exception){
             null
         }
     }

    override suspend fun deleteDownloadedSong(songId: String): RequestResult<Unit> {
        return try {
            songDao.deleteFromDownload(songId)
            RequestResult.Success(Unit)
        }
        catch (error:IOException){
            RequestResult.Error("${error.message}")
        }
        catch (error:Exception){
            RequestResult.Error("${error.message}")

        }
    }

    override fun getUser(username: String?, cacheUser: Boolean): Flow<RequestResult<com.crezent.models.User>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                try {
                    val request = baseApi.getUserInfo(username = username)
                    if (request is RequestResult.Success){
                        val user = request.resource
                        val userEntity = user.toEntity()
                        userDao.insertUser(userEntity)

                    }
                    else if (request is RequestResult.Error) {
                        emit(RequestResult.Error(request.errorMessage))
                    }
                }
                catch (error:ConnectException){
                    emit(RequestResult.Error("Cant retrieve the user from server"))
                }
                finally {
                    val cachedUser = userDao.getUser(username?:preference.loggedInUsername())
                    emit(RequestResult.Success(cachedUser.toUser()))
                }

            }
            catch (error:IOException){
                emit(RequestResult.Error("Database Error"))

            }
            catch (error:Exception){
                val errorMessage = error.message?:"Unknown Error"
                emit(RequestResult.Error(errorMessage))

            }
        }
    }


    override fun getPersonalPlaylist(): Flow<RequestResult<List<com.crezent.models.PersonalPlaylist>>> {
        TODO("Not yet implemented")
    }

    //Recently Played
    override suspend fun addToRecentlyPlayed(recentlyPlayed: com.crezent.models.RecentlyPlayed) {
        try {
            recentDao.addToRecent(recentlyPlayed.toEntity())
        }
        catch (error:Exception){
            //
        }
    }

    override suspend fun removeFromRecent(songId: String) {
       try {
           recentDao.removeFromRecent(songId)
       }
       catch (error:Exception){
           //TODO
       }
    }

    override fun getAllRecentlyPlayed(): Flow<RequestResult<List<com.crezent.models.RecentlyPlayed>>> {
        return flow {
            emit(RequestResult.Loading())
            try {
                recentDao.getRecentPlayed().collectLatest {
                    val recentPlayed = it.map {
                        recent->
                        recent.mapToModel()
                    }
                    emit(RequestResult.Success(recentPlayed))
                }
            }
            catch (error:Exception){
                val errorMessage = error.message?:"Unknown"
                emit(RequestResult.Error(errorMessage))

            }
            finally {
                emit(RequestResult.Loading(false))

            }
        }
    }

}
