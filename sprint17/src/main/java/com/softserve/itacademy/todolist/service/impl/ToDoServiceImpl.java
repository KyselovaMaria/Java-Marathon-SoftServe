package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.dto.todo.ToDoDtoFetchedCollaborators;
import com.softserve.itacademy.todolist.dto.todo.ToDoDto;
import com.softserve.itacademy.todolist.dto.todo.ToDoResponse;
import com.softserve.itacademy.todolist.dto.todo.ToDoTransformer;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.ToDoRepository;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;
    private final ToDoTransformer toDoTransformer;
    private final UserService userService;

    @Override
    public ToDoResponse create(ToDoDto toDoDto) {
        if (toDoDto != null) {
            User owner = userService.loadUserByUsername(toDoDto.getOwnerEmail());
            ToDo todo = toDoTransformer.dtoToToDo(toDoDto);
            LocalDateTime createdAt = toDoDto.getCreatedAt() == null ? LocalDateTime.now() : toDoDto.getCreatedAt();
            todo.setCreatedAt(createdAt);
            todo.setOwner(owner);
            System.out.println(todo);
            ToDo persistedTodo = todoRepository.save(todo);
            return toDoTransformer.todoToResponseDto(persistedTodo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Transactional(readOnly = true)
    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public ToDoResponse readDtoById(long id) {
        return todoRepository.findToDoDtoById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    @Transactional
    @Override
    public ToDoResponse update(Long id, ToDoDto todoDto) {
        ToDo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ToDo with id " + id + " not found"));
        todo.setTitle(todoDto.getTitle());
        return toDoTransformer.todoToResponseDto(todo);
    }

    @Override
    public ToDoResponse delete(Long id) {
        ToDo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ToDo with id " + id + " not found"));
        todoRepository.delete(todo);
        return toDoTransformer.todoToResponseDto(todo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ToDoResponse> getAll() {
        return todoRepository.findAllToDoDtos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ToDoResponse> getByUserId(long userId) {
        return todoRepository.getAllToDoDtosByUserId(userId);
    }

    @Transactional
    @Override
    public ToDoDtoFetchedCollaborators addCollaborator(Long id, Long collaboratorId) {
        User newCollaborator = userService.readById(collaboratorId);
        ToDo toDo = todoRepository.findByIdFetchCollaborators(id);
        if (toDo.getCollaborators().contains(newCollaborator)) {
            throw new IllegalArgumentException("This collaborator already exists");
        }
        toDo.addCollaborator(newCollaborator);
        return toDoTransformer.todoToDtoFetchedCollaborators(toDo);
    }

    @Transactional
    @Override
    public ToDoDtoFetchedCollaborators deleteCollaborator(Long id, Long collaboratorId) {
        User collaborator = userService.readById(collaboratorId);
        ToDo toDo = todoRepository.findByIdFetchCollaborators(id);
        if (!toDo.getCollaborators().contains(collaborator)) {
            throw new IllegalArgumentException("The list of the collaborators doesn't contain the given collaborator with id " + id);
        }
        toDo.deleteCollaborator(collaborator);
        return toDoTransformer.todoToDtoFetchedCollaborators(toDo);
    }
}
