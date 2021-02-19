package nl.wilkoetsier.todolist.controller;

import java.util.UUID;

public class TodoItemNotFoundException extends RuntimeException {

    TodoItemNotFoundException(UUID id) {
        super("Could not find todoItem " + id);
    }
}
