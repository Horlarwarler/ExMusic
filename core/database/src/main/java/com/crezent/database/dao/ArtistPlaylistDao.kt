package com.crezent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.crezent.database.model.ArtistPlaylistEntity
import com.crezent.database.model.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistPlaylistDao {

    @Query("SELECT * FROM  artist_playlist WHERE artistUsername == :artistUsername OR :artistUsername IS NULL ")
    suspend fun  getAllPlaylist(artistUsername:String?) : List<ArtistPlaylistEntity>

    @Query("SELECT * FROM artist_playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId:String) : ArtistPlaylistEntity?

    @Query("DELETE  FROM artist_playlist  WHERE artistUsername == :artistUsername OR :artistUsername IS NULL ")
    suspend fun clearAllArtistPlaylist(artistUsername: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtistPlaylist(artistPlaylistEntities: List<ArtistPlaylistEntity>)

}