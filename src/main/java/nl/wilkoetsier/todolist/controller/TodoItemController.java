package nl.wilkoetsier.todolist.controller;

import nl.wilkoetsier.todolist.model.TodoItem;
import nl.wilkoetsier.todolist.model.TodoItemRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class TodoItemController {

    private final TodoItemRepository repository;

    public TodoItemController(TodoItemRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/todoitems")
    List<TodoItem> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/todoitems")
    TodoItem newTodoItem(@RequestBody TodoItem newTodoItem) {
        return repository.save(newTodoItem);
    }

    // Single item

    @GetMapping("/todoitems/{id}")
    TodoItem one(@PathVariable UUID id) {

        return repository.findById(id)
                         .orElseThrow(() -> new TodoItemNotFoundException(id));
    }

    @PutMapping("/todoitems/{id}")
    TodoItem replaceTodoItem(@RequestBody TodoItem newTodoItem, @PathVariable UUID id) {

        return repository.findById(id)
                         .map(todoItem -> {
                             todoItem.setTodoValue(newTodoItem.getTodoValue());
                             todoItem.setMarkedAsDone(newTodoItem.isMarkedAsDone());
                             todoItem.setDateTimeUpdated(Instant.now().toEpochMilli());
                             return repository.save(todoItem);
                         })
                         .orElseGet(() -> {
                             newTodoItem.setId(id);
                             return repository.save(newTodoItem);
                         });
    }

    @DeleteMapping("/todoitems/{id}")
    void deleteEmployee(@PathVariable UUID id) {
        repository.deleteById(id);
    }
}
