package ru.job4j.todo.service;

import ru.job4j.todo.model.UserStore;

import java.util.Optional;

public interface UserService {
    Optional<UserStore> save(UserStore userStore);

    Optional<UserStore> findByLoginAndPassword(String login, String password);
}
