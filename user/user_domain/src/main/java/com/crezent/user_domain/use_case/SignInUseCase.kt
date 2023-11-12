package com.crezent.user_domain.use_case

import android.util.Log
import com.crezent.common.preference.Preference
import com.crezent.common.util.RequestResult
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase (
    private val baseApi: BaseApi,
    private val preference:Preference
) {
   operator  fun invoke(
       username: String,
       password: String,
   ):Flow<RequestResult<Unit>> {
       return flow {
           val result = baseApi.signIn(username = username, password = password)
           when(result) {
               is RequestResult.Loading -> {
                   emit(RequestResult.Loading())
               }
               is RequestResult.Error -> {
                   emit(RequestResult.Error(result.errorMessage))

               }
               is RequestResult.Success ->{
                   val token = result.resource
                   Log.d("DEBUG","TOKEN SUCCESSFUL $token")
                   preference.saveApiKey(token)
                   emit(RequestResult.Success(Unit))
               }
           }

       }
    }
}