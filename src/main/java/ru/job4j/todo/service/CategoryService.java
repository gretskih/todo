package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    Set<Category> findAll();

    Set<Category> findByListId(List<Integer> idList);
}
