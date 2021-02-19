package nl.wilkoetsier.todolist.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TodoItem {

    private static final long NOW = Instant.now().toEpochMilli();

    @Id
    @GeneratedValue
    protected UUID id;
    protected long dateTimeCreated;
    private String todoValue;
    private long dateTimeUpdated;
    private boolean markedAsDone;

    public TodoItem() {
    }

    public TodoItem(String todoValue) {
        this(todoValue, false);
    }

    public TodoItem(String todoValue, boolean markedAsDone) {
        this.todoValue = todoValue;
        this.dateTimeCreated = NOW;
        this.markedAsDone = markedAsDone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTodoValue() {
        return todoValue;
    }

    public void setTodoValue(String todoValue) {
        this.todoValue = todoValue;
    }

    public long getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(long dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public long getDateTimeUpdated() {
        return dateTimeUpdated;
    }

    public void setDateTimeUpdated(long dateTimeUpdated) {
        this.dateTimeUpdated = dateTimeUpdated;
    }

    public boolean isMarkedAsDone() {
        return markedAsDone;
    }

    public void setMarkedAsDone(boolean markedAsDone) {
        this.markedAsDone = markedAsDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return getDateTimeCreated() == todoItem.getDateTimeCreated() &&
               getDateTimeUpdated() == todoItem.getDateTimeUpdated() && isMarkedAsDone() == todoItem.isMarkedAsDone() &&
               getId().equals(todoItem.getId()) && getTodoValue().equals(todoItem.getTodoValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTodoValue(), getDateTimeCreated(), getDateTimeUpdated(), isMarkedAsDone());
    }
}
