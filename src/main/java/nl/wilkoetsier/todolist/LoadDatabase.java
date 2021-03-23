package nl.wilkoetsier.todolist;

import nl.wilkoetsier.todolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TodoItemRepository todoItemRepository, PersonRepository personRepository) {
        return args -> {
            LOG.info("Preloading {}", todoItemRepository.save(new TodoItem("Koffie zetten", Status.COMPLETED)));
            LOG.info("Preloading {}", todoItemRepository.save(new TodoItem("Koffie opdrinken")));
            LOG.info("Preloading {}", personRepository.save(new Person("Shiloh", "Renaissance")));
            LOG.info("Preloading {}", personRepository.save(new Person("Bobby", "Of Colourful White")));
        };
    }
}
