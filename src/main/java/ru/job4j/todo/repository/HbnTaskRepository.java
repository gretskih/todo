package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public Task add(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        boolean result;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean replace(int id, Task task) {
        Session session = sf.openSession();
        boolean result;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "UPDATE Task SET title = :fTitle"
                                    + ", description = :fDescription"
                                    + ", created = :fCreated"
                                    + ", done = :fDone WHERE id = :fId")
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> userOptional = Optional.empty();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task where id = :fId", Task.class);
            query.setParameter("fId", id);
            userOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return userOptional;
    }

    @Override
    public Collection<Task> findAll() {
        Session session = sf.openSession();
        List<Task> taskList;
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task", Task.class);
            taskList = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return taskList.stream().sorted(Comparator.comparingInt(Task::getId)).collect(Collectors.toList());
    }
}
