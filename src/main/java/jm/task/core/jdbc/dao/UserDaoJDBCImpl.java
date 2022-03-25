package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String dbTableName = "user";
    private final Util util = new Util();

    public UserDaoJDBCImpl() {
    }

    private void sqlUpdate(String request) {

    }

    private ResultSet sqlQuery(String request) throws SQLException {
        return null;
    }

    public void createUsersTable() {

    }

    public void dropUsersTable() {

    }

    public void saveUser(String name, String lastName, byte age) {

    }

    public void removeUserById(long id) {

    }

    public List<User> getAllUsers() {

        return null;
    }

    public void cleanUsersTable() {

    }
}
