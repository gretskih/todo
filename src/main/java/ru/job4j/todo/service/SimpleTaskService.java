package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task add(Task task) {
        return taskRepository.add(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean replace(int id, Task task) {
        return taskRepository.replace(id, task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Collection<Task> findAllDone() {
        return findAll().stream().filter(Task::isDone).collect(Collectors.toList());
    }

    @Override
    public Collection<Task> findAllNew() {
        return findAll().stream().filter(x -> !x.isDone()).collect(Collectors.toList());
    }
}
