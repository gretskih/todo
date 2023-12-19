package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public Optional<Task> add(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            return Optional.of(task);
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        boolean result = false;
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
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean replace(int id, Task task) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "UPDATE Task SET title = :fTitle"
                                    + ", description = :fDescription"
                                    + ", done = :fDone WHERE id = :fId")
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean setDoneTrue(int id) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "UPDATE Task SET done = :fDone WHERE id = :fId")
                    .setParameter("fDone", true)
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task where id = :fId", Task.class);
            query.setParameter("fId", id);
            Optional<Task> taskOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
            return taskOptional;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public Collection<Task> findAll() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task task ORDER BY task.title", Task.class);
            List<Task> taskList = query.list();
            session.getTransaction().commit();
            return taskList;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Task> findAllDone() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task task where done = true ORDER BY task.title", Task.class);
            List<Task> taskList = query.list();
            session.getTransaction().commit();
            return taskList;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Task> findAllNew() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("from Task task where done = false ORDER BY task.title", Task.class);
            List<Task> taskList = query.list();
            session.getTransaction().commit();
            return taskList;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }
}
