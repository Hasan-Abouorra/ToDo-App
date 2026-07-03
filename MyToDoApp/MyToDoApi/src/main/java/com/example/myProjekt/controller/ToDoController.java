package com.example.myProjekt.controller;

import com.example.myProjekt.model.ToDo;
import com.example.myProjekt.service.ToDoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin("*")
@Tag(name = "ToDo API", description = "CRUD operations for ToDos")
public class ToDoController {

    private final ToDoService todoService;

    public ToDoController(ToDoService todoService) {
        this.todoService = todoService;
    }

    //  Swagger redirect
    //  http://localhost:8089/swagger-ui/index.html#/
    @GetMapping("/")
    public void redirectToSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }

    //  READ ALL

    @GetMapping
    public ResponseEntity<List<ToDo>> getAllToDos() {
        return ResponseEntity.ok(todoService.readAllToDos());
    }

    //  READ ONE

    @GetMapping("/{id}")
    public ResponseEntity<?> getToDoById(@PathVariable String id) {
        return todoService.readToDo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
                /*.orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("ToDo with id " + id + " not found")
                );*/
    }

    //   CREATE

    @PostMapping
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo todo) {
        ToDo created = todoService.createToDo(todo);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //   UPDATE

    @PutMapping("/{id}")
    public ResponseEntity<?> updateToDo(
            @PathVariable String id,
            @RequestBody ToDo updatedTodo
    ) {
        return todoService.updateToDo(id, updatedTodo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
                /*.orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body("ToDo with id " + id + " not found")
                );*/
    }

    //   DELETE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable String id) {
        boolean deleted = todoService.deleteToDo(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
