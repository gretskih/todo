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
    public Optional<Task> add(Task task) {
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
    public boolean setDoneTrue(int id) {
        return taskRepository.setDoneTrue(id);
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
        return taskRepository.findAllDone();
    }

    @Override
    public Collection<Task> findAllNew() {
        return taskRepository.findAllNew();
    }
}
