package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Util {
    private final String dbHost = "localhost:3306";
    private final String dbOwner = "root";
    private final String dbPassword = "biblio888tekar";
    private final String dbName = "pre_project_test_schema";
    private SessionFactory dbSessionFactory;

    public Util() {
        dbSessionFactory = connect();
    }

    private SessionFactory connect() {
        final String dbURL = "jdbc:mysql://" + dbHost + "/" + dbName;
        final String sqlDialect = "org.hibernate.dialect.MySQLDialect";
        final String dbDriver = "com.mysql.cj.jdbc.Driver";

        Map<String, String> hibernateSettings = new HashMap<>();

        hibernateSettings.put(AvailableSettings.DRIVER, dbDriver);
        hibernateSettings.put(AvailableSettings.DIALECT, sqlDialect);
        hibernateSettings.put(AvailableSettings.URL, dbURL);
        hibernateSettings.put(AvailableSettings.USER, dbOwner);
        hibernateSettings.put(AvailableSettings.PASS, dbPassword);
//        hibernateSettings.put(AvailableSettings.SHOW_SQL, "true");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateSettings)
                .build();

        return dbSessionFactory = new MetadataSources( registry )
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory();
    } // connect()

    public SessionFactory getDBConnect() {
        if (null == dbSessionFactory) { dbSessionFactory = connect(); }
        return dbSessionFactory;
    }

} // Util class
