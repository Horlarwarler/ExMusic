package com.crezent.user_presentation.signIn

import com.crezent.common.util.LinkedList

data class SignInState(
    val emailAddress:String="",
    val password:String="",
    val isLoading:Boolean = false,
    val signInSuccessful:Boolean = false,
   // val errors:List<String> = emptyList(),
    val loginButtonEnabled:Boolean = false,
    val error: LinkedList.Node<String>? = null,
    )
