package com.crezent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crezent.database.dao.ArtistPlaylistDao
import com.crezent.database.dao.SongDao
import com.crezent.database.dao.UserDao
import com.crezent.database.dao.RecentDao
import com.crezent.database.dao.PlaylistDao
import com.crezent.database.model.ArtistPlaylistEntity
import com.crezent.database.model.PlaylistEntity
import com.crezent.database.model.RecentEntity
import com.crezent.database.model.SongEntity
import com.crezent.database.model.UserEntity

@Database(
    entities = [ RecentEntity::class, SongEntity::class, PlaylistEntity::class, UserEntity::class,ArtistPlaylistEntity::class],
    version = 2,
    exportSchema = false
)
abstract  class ExMusicDatabase():RoomDatabase() {
    abstract val recentDao: RecentDao
    abstract val songDao: SongDao
    abstract val playlistDao: PlaylistDao
    abstract val userDao : UserDao
    abstract val artistPlaylistDao: ArtistPlaylistDao
}
