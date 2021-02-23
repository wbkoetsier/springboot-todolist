package nl.wilkoetsier.todolist.controller;

import nl.wilkoetsier.todolist.model.TodoItem;
import nl.wilkoetsier.todolist.model.TodoItemRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TodoItemController {

    private final TodoItemRepository repository;

    public TodoItemController(TodoItemRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/todoitems")
    CollectionModel<EntityModel<TodoItem>> all() {
        List<EntityModel<TodoItem>> todoItems = repository.findAll().stream()
                                                          .map(todoItem -> EntityModel.of(todoItem,
                                                                  linkTo(methodOn(TodoItemController.class)
                                                                          .one(todoItem.getId()))
                                                                          .withSelfRel(),
                                                                  linkTo(methodOn(TodoItemController.class)
                                                                          .all())
                                                                          .withRel("todoItems")))
                                                          .collect(Collectors.toList());
        return CollectionModel.of(todoItems,
                linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/todoitems")
    TodoItem newTodoItem(@RequestBody TodoItem newTodoItem) {
        return repository.save(newTodoItem);
    }

    // Single item

    @GetMapping("/todoitems/{id}")
    EntityModel<TodoItem> one(@PathVariable UUID id) {
        TodoItem todoItem = repository.findById(id)
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));
        return EntityModel.of(todoItem,
                linkTo(methodOn(TodoItemController.class).one(id)).withSelfRel(),
                linkTo(methodOn(TodoItemController.class).all()).withRel("todoItems"));
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
