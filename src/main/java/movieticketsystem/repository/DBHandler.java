package movieticketsystem.repository;

import java.sql.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class DBHandler {

    private static Connection connection;

    private static PropertiesConfiguration databaseProperties = new PropertiesConfiguration();

    private DBHandler() {

    }
    public static Connection getConnection() {

        try {
            databaseProperties.load("database.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        String pass = databaseProperties.getString("database.password");
        String user = databaseProperties.getString("database.user");
        String host = databaseProperties.getString("database.host");
        String port = databaseProperties.getString("database.port");
        String dbName = databaseProperties.getString("database.name");
        String connectionUrl = host + ":" + port + "/" + dbName + "?serverTimezone=GMT%2B3";

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(connectionUrl, user, pass);
            }
        } catch (SQLException ex) {
            System.out.println("Unable to connect to database");
            ex.printStackTrace();
        }
        return connection;
    }

    public static void close(ResultSet result, PreparedStatement statement, Connection connection) {
        try {
            result.close();
            statement.close();
            connection.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static void close(PreparedStatement statement, Connection connection) {
        try {
            statement.close();
            connection.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
