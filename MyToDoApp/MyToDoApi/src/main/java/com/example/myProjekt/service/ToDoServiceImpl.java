package com.example.myProjekt.service;

import com.example.myProjekt.model.ToDo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo createToDo(ToDo todo) {return todoRepository.save(todo);}

    @Override
    public List<ToDo> readAllToDos() {
        return todoRepository.findAll();
    }


    @Override
    public Optional<ToDo> readToDo(String id) {
        return todoRepository.findById(id);
    }

    @Override
    public Optional<ToDo> updateToDo(String id, ToDo updatedTodo) {
        return todoRepository.findById(id).map(existing -> {
            existing.setText(updatedTodo.getText());
            existing.setCompleted(updatedTodo.isCompleted());
            return todoRepository.save(existing);
        });
    }

    @Override
    public boolean deleteToDo(String id) {
        if (!todoRepository.existsById(id)) {
            return false;
        }
        todoRepository.deleteById(id);
        return true;
    }
}
