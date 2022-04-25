package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final Util util = new Util();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        final String sqlUsersCreateTable = "create table if not exists User " +
                "(id bigint primary key auto_increment, Name varchar(20), lastName varchar(20)" +
                ", age tinyint)";

        try( Session session = util.getDBConnect().openSession() ) {
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery(sqlUsersCreateTable).executeUpdate();

            transaction.commit();
        }

    }

    @Override
    public void dropUsersTable() {
        final String sqlUsersDropTable = "drop table if exists User";

        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery(sqlUsersDropTable).executeUpdate();

            transaction.commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            session.persist( new User(name, lastName, age) );

            transaction.commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        final String hqlRemove = "delete User user where user.id = :id";

        try( Session session = util.getDBConnect().openSession() ){
            try {Transaction transaction = session.beginTransaction();
                session.createQuery( hqlRemove )
                        .setParameter( "id", id )
                        .executeUpdate();

                transaction.commit();
            } catch ( Exception err ) {
                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                session.getTransaction().rollback();
            }
        }
    }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            String hqlGetAll = "select user from User user";
            users = session.createQuery( hqlGetAll, User.class ).getResultList();

            transaction.commit();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            String hqlClean = "delete User";
            session.createQuery( hqlClean ).executeUpdate();

            transaction.commit();
        }
    }
}
