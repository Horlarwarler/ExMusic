package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileUpdateUseCase(
    private val baseApi: BaseApi,
    private val exMusicRepo: ExMusicRepo
) {

    operator fun invoke(
        username: String?,
        password: String?,
        displayName:String?,
        emailAddress:String?,
        asArtist: Boolean? = null,
        profilePicture: ByteArray?,
        profilePictureRemove:String = "false"
    ): Flow<RequestResult<com.crezent.models.User>> {
        return flow {
            val result = baseApi.updateProfile(
                username =  username,
                password= password,
                displayName =displayName,
                emailAddress = emailAddress,
                profilePicture= profilePicture,
                asArtist = asArtist,
                profilePictureRemove = profilePictureRemove
            )
            when(result) {
                is RequestResult.Loading -> {
                    emit(RequestResult.Loading())
                }
                is RequestResult.Error -> {
                    emit(RequestResult.Error(result.errorMessage))

                }
                is RequestResult.Success ->{
                    val user = result.resource.toUser()
                    exMusicRepo.insertUser(user)
                    emit(RequestResult.Success(user))
                }
            }

        }

    }
}