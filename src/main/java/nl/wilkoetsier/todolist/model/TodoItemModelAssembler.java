package nl.wilkoetsier.todolist.model;

import nl.wilkoetsier.todolist.controller.TodoItemController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TodoItemModelAssembler implements RepresentationModelAssembler<TodoItem, EntityModel<TodoItem>> {

    @Override
    public EntityModel<TodoItem> toModel(TodoItem todoItem) {
        return EntityModel.of(todoItem,
                linkTo(methodOn(TodoItemController.class).one(todoItem.getId())).withSelfRel(),
                linkTo(methodOn(TodoItemController.class).all()).withRel("todoItems"));
    }

}
