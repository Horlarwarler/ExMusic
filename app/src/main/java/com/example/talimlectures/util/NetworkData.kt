package com.example.talimlectures.util

//sealed class  RequestState <out T:Any>{
//    object idle : RequestState<Nothing>()
//    object loading : RequestState<Nothing>()
//    data class success(val data: NetworkLectures) : RequestState<NetworkLectures>()
//    data class error(val error: Exception) : RequestState<Nothing>()
//}

enum class RequestState{
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
