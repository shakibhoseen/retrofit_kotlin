package com.example.rectrofitandroid.repository


sealed class Response<T>(val data: T?=null, val errorMessage: String?=null){
    class Loading<T>: Response<T>()
    class Success<T>(data: T?=null): Response<T>(data = data)
    class Error<T>(message: String): Response<T>(errorMessage = message)

}
