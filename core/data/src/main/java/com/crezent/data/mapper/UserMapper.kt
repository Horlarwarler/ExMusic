package com.crezent.data.mapper

import com.crezent.database.model.UserEntity
import com.crezent.models.User
import com.crezent.network.models.UserDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun User.toDto(): UserDto {

    return UserDto(
        username = username,
        displayName = displayName,
        password = password,
        emailAddress = emailAddress,
        profilePicture = profilePicture,
        isArtist = isArtist,

        )
}

fun UserDto.toEntity():UserEntity{
    val isArtist = if (isArtist)1 else 0

    return  UserEntity(
        username = username,
        displayName = displayName!!,
        password = password,
        emailAddress = emailAddress!!,
        profilePicture = profilePicture,
        isArtist = isArtist,
        registeredDate = registeredDate!!,
        favSongs = Json.encodeToString<List<String>>(favSongs),
        followers = Json.encodeToString<List<String>>(followers),
        following = Json.encodeToString<List<String>>(following)
        )
}

fun User.toEntity(): UserEntity {
    val isArtist = if (isArtist)1 else 0

    return  UserEntity(
        username = username,
        displayName = displayName,
        password = password,
        emailAddress = emailAddress,
        profilePicture = profilePicture,
        isArtist = isArtist,
        registeredDate = registeredDate,
        favSongs = Json.encodeToString<List<String>>(favSongs),
        followers = Json.encodeToString<List<String>>(followers),
        following = Json.encodeToString<List<String>>(following)
    )
}






