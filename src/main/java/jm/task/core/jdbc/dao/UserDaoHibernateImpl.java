package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger( UserDaoHibernateImpl.class.getName() );
    private final String dbTableName = "User";
    private final Util util = new Util();

    public UserDaoHibernateImpl() {
    }

    private void sqlQuery( String request ) {
        try (Session session = util.getSession()) {
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery( String.format(request, dbTableName) ).executeUpdate();

            transaction.commit();
        } catch (PersistenceException sqlErr) {
            LOGGER.severe("Error in SQL: " + sqlErr.getMessage());
        }
    }

    @Override
    public void createUsersTable() {
        sqlQuery("create table if not exists %s (Id bigint primary key auto_increment, Name varchar(20), lastName varchar(20), age tinyint);");
    }

    @Override
    public void dropUsersTable() {
        sqlQuery("drop table if exists %s;");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try ( Session session = util.getSession() ) {
            session.save(new User(name, lastName, age));
        } catch (HibernateException sqlErr) {
            LOGGER.severe("Error on user persist");
        }
    }

    @Override
    public void removeUserById(long id) {
        try( Session session = util.getSession() ){
            Transaction transaction = session.beginTransaction();

//            session.createQuery( String.format("delete %s user where user.id = :id", dbTableName) )
//                    .setParameter( "id", id )
//                    .executeUpdate();

            CriteriaQuery<User> query = session.getCriteriaBuilder().createQuery(User.class);
            Root<User> user = query.from(User.class);
            Metamodel metaModel = session.getMetamodel();
            EntityType<User> User_ = metaModel.entity(User.class);

            query.where( user.get(User_.getId(Long.class)).in(id) );
            session.remove( session.createQuery(query).getSingleResult() );

            transaction.commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try( Session session = util.getSession() ){
//            users = session.createQuery( String.format("select user from %s user", dbTableName) ).getResultList();

            CriteriaQuery<User> query = session.getCriteriaBuilder().createQuery(User.class);
            Root<User> user = query.from(User.class);
            query.select(user);
            TypedQuery<User> userResult = session.createQuery(query);
            users = userResult.getResultList();
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
