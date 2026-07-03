package com.example.myProjekt.service;

import com.example.myProjekt.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ToDoRepository extends MongoRepository<ToDo,String> {
    //Optional<ToDo> findById(int id);
    // Customer findById(int id);

}


