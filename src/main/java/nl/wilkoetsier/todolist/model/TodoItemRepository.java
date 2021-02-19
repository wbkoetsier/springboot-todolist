package nl.wilkoetsier.todolist.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoItemRepository extends JpaRepository<TodoItem, UUID> {

}
