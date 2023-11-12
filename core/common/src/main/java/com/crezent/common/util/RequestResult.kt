package com.crezent.common.util



sealed class RequestResult< out T> (val data:T? = null, val message:String? = null ) {
    data  class  Success <out T>(val resource: T) : RequestResult<T>(data = resource)
     data class Loading<T>(  val isLoading:Boolean = true , val progress:Int? = null): RequestResult<T>(data = null, message = progress?.toString())
    data class  Error<T> ( val errorMessage:String): RequestResult<T>(message = errorMessage, data = null)
}
