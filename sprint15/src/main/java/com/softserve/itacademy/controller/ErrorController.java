package com.softserve.itacademy.controller;

import com.softserve.itacademy.exceptions.EntityNotFoundException;
import com.softserve.itacademy.exceptions.NullEntityReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler({NullEntityReferenceException.class, EntityNotFoundException.class})
    public ModelAndView nullEntityReference(Exception exception){
        return handle(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView notFoundExceptions(NoHandlerFoundException exception){
        return handle(exception, HttpStatus.NOT_FOUND);
    }

    private ModelAndView handle(Exception exception, HttpStatus status){
        ModelAndView modelAndView = new ModelAndView("error-" + status.value());
        modelAndView.setStatus(status);
        modelAndView.addObject("code", status.value());
        modelAndView.addObject("error", exception.getMessage());
        return modelAndView;
    }

}
