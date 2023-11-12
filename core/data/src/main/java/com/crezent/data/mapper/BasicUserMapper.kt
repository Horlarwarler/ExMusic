package com.crezent.data.mapper

import com.crezent.models.BasicUserInfo
import com.crezent.network.models.BasicUserInfoDto

fun BasicUserInfoDto.mapToUser(): BasicUserInfo {
    return BasicUserInfo(
        username = username,
        displayName = displayName,
        profilePicture = profilePicture,
        isArtist = isArtist
    )
}

fun BasicUserInfo.mapToUser(): BasicUserInfoDto {
    return BasicUserInfoDto(
        username = username,
        displayName = displayName,
        profilePicture = profilePicture,
        isArtist = isArtist
    )
}