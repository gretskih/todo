package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.UserStore;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<UserStore> save(UserStore userStore) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(userStore);
            session.getTransaction().commit();
            return Optional.of(userStore);
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
    public Optional<UserStore> findByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<UserStore> query = session.createQuery("from UserStore where login = :fLogin AND password = :fPassword", UserStore.class);
            query.setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            Optional<UserStore> userStoreOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
            return userStoreOptional;
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
}
