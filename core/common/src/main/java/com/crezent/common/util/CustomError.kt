package com.crezent.common.util


class CustomError (
    errorMessage:String,
    errorCause:Throwable? = null
) :Exception(
)