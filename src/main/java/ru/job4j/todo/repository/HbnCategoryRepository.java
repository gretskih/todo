package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbnCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public Set<Category> findAll() {
        return new HashSet<>(crudRepository.query(
                "from Category category ORDER BY category.name",
                Category.class
        ));
    }

    @Override
    public Set<Category> findByListId(List<Integer> idList) {
        return new HashSet<>(crudRepository.query(
                "from Category category where category.id in :fIdList",
                Category.class, Map.of("fIdList", idList)
        ));
    }
}
