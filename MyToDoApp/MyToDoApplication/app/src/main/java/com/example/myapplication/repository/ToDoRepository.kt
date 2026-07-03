package com.example.myapplication.repository

import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.model.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoRepository {

    private val apiService = RetrofitClient.apiService

    // Fakedaten als Fallback falls die Verbindung nicht klappt
    private val FakeToDos = mutableListOf(
        ToDo(id = "Fake1", text = "Willkommen bei der To-Do App", completed = false),
        ToDo(id = "Fake2", text = "Erstelle deine erste Aufgabe", completed = false),
        ToDo(id = "Fake3", text = "Verbinde mit der API", completed = false)
    )

    private var useFakeData = false

    suspend fun getAllToDos(): Result<List<ToDo>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllToDos()
            if (response.isSuccessful) {
                useFakeData = false
                Result.success(response.body() ?: emptyList())
            } else {
                // Fallback auf Fakedaten
                useFakeData = true
                Result.success(FakeToDos)
            }
        } catch (e: Exception) {
            // Verbindungsfehler - nutze Fakedaten
            useFakeData = true
            Result.success(FakeToDos)
        }
    }

    suspend fun createToDo(todo: ToDo): Result<ToDo> = withContext(Dispatchers.IO) {
        if (useFakeData) {
            // Fakedaten verwenden
            val newTodo = todo.copy(id = "Fake${System.currentTimeMillis()}")
            FakeToDos.add(newTodo)
            return@withContext Result.success(newTodo)
        }

        try {
            val response = apiService.createToDo(todo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Fehler beim Erstellen: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Fallback
            val newTodo = todo.copy(id = "Fake${System.currentTimeMillis()}")
            FakeToDos.add(newTodo)
            useFakeData = true
            Result.success(newTodo)
        }
    }

    suspend fun updateToDo(id: String, todo: ToDo): Result<ToDo> = withContext(Dispatchers.IO) {
        if (useFakeData) {
            // Fakedaten aktualisieren
            val index = FakeToDos.indexOfFirst { it.id == id }
            if (index != -1) {
                FakeToDos[index] = todo.copy(id = id)
                return@withContext Result.success(FakeToDos[index])
            }
            return@withContext Result.failure(Exception("ToDo nicht gefunden"))
        }

        try {
            val response = apiService.updateToDo(id, todo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Fehler beim Aktualisieren: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteToDo(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        if (useFakeData) {
            // Aus Fakedaten löschen
            val removed = FakeToDos.removeIf { it.id == id }
            return@withContext Result.success(removed)
        }

        try {
            val response = apiService.deleteToDo(id)
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUsingFakeData(): Boolean = useFakeData
}