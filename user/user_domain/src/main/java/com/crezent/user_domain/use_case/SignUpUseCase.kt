package com.crezent.user_domain.use_case

import com.crezent.common.preference.Preference
import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpUseCase(
    private val exMusicRepo: ExMusicRepo,
    private val baseApi: BaseApi,
    private val preference: Preference
) {
    operator fun invoke(
        username: String,
        password: String,
        displayName:String,
        emailAddress:String,
        profilePicture: ByteArray?,
        asArtist:String = "false"
    ): Flow<RequestResult<Unit>>{
        return flow {
            val result = baseApi.signUp(
                username =  username,
                password= password,
                displayName =displayName,
                emailAddress = emailAddress,
                profilePicture= profilePicture,
                asArtist = asArtist
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
                    val signIn = baseApi.signIn(username, password)
                    when(signIn) {
                        is RequestResult.Loading -> {
                            emit(RequestResult.Loading())
                        }
                        is RequestResult.Error -> {
                            emit(RequestResult.Error(signIn.errorMessage))

                        }
                        is RequestResult.Success ->{
                            val token = signIn.resource
                            preference.saveApiKey(token)
                            emit(RequestResult.Success(Unit))
                        }
                    }

                }
            }

        }


    }
}