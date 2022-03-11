package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 Класс dao должен иметь конструктор пустой/по умолчанию
 Все поля должны быть private
 Обработка всех исключений, связанных с работой с базой данных должна находиться в dao

 Conection должен быть один, не должен создаваться при каждом запросе.
 через CallableProcedure?
 */

public class UserDaoJDBCImpl implements UserDao {
    private final String dbTableName = "user";
    private final Util util = new Util();

    public UserDaoJDBCImpl() {
    }

    private void sqlUpdate(String request) {
        try ( Statement statement = util.getDBConnect().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) ) {
            int touched = statement.executeUpdate(request);

//            System.out.println("DAO SQL обновлено строк: " + touched);
        } catch (SQLException sqlErr) {
//            sqlErr.printStackTrace();
//            System.out.println(sqlErr);
//            System.out.println("DAO SQL Ошибка обновления данных таблицы");
        }
    }

    private ResultSet sqlQuery(String request) throws SQLException {
        return util.getDBConnect().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(request);
    }

    public void createUsersTable() {
        final String sqlUsersCreateTable = String.format("CREATE TABLE IF NOT EXISTS %s (id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(20), lastName VARCHAR(20), age TINYINT);", dbTableName);

        sqlUpdate(sqlUsersCreateTable);
    }

    public void dropUsersTable() {
        final String sqlUsersDropTable = String.format("DROP TABLE IF EXISTS %s;", dbTableName);

        sqlUpdate(sqlUsersDropTable);
    }

    public void saveUser(String name, String lastName, byte age) {
        final String sqlAddUser = String.format("INSERT %s (name, lastName, age) VALUES(\"%s\", \"%s\", %d)", dbTableName, name, lastName, age);

        sqlUpdate(sqlAddUser);
    }

    public void removeUserById(long id) {
        final String sqlRemoveUser = String.format("DELETE FROM %s WHERE id=%d;", dbTableName, id);

        sqlUpdate(sqlRemoveUser);
    }

    public List<User> getAllUsers() {
        final String sqlGetUsers = String.format("SELECT * FROM %s;", dbTableName);
        final List<User> allUsers = new ArrayList<>();

        try ( ResultSet requestResult = sqlQuery(sqlGetUsers) ) {
            requestResult.first();
            do {
                User user = new User( requestResult.getString("name"), requestResult.getString("lastName"), requestResult.getByte("age") );
                user.setId( requestResult.getLong("id") );
                allUsers.add(user);

//                System.out.println("DAO Получен " + user);
            } while(requestResult.next());
        } catch (SQLException sqlErr) {
//            sqlErr.printStackTrace();
//            System.out.println("DAO ошибка при обработке записей");
        }

        return allUsers;
    }

    public void cleanUsersTable() {
        final String sqlRemoveUser = String.format("DELETE FROM %s;", dbTableName);

        sqlUpdate(sqlRemoveUser);
    }
}
