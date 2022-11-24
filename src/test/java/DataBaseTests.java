import org.junit.Assert;
import org.junit.Test;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DataBaseTests {

    private final String dbName = "db-example";

    private Connection getConnection() {
        String dbSqlitePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource(dbName)).getPath();
        try {
            Connection connection = DriverManager.getConnection(JDBC.PREFIX + dbSqlitePath);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = getConnection();
        Assert.assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testInsert() {
        String query = "INSERT INTO USER(LOGIN, EMAIL, AGE) VALUES(?, ?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, "TEST_USER_1");
            statement.setString(2, "test_email_1@test.com");
            statement.setInt(3, 73);
            statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSelect() {
        String query = "SELECT * FROM USER";
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println("ID: " + result.getInt("ID") + "\n" +
                        "LOGIN: " + result.getString("LOGIN") + "\n" +
                        "EMAIL: " + result.getString("EMAIL") + "\n" +
                        "AGE: " + result.getInt("AGE"));
            }

            connection.close();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }
}
