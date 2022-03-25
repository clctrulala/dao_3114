package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final String dbTableName = "User";
    private final Util util = new Util();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        final String sqlUsersCreateTable = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(20), lastName VARCHAR(20)" +
                ", age TINYINT)", dbTableName);

        try( Session session = util.getDBConnect().openSession() ) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(sqlUsersCreateTable).executeUpdate();
//            session.flush();
            transaction.commit();
        }
        System.out.printf("Table \"%s\" created", dbTableName);
    }

    @Override
    public void dropUsersTable() {
        final String sqlUsersDropTable = String.format("DROP TABLE IF EXISTS %s;", dbTableName);

        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(sqlUsersDropTable).executeUpdate();
//            session.flush();
            transaction.commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        System.out.println("persist user");

        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();
            session.persist( new User(name, lastName, age) );
//            session.flush();
            transaction.commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            String hqlRemove = String.format("delete %s user where user.id = :id", dbTableName);
            int deletedCount = session.createQuery( hqlRemove )
                    .setParameter( "id", id )
                    .executeUpdate();

            System.out.printf("Удалено %d идентификаторов\n", deletedCount);

//            session.flush();
            transaction.commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            String hqlGetAll = String.format("select user from %s user", dbTableName);
            users = (List<User>)session.createQuery( hqlGetAll ).getResultList();

            transaction.commit();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try( Session session = util.getDBConnect().openSession() ){
            Transaction transaction = session.beginTransaction();

            String hqlClean = String.format("delete %s", dbTableName);
            int deletedCount = session.createQuery( hqlClean ).executeUpdate();

            System.out.printf("Удалено %d идентификаторов\n", deletedCount);

//            session.flush();
            transaction.commit();
        }
    }
}
