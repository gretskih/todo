package ru.job4j.todo.repository;

import ru.job4j.todo.model.UserStore;

import java.util.Optional;

public interface UserRepository {
    Optional<UserStore> save(UserStore userStore);

    Optional<UserStore> findByLoginAndPassword(String login, String password);
}
