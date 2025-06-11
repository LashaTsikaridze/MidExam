package com.example.booklibrary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



interface ReqRes {
    @GET("MID")
    fun getBooks(@Query("page") page: Int): Call<ReqesObj<List<Books>>>
}