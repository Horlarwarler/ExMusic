package com.crezent.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crezent.database.model.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_table WHERE username == :username")
    suspend fun getUser(username:String):UserEntity

    @Query("SELECT * FROM user_table WHERE isArtist == 1")
     fun getCachedArtists():Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTempArtist(artists:List<UserEntity>)
}

