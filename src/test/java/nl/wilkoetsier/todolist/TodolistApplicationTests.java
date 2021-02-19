package nl.wilkoetsier.todolist;

import nl.wilkoetsier.todolist.controller.TodoItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TodolistApplicationTests {

    @Autowired
    private TodoItemController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
