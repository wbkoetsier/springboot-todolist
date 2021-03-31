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
        EntityModel<TodoItem> todoItemModel = EntityModel.of(todoItem,
                linkTo(methodOn(TodoItemController.class).one(todoItem.getId())).withSelfRel(),
                linkTo(methodOn(TodoItemController.class).all()).withRel("todoItems"));

        if (todoItem.getStatus() == Status.TODO) {
            todoItemModel.add(
                    linkTo(methodOn(TodoItemController.class)
                            .inProgress(todoItem.getId()))
                            .withRel("inProgress"));
            todoItemModel.add(
                    linkTo(methodOn(TodoItemController.class)
                            .complete(todoItem.getId()))
                            .withRel("complete"));
        } else if (todoItem.getStatus() == Status.IN_PROGRESS) {
            todoItemModel.add(
                    linkTo(methodOn(TodoItemController.class)
                            .todo(todoItem.getId()))
                            .withRel("todo"));
            todoItemModel.add(
                    linkTo(methodOn(TodoItemController.class)
                            .complete(todoItem.getId()))
                            .withRel("complete"));
        }
        return todoItemModel;
    }

}
