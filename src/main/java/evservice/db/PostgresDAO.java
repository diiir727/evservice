package evservice.db;

import evservice.core.DAO;
import evservice.core.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresDAO implements DAO {

    private Connection connection;

    public PostgresDAO() throws Exception {
        Class.forName("org.postgresql.Driver");
        String host = "jdbc:postgresql://localhost:5432/den";
        String login = "den";
        String password = "123";
        connection = DriverManager.getConnection(host, login, password);
        connection.setAutoCommit(true);
    }

    public boolean registerUser(User user) throws SQLException{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO public.users( login, pass) VALUES (?, ?) ON CONFLICT DO NOTHING;"
            );
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            int res = preparedStatement.executeUpdate();
            preparedStatement.close();
            return res == 1;
    }
}
