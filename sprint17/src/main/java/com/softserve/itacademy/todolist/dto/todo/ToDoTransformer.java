package com.softserve.itacademy.todolist.dto.todo;

import com.softserve.itacademy.todolist.model.ToDo;
import org.springframework.stereotype.Component;

@Component
public class ToDoTransformer {
    
    public ToDoDto toDoToDto(ToDo todo) {
        return new ToDoDto(
                todo.getTitle(), todo.getCreatedAt(),
                todo.getOwner().getEmail()
        );
    }

    public ToDo dtoToToDo(ToDoDto dto) {
        return new ToDo(
                dto.getTitle(), dto.getCreatedAt()
        );
    }

    public ToDoResponse todoToResponseDto(ToDo toDo) {
        return new ToDoResponse(
                toDo.getId(), toDo.getTitle(),
                toDo.getCreatedAt(), toDo.getOwner().getEmail()
        );
    }

    public ToDoDtoFetchedCollaborators todoToDtoFetchedCollaborators(ToDo toDo) {
        return new ToDoDtoFetchedCollaborators(
                toDo.getId(), toDo.getTitle(), toDo.getCreatedAt(),
                toDo.getOwner().getEmail(), toDo.getCollaborators()
        );
    }
}
