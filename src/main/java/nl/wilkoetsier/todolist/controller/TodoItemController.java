package nl.wilkoetsier.todolist.controller;

import nl.wilkoetsier.todolist.model.Status;
import nl.wilkoetsier.todolist.model.TodoItem;
import nl.wilkoetsier.todolist.model.TodoItemModelAssembler;
import nl.wilkoetsier.todolist.model.TodoItemRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final TodoItemModelAssembler assembler;

    public TodoItemController(TodoItemRepository repository, TodoItemModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/todoitems")
    public CollectionModel<EntityModel<TodoItem>> all() {
        List<EntityModel<TodoItem>> todoItems = repository.findAll().stream()
                                                          .map(assembler::toModel)
                                                          .collect(Collectors.toList());
        return CollectionModel.of(todoItems,
                linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/todoitems")
    ResponseEntity<?> newTodoItem(@RequestBody TodoItem newTodoItem) {
        EntityModel<TodoItem> entityModel = assembler.toModel(repository.save(newTodoItem));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Single item

    @GetMapping("/todoitems/{id}")
    public EntityModel<TodoItem> one(@PathVariable UUID id) {
        TodoItem todoItem = repository.findById(id)
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));
        return assembler.toModel(todoItem);
    }

    @PutMapping("/todoitems/{id}")
    ResponseEntity<?> replaceTodoItem(@RequestBody TodoItem newTodoItem, @PathVariable UUID id) {

        TodoItem updatedTodoItem = repository.findById(id)
                                             .map(todoItem -> {
                                                 todoItem.setTodoValue(newTodoItem.getTodoValue());
                                                 todoItem.setStatus(newTodoItem.getStatus());
                                                 todoItem.setDateTimeUpdated(Instant.now().toEpochMilli());
                                                 return repository.save(todoItem);
                                             })
                                             .orElseGet(() -> {
                                                 newTodoItem.setId(id);
                                                 return repository.save(newTodoItem);
                                             });
        EntityModel<TodoItem> entityModel = assembler.toModel(updatedTodoItem);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/todoitems/{id}/todo")
    public ResponseEntity<?> todo(@PathVariable UUID id) {

        TodoItem todoItem = repository.findById(id)
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));

        if (todoItem.getStatus() == Status.IN_PROGRESS) {
            todoItem.setStatus(Status.TODO);
            return ResponseEntity.ok(assembler.toModel(repository.save(todoItem)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                             .withTitle("Method not allowed")
                             .withDetail(
                                     "Unable to change status from " + todoItem.getStatus() + " to " + Status.TODO));
    }

    @PutMapping("/todoitems/{id}/inprogress")
    public ResponseEntity<?> inProgress(@PathVariable UUID id) {

        TodoItem todoItem = repository.findById(id)
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));

        if (todoItem.getStatus() == Status.TODO) {
            todoItem.setStatus(Status.IN_PROGRESS);
            return ResponseEntity.ok(assembler.toModel(repository.save(todoItem)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                             .withTitle("Method not allowed")
                             .withDetail("Unable to change status from " + todoItem.getStatus() + " to " +
                                         Status.IN_PROGRESS));
    }

    @PutMapping("/todoitems/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable UUID id) {

        TodoItem todoItem = repository.findById(id)
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));

        if (todoItem.getStatus() == Status.TODO || todoItem.getStatus() == Status.IN_PROGRESS) {
            todoItem.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repository.save(todoItem)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                             .withTitle("Method not allowed")
                             .withDetail("Unable to change status from " + todoItem.getStatus() + " to " +
                                         Status.COMPLETED));
    }

    @DeleteMapping("/todoitems/{id}")
    ResponseEntity<?> deleteTodoItem(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
