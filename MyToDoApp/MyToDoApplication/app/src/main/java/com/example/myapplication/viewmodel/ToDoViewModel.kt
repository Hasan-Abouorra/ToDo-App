package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ToDo
import com.example.myapplication.repository.ToDoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ToDoUiState(
    val todos: List<ToDo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUsingFakeData: Boolean = false
)

class ToDoViewModel : ViewModel() {

    private val repository = ToDoRepository()

    private val _uiState = MutableStateFlow(ToDoUiState())
    val uiState: StateFlow<ToDoUiState> = _uiState.asStateFlow()

    init {
        loadToDos()
    }

    fun loadToDos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.getAllToDos().fold(
                onSuccess = { todos ->
                    _uiState.value = ToDoUiState(
                        todos = todos,
                        isLoading = false,
                        isUsingFakeData = repository.isUsingFakeData()
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun addToDo(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val newToDo = ToDo(text = text, completed = false)
            repository.createToDo(newToDo).fold(
                onSuccess = { createdTodo ->
                    //  Füge das neue Todo direkt zur Liste hinzu
                    val currentTodos = _uiState.value.todos
                    _uiState.value = _uiState.value.copy(
                        todos = currentTodos + createdTodo
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }
            )
        }
    }

    fun toggleToDo(todo: ToDo) {
        //Aktualisiere sofort in der UI
        val currentTodos = _uiState.value.todos
        val updatedTodos = currentTodos.map {
            if (it.stableId == todo.stableId) {
                it.copy(completed = !it.completed)
            } else {
                it
            }
        }
        _uiState.value = _uiState.value.copy(todos = updatedTodos)

        // Dann API Call im Hintergrund
        viewModelScope.launch {
            val updatedToDo = todo.copy(completed = !todo.completed)
            repository.updateToDo(todo.id ?: "", updatedToDo).fold(
                onSuccess = {
                    //UI ist aktualisiert
                },
                onFailure = { error ->
                    // Bei Fehler: Rückgängig machen
                    _uiState.value = _uiState.value.copy(
                        todos = currentTodos,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun deleteToDo(todo: ToDo) {
        // Entferne sofort aus der UI
        val currentTodos = _uiState.value.todos
        val updatedTodos = currentTodos.filter { it.stableId != todo.stableId }
        _uiState.value = _uiState.value.copy(todos = updatedTodos)


        viewModelScope.launch {
            repository.deleteToDo(todo.id ?: "").fold(
                onSuccess = {
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        todos = currentTodos,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}