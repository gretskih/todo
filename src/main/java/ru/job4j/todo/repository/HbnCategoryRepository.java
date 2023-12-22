package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HbnCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAll() {
        return crudRepository.query(
                "from Category category ORDER BY category.name",
                Category.class
        );
    }

    @Override
    public List<Category> findByListId(List<Integer> idList) {
        return crudRepository.query(
                "from Category category where category.id in :fIdList",
                Category.class, Map.of("fIdList", idList)
        );
    }
}
