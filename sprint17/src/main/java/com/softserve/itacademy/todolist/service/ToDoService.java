package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.dto.todo.ToDoDtoFetchedCollaborators;
import com.softserve.itacademy.todolist.dto.todo.ToDoDto;
import com.softserve.itacademy.todolist.dto.todo.ToDoResponse;
import com.softserve.itacademy.todolist.model.ToDo;

import java.util.List;

public interface ToDoService {

    ToDoResponse create(ToDoDto toDoDto);
    ToDo readById(long id);
    ToDoResponse readDtoById(long id);
    ToDoResponse update(Long id, ToDoDto todoDto);
    ToDoResponse delete(Long id);
    List<ToDoResponse> getAll();
    List<ToDoResponse> getByUserId(long userId);
    ToDoDtoFetchedCollaborators addCollaborator(Long id, Long collaboratorId);
    ToDoDtoFetchedCollaborators deleteCollaborator(Long id, Long collaboratorId);
}
