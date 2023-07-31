package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-task")
public class DeleteTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init()  {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int taskId = Integer.parseInt(request.getParameter("id"));
        if (taskRepository.delete(taskId)) {
            response.sendRedirect("/tasks-list");
        } else {
            request.setAttribute("url", "/delete-task");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/pages/error.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
