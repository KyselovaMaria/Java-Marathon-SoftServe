package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/create-task")
public class CreateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/create-task.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String title = request.getParameter("title");
        Priority priority = Priority.valueOf(request.getParameter("priority"));

        Task task = new Task(title, priority);
        boolean isCreated = taskRepository.create(task);

        if (isCreated) {
            response.sendRedirect(request.getContextPath() + "/tasks-list");
        } else {
            request.setAttribute("message", "Task with a given name already exists!");
            request.setAttribute("task", task);
            request.getRequestDispatcher("/WEB-INF/pages/create-task.jsp").forward(request, response);
        }

    }
}