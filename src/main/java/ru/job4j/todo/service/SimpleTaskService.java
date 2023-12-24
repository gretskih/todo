package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> add(Task task) {
        return taskRepository.add(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean replace(Task task) {
        return taskRepository.replace(task);
    }

    @Override
    public boolean setDoneTrue(int id) {
        return taskRepository.setDoneTrue(id);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Optional<Task> findById(int id, User user) {
        var taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            var task = getTimesForUser(taskOptional.get(), user);
            return Optional.of(task);
        }
        return taskOptional;
    }

    @Override
    public Collection<Task> findAll(User user) {
        return getTimesForUser(taskRepository.findAll(), user);
    }

    @Override
    public Collection<Task> findAllDone(User user) {
        return getTimesForUser(taskRepository.findAllDone(), user);
    }

    @Override
    public Collection<Task> findAllNew(User user) {
        return getTimesForUser(taskRepository.findAllNew(), user);
    }

    private Collection<Task> getTimesForUser(Collection<Task> tasks, User user) {
        for (Task task : tasks) {
            var created = getZonedDateTime(task, user);
            task.setCreated(LocalDateTime.from(created));
        }
        return tasks;
    }

    private Task getTimesForUser(Task task, User user) {
            var created = getZonedDateTime(task, user);
            task.setCreated(LocalDateTime.from(created));
        return task;
    }

    private ZonedDateTime getZonedDateTime(Task task, User user) {
        var defaultTimeZone = TimeZone.getDefault();
        return task.getCreated().atZone(
                        ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(user.getTimezone() == null ? defaultTimeZone.getID() : user.getTimezone()));
    }
}
