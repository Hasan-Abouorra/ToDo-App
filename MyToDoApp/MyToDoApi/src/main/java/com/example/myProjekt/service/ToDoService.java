package com.example.myProjekt.service;

import com.example.myProjekt.model.ToDo;

import java.util.List;
import java.util.Optional;

public interface ToDoService {

    /**
     * Creates a new todo
     * @param todo - ToDo to be created
     */
    ToDo createToDo(ToDo todo);

    /**
     * Returns a list of all existing todos
     * @return List of todos
     */
    List<ToDo> readAllToDos();

    /**
     * Returns a todo based on its ID
     * @param id - ToDo ID
     * @return - ToDo object with the given ID
     */
     Optional<ToDo> readToDo(String id);

    /**
     * Updates the todo with the given ID
     * @param todo - ToDo to be updated
     * @param id - ID of the todo you want to update
     * @return -  true if the data has benn updated, othérwise false
     */
    Optional<ToDo> updateToDo(String id, ToDo todo);

    /**
     * Deletes the todo with the given ID
     * @param id - ID of the todo to be deleted
     * @return - true if the todo was deleted, otherwise false
     */
    boolean deleteToDo(String id);


}
