package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ToDoService toDoService;
    private final UserService userService;
    private final StateService stateService;


    public TaskController(ToDoService todoService, TaskService taskService, UserService userService, StateService stateService) {
        this.toDoService = todoService;
        this.taskService = taskService;
        this.userService = userService;
        this.stateService = stateService;
    }


    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") int todo_id, Model model) {
        model.addAttribute("todo_id", todo_id);
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable(value = "todo_id") long todo_id, @ModelAttribute("todo") @Valid Task task, BindingResult result) {
        if (result.hasErrors()) {
            return "create-task";
        }
        task.setState(stateService.readById(5L));
        task.setTodo(toDoService.readById(todo_id));
        toDoService.readById(todo_id).getTasks().add(task);
        taskService.create(task);
        return "redirect:/todos/" + todo_id + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable Long task_id, @PathVariable Long todo_id, Model model) {
        Task task = taskService.readById(task_id);
        TaskDto taskDto = TaskTransformer.convertToDto(task);
        model.addAttribute("todo_id", todo_id);
        model.addAttribute("taskDto", taskDto);
        List<State> states = stateService.getAll();
        model.addAttribute("states", states);
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable Long task_id, @PathVariable Long todo_id, @ModelAttribute("taskDto") TaskDto taskDto) {
        ToDo toDo = toDoService.readById(todo_id);
        State state = stateService.readById(taskDto.getStateId());
        Task task = TaskTransformer.convertToEntity(taskDto, toDo, state);
        taskService.update(task);
        return "redirect:/todos/" + todo_id + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable Long task_id, @PathVariable Long todo_id) {
        taskService.delete(task_id);
        return "redirect:/todos/" + todo_id + "/tasks";
    }
    
}
