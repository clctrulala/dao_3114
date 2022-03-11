package jm.task.core.jdbc.util;

// ? Класс Util должен содержать логику настройки соединения с базой данных

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util implements AutoCloseable {
    // реализуйте настройку соеденения с БД
    private final String dbHost = "localhost:3306";
    private final String dbOwner = "root";
    private final String dbPassword = "biblio888tekar";
    private final String dbName = "pre_project_test_schema";
    private Connection dbConnect;

    public Util() {
        dbConnect = connect();
    }

    private Connection connect() {
        final String dbURL = "jdbc:mysql://" + dbHost + "/" + dbName;

        try {
            dbConnect = DriverManager.getConnection(dbURL, dbOwner, dbPassword);

//            System.out.println( "Schema : " + dbConnect.getSchema() ); // tables
//            System.out.println( "isClosed : " + dbConnect.isClosed() );
//            System.out.println( "Catalog : " + dbConnect.getCatalog() ); // database
//            System.out.println("Util Соединение с базой установлено");
        } catch (SQLException sqlErr) {
//            System.out.println(sqlErr);
//            System.out.println(sqlErr.getSQLState());
//            System.out.println("Util Ошибка соединения с базой данных");
        }
        return dbConnect;
    }

    public Connection getDBConnect() {
        if (null == dbConnect) { dbConnect = connect(); }
        return dbConnect;
    }

    @Override
    public void close() {
        try {
            if (null != dbConnect) { dbConnect.close(); }
        } catch (SQLException err) {}
    } // close()

} // Util class
