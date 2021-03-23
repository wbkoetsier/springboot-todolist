package nl.wilkoetsier.todolist.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;
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
    private Status status;

    public TodoItem() {
    }

    public TodoItem(String todoValue) {
        this(todoValue, Status.TODO);
    }

    public TodoItem(String todoValue, Status status) {
        this.todoValue = todoValue;
        this.dateTimeCreated = NOW;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TodoItem.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dateTimeCreated=" + dateTimeCreated)
                .add("todoValue='" + todoValue + "'")
                .add("dateTimeUpdated=" + dateTimeUpdated)
                .add("status=" + status)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return getDateTimeCreated() == todoItem.getDateTimeCreated() &&
               getDateTimeUpdated() == todoItem.getDateTimeUpdated() && getId().equals(todoItem.getId()) &&
               Objects.equals(getTodoValue(), todoItem.getTodoValue()) && getStatus() == todoItem.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateTimeCreated(), getTodoValue(), getDateTimeUpdated(), getStatus());
    }
}
