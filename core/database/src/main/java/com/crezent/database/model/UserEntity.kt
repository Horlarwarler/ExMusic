package com.crezent.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crezent.models.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Entity(tableName = "USER_TABLE")
data class UserEntity(
    @PrimaryKey
    val username:String,
    val displayName:String,
    val emailAddress:String,
    val password:String?,
    val profilePicture:String? = null,
    val followers: String,
    val following: String,
    val favSongs: String,
    val registeredDate:String,
    val isArtist:Int = 0
)

fun UserEntity.toUser(): User {
    val isArtist = isArtist == 1


    return User(
        username = username,
        displayName = displayName,
        password = password,
        emailAddress = emailAddress,
        profilePicture = profilePicture,
        isArtist = isArtist,
        registeredDate = registeredDate,
        favSongs = Json.decodeFromString(favSongs),
        followers = Json.decodeFromString(followers),
        following = Json.decodeFromString(following)
    )
}

