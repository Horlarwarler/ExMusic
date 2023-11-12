package com.crezent.user_presentation.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.RequestResult
import com.crezent.domain.ApplicationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor (
    private val applicationUseCases: ApplicationUseCases
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated  = _isAuthenticated.asStateFlow()

    init {
        authenticate()
    }
    private  fun  authenticate(){
        viewModelScope.launch {
            applicationUseCases.authenticationUseCase.invoke()
                .collectLatest {
                    when(it){
                        is RequestResult.Error ->{
                            _isAuthenticated.update {
                                false
                            }
                        }
                        is RequestResult.Success -> {
                            _isAuthenticated.update {
                                true
                            }
                        }
                        is RequestResult.Loading -> {

                        }
                    }
                }
        }

    }
}