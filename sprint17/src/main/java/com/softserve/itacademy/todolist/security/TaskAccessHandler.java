package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.ToDoRepository;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component("TaskAccess")
@Slf4j
@AllArgsConstructor
public class TaskAccessHandler {
    private final ToDoRepository toDoRepository;

    private final ToDoService toDoService;
    private final TaskService taskService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public boolean canReadTask(long taskId) {
        User user = getAuthenticatedUser();
        Task task = taskService.readById(taskId);
        ToDo todo = task.getTodo();
        return user.getEmail().equals(todo.getOwner().getEmail()) || todo.getCollaborators()
                .stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
    }

    public boolean canDeleteTask(long taskId) {
        User user = getAuthenticatedUser();
        Task task = taskService.readById(taskId);

        switch (user.getRole().getName()) {
            case "ADMIN" -> {
                return true;
            }
            case "USER" -> {
                if (Objects.equals(user.getUsername(), task.getTodo().getOwner().getEmail())){
                    return true;
                }
                throw new AccessDeniedException("User with id " + user.getId() + " tried to do FORBIDDEN request - " + getCurrentHttpRequest().getRequestURL().toString());
            }default -> {
                return false;
            }
        }
    }

    public boolean canUpdateTask(long taskId) {
        User user = getAuthenticatedUser();
        ToDo toDo = taskService.readById(taskId).getTodo();
        return canManipulateTask(user, toDo);
    }

    public boolean canCreateOrGetAllTasks(long todoId){
        User user = getAuthenticatedUser();
        ToDo toDo = toDoService.readById(todoId);
        return canManipulateTask(user, toDo);
    }

    private boolean canManipulateTask(User user, ToDo toDo) {
        switch (user.getRole().getName()) {
            case "ADMIN"->{
                return true;
            }
            case "USER"->{
                if(user.getEmail().equals(toDo.getOwner().getEmail()) || toDo.getCollaborators()
                        .stream()
                        .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                    return true;
                }
                throw new AccessDeniedException("User with id " + user.getId() + " tried to do FORBIDDEN request!");
            }
            default -> {
                return false;
            }
        }
    }

    public static HttpServletRequest getCurrentHttpRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        log.debug("Not called in the context of an HTTP request");
        return null;
    }
}
