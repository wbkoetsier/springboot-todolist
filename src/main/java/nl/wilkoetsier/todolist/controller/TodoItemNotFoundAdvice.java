package nl.wilkoetsier.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TodoItemNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TodoItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String todoItemNotFoundHandler(TodoItemNotFoundException e) {
        return String.format("%s: %s", HttpStatus.NOT_FOUND.toString(), e.getMessage());
    }
}
