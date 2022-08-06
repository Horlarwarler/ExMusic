package com.example.talimlectures.util

sealed class Resource<T> (val data:T? = null, val message:String? = null ) {
     class  Success <T>( data: T) :Resource<T>(data = data)
     class Loading<T>(  val isloading:Boolean = true): Resource<T>(data = null)
    class  Error<T> ( errorMessage:String):Resource<T>(message = errorMessage,)
}