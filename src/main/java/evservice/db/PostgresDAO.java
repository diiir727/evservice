package evservice.db;

import evservice.core.DAO;
import evservice.core.User;

import java.sql.*;

public class PostgresDAO implements DAO {

    private Connection connection;

    public PostgresDAO(String jdbcUrl, String login, String password) throws Exception {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(jdbcUrl, login, password);
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
        double balance = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT sum(\"sum\") as balance FROM public.transactions WHERE user_id = ?"
        );
        preparedStatement.setInt(1, user.getId());
        ResultSet results = preparedStatement.executeQuery();
        if(results.next())
            balance = results.getDouble("balance");
        preparedStatement.close();
        return balance;
    }

    @Override
    public User getUserByLogin(User user) throws SQLException {

        User result = null;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM public.users WHERE login = ?"
        );
        preparedStatement.setString(1, user.getLogin());
        ResultSet results = preparedStatement.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String pass = results.getString("pass");
            String login = results.getString("login");
            result = new User(id, login, pass);
        }
        preparedStatement.close();

        return result;
    }
}
