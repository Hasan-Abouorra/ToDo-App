package com.example.myapplication.api

import com.example.myapplication.model.ToDo
import retrofit2.Response
import retrofit2.http.*

interface ToDoApiService {

    @GET("api/todos")
    suspend fun getAllToDos(): Response<List<ToDo>>

    @GET("api/todos/{id}")
    suspend fun getToDoById(@Path("id") id: String): Response<ToDo>

    @POST("api/todos")
    suspend fun createToDo(@Body todo: ToDo): Response<ToDo>

    @PUT("api/todos/{id}")
    suspend fun updateToDo(
        @Path("id") id: String,
        @Body todo: ToDo
    ): Response<ToDo>

    @DELETE("api/todos/{id}")
    suspend fun deleteToDo(@Path("id") id: String): Response<Void>
}