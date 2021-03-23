package nl.wilkoetsier.todolist.controller;

import nl.wilkoetsier.todolist.model.Person;
import nl.wilkoetsier.todolist.model.PersonModelAssembler;
import nl.wilkoetsier.todolist.model.PersonRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {

    private final PersonRepository repository;
    private final PersonModelAssembler assembler;

    public PersonController(PersonRepository repository, PersonModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/persons")
    public CollectionModel<EntityModel<Person>> all() {
        List<EntityModel<Person>> persons = repository.findAll().stream()
                                                      .map(assembler::toModel)
                                                      .collect(Collectors.toList());
        return CollectionModel.of(persons,
                linkTo(methodOn(PersonController.class).all()).withSelfRel());
    }

    @GetMapping("/persons/{id}")
    public EntityModel<Person> one(@PathVariable UUID id) {
        Person todoItem = repository.findById(id)
                                    .orElseThrow(() -> new PersonNotFoundException(id));
        return assembler.toModel(todoItem);
    }

    @PostMapping("/persons")
    ResponseEntity<?> newPerson(@RequestBody Person newPerson) {
        Person person = repository.save(newPerson);
        return ResponseEntity
                .created(linkTo(methodOn(PersonController.class).one(person.getId())).toUri())
                .body(assembler.toModel(person));
    }

}
