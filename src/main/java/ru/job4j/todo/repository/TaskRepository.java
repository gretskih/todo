package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Task add(Task task);

    boolean deleteById(int id);

    boolean replace(int id, Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();
}
