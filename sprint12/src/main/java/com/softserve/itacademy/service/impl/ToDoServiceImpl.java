package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Override
    public ToDo create(ToDo todo) {
        if (todo != null){
            return toDoRepository.save(todo);
        }
        throw new EntityNotFoundException("Todo cannot be null");
    }

    @Override
    public ToDo readById(long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Todo with this id %s not found", id)));
    }

    @Override
    public ToDo update(ToDo todo) {
        return toDoRepository.saveAndFlush(todo);

    }

    @Override
    public void delete(long id) {
        toDoRepository.delete(readById(id));

    }

    @Override
    public List<ToDo> getAll() {
        return toDoRepository.findAll();
    }


    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = toDoRepository.findByOwnerId(userId);

        if (todos.isEmpty()) {
            throw new EntityNotFoundException(String.format("No ToDos found for User id %s", userId));
        }

        return todos;
    }

}

