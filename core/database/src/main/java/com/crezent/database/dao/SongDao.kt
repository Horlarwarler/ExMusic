package com.crezent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crezent.database.model.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("""
        SELECT * FROM  song_table
        WHERE LOWER(title) LIKE '%' || LOWER(:searchQuery) || '%'
        OR LOWER(description) LIKE '%'|| LOWER(:searchQuery)|| '%'
        OR LOWER(artistUsername) LIKE '%'|| LOWER(:searchQuery)|| '%'
        AND :artistUsername IS NULL OR artistUsername = :artistUsername
        ORDER by id ASC
        """)
    fun  getAllSong(searchQuery:String, artistUsername:String? =null) : Flow<List<SongEntity>>

    @Query("SELECT * FROM song_table WHERE songId = :songId")
    suspend fun getSongById(songId:String) : SongEntity

    @Query("DELETE  FROM song_table WHERE songId = :songId")
    suspend fun deleteFromDownload(songId:String)

    @Query("DELETE  FROM song_table WHERE isDownloaded = 0")
    suspend fun deleteTempMusic()
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTempSongs(songs:List<SongEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDownload(songEntity: SongEntity)
}