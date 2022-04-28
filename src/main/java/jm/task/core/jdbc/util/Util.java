package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

import java.util.HashMap;
import java.util.Map;

public class Util implements AutoCloseable {
    private final String dbHost = "localhost:3306";
    private final String dbOwner = "root";
    private final String dbPassword = "biblio888tekar";
    private final String dbName = "pre_project_test_schema";
    private final String dbURL = "jdbc:mysql://" + dbHost + "/" + dbName;
    private final String sqlDialect = "org.hibernate.dialect.MySQL8Dialect";
    private final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private SessionFactory dbSessionFactory;

    public Util() {
        dbSessionFactory = getSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        if ( null == dbSessionFactory || dbSessionFactory.isClosed() ) {
            Map<String, String> hibernateSettings = new HashMap<>();

            hibernateSettings.put(AvailableSettings.DRIVER, dbDriver);
            hibernateSettings.put(AvailableSettings.DIALECT, sqlDialect);
            hibernateSettings.put(AvailableSettings.URL, dbURL);
            hibernateSettings.put(AvailableSettings.USER, dbOwner);
            hibernateSettings.put(AvailableSettings.PASS, dbPassword);

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(hibernateSettings)
                    .build();

            return dbSessionFactory = new MetadataSources( registry )
                    .addAnnotatedClass(User.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }
        return dbSessionFactory;
    }

    @Override
    public void close() {
        if ( null != dbSessionFactory && !dbSessionFactory.isClosed() ) {
            dbSessionFactory.getCurrentSession().close();
            dbSessionFactory.close();
        }
    }

}
