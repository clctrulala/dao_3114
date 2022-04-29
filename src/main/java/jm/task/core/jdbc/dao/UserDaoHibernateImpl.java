package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = new Util().getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    private void sqlQuery(String request) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(request).executeUpdate();
            transaction.commit();
        } catch (HibernateException sqlErr) {
            TransactionStatus status = transaction.getStatus();

            if (status == TransactionStatus.ACTIVE || status == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void createUsersTable() {
        sqlQuery("create table if not exists User (Id bigint primary key auto_increment, Name varchar(20), lastName varchar(20), age tinyint);");
    }

    @Override
    public void dropUsersTable() {
        sqlQuery("drop table if exists User;");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException sqlErr) {
            TransactionStatus status = transaction.getStatus();

            if (status == TransactionStatus.ACTIVE || status == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            session.createQuery("delete User user where user.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        } catch (HibernateException err) {
            TransactionStatus status = transaction.getStatus();

            if (status == TransactionStatus.ACTIVE || status == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("select user from User user", User.class).getResultList();

            transaction.commit();
        } catch (HibernateException err) {
            TransactionStatus status = transaction.getStatus();

            if (status == TransactionStatus.ACTIVE || status == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        for ( User user : getAllUsers() ) {
            removeUserById(user.getId());
        }
    }
}
