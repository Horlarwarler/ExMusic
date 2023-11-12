package com.crezent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.crezent.database.model.PlaylistEntity
@Dao
interface PlaylistDao {
    @Query("SELECT * FROM  playlist ")
    suspend fun  getAllPlaylist() : List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE songId = :songId")
    suspend fun getPlaylistById(songId:String) : PlaylistEntity?

    @Query("DELETE  FROM playlist WHERE songId = :songId")
    suspend fun deleteFromPersonalPlaylist(songId:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlaylist(playlistEntity: PlaylistEntity)
    @Update()
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
}