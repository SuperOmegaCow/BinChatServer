package binchat.sql;

import java.sql.*;

public class Database {

    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    private Connection connection;

    public Database(String user, String database, String password, String port, String hostname) {
        this.user = user;
        this.database = database;
        this.password = password;
        this.port = port;
        this.hostname = hostname;
    }

    public Connection openConnection() throws Exception, ClassNotFoundException {
        if (checkConnection())
            return connection;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                        + this.hostname + ":" + this.port + "/" + this.database,
                this.user, this.password);
        return connection;
    }

    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection == null)
            return false;
        connection.close();
        return true;
    }

    public boolean exists(String username) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM binchat WHERE Username = '" + username + "';")
                    .executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getPassword(String username) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM binchat WHERE Username = '" + username + "';")
                    .executeQuery();
            return resultSet.getString("Password");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public boolean isAdmin(String username) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM binchat WHERE Username = '" + username + "';")
                    .executeQuery();
            return resultSet.getBoolean("Admin");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void add(String username, String password) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO binchat (Username, Password, Admin) Values (?, ?, ?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBoolean(3, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
