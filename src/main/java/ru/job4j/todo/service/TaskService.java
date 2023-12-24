package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Optional<Task> add(Task task);

    boolean deleteById(int id);

    boolean replace(Task task);

    boolean setDoneTrue(int id);

    Optional<Task> findById(int id);

    Optional<Task> findById(int id, User user);

    Collection<Task> findAll(User user);

    Collection<Task> findAllDone(User user);

    Collection<Task> findAllNew(User user);
}
