package com.crezent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.crezent.database.model.RecentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {
    @Query("SELECT * FROM recently_played  LIMIT 5")
     fun getRecentPlayed():Flow<List<RecentEntity>>

    //Getting selected Recent Played

    @Insert
    suspend fun  addToRecent(recent: RecentEntity)
    @Query("DELETE FROM recently_played WHERE songId = :songId")
    suspend fun  removeFromRecent(songId: String)
}