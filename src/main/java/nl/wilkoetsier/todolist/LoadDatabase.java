package nl.wilkoetsier.todolist;

import nl.wilkoetsier.todolist.model.TodoItem;
import nl.wilkoetsier.todolist.model.TodoItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TodoItemRepository repository) {
        return args -> {
            LOG.info("Preloading {}", repository.save(new TodoItem("Koffie zetten", true)));
            LOG.info("Preloading {}", repository.save(new TodoItem("Koffie opdrinken")));
        };
    }
}
