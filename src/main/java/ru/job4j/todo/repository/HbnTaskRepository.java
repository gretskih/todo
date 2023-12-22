package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> add(Task task) {
        return crudRepository.optional(session -> {
            session.save(task);
            return task;
        });
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.booleanCall(
                "DELETE Task WHERE id = :fId",
                Map.of("fId", id)
        );
    }

    @Override
    public boolean replace(int id, Task task) {
        return crudRepository.booleanCall(
                "UPDATE Task SET title = :fTitle"
                        + ", description = :fDescription"
                        + ", done = :fDone "
                        + ", priority = :fPriority"
                        + " WHERE id = :fId",
                Map.of("fTitle", task.getTitle(),
                        "fDescription", task.getDescription(),
                        "fDone", task.isDone(),
                        "fPriority", task.getPriority(),
                        "fId", id)
        );
    }

    @Override
    public boolean setDoneTrue(int id) {
        return crudRepository.booleanCall(
                "UPDATE Task SET done = :fDone WHERE id = :fId",
                Map.of("fDone", true,
                        "fId", id)
        );
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task task JOIN FETCH task.priority where task.id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query(
                "from Task task JOIN FETCH task.priority ORDER BY task.title", Task.class
        );
    }

    @Override
    public Collection<Task> findAllDone() {
        return crudRepository.query(
                "from Task task JOIN FETCH task.priority where done = true ORDER BY task.title",
                Task.class
        );
    }

    @Override
    public Collection<Task> findAllNew() {
        return crudRepository.query(
                "from Task task JOIN FETCH task.priority where done = false ORDER BY task.title",
                Task.class
        );
    }

}
