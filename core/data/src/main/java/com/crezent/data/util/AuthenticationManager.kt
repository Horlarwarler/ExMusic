package com.crezent.data.util

import com.crezent.common.util.RequestResult
import com.crezent.common.util.UiEvent
import com.crezent.common.util.UiText
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class AuthenticationManager(
    private val baseApi: BaseApi
) {

    private val _authenticationResult :MutableStateFlow<UiEvent?> = MutableStateFlow(null)
    val isAuthenticated = _authenticationResult.asStateFlow()
    private val delay = 60 * 1000 * 5
    private var job: Job? = null

    init {
    }

    private val timer = Timer()

    private fun authenticateUser(){
        job?.cancel()
        job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.IO + job!!)
        coroutineScope.launch {
            baseApi.authenticate().collectLatest {
                authenticationRequest ->
                val authenticationResult = when(authenticationRequest){
                    is RequestResult.Loading -> {
                        null
                    }
                    is RequestResult.Error -> {
                        val message = authenticationRequest.errorMessage
                        val uiText = UiText.DynamicText(message)
                        UiEvent.ShowSnackBar(uiText)
                    }
                    is RequestResult.Success -> {
                        UiEvent.Success
                    }
                }
                _authenticationResult.update {
                    authenticationResult
                }
            }

        }
    }
    private fun startAuthenticationTask(){
        timer.scheduleAtFixedRate(0, delay.toLong()){
            authenticateUser()
        }
    }

    fun stopAuthenticationTask(){
        job?.cancel()
        timer.cancel()

    }

}