package nl.wilkoetsier.todolist.controller;

import java.util.UUID;

public class PersonNotFoundException extends RuntimeException {

    PersonNotFoundException(UUID id) {
        super("Could not find person " + id);
    }
}
