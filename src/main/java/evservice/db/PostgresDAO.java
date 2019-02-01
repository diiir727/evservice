package evservice.db;

import evservice.core.DAO;
import evservice.core.User;

import java.sql.*;

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

    @Override
    public double getBalance(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT sum(\"sum\") as balance FROM public.transactions WHERE user_id = ?"
        );
        preparedStatement.setInt(1, user.getId());
        ResultSet results = preparedStatement.executeQuery();
        double balance = results.getDouble("balance");
        preparedStatement.close();

        return balance;
    }

    @Override
    public User getUserByLogin(User user) throws SQLException {
        return null;
    }
}
