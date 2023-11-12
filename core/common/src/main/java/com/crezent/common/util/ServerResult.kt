package com.crezent.common.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend  fun  <T> getServerErrorResult(request: HttpResponse) : RequestResult<T> {
    return   when(request.status){
        HttpStatusCode.NotFound ->{
            val message = request.body<String>()
            RequestResult.Error(errorMessage = message)
        }

        HttpStatusCode.Conflict ->{
            val message = request.body<String>()
            RequestResult.Error(errorMessage = message)
        }
        HttpStatusCode.Unauthorized ->{
            RequestResult.Error(errorMessage = "UnAuthorized")
        }
        HttpStatusCode.BadRequest ->{
            val message = request.body<String>()
            RequestResult.Error(errorMessage = message)
        }
        else -> {
            val message = request.status.description
            RequestResult.Error(errorMessage = message)

        }
    }

}
