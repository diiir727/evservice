package evservice.db;

import evservice.core.DAO;
import evservice.core.DAOException;
import evservice.core.User;
import evservice.core.UserTransaction;

import java.sql.*;

public class PostgresDAO implements DAO {

    private Connection connection;

    public PostgresDAO(String jdbcUrl, String login, String password) throws DAOException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcUrl, login, password);
            connection.setAutoCommit(true);
        }catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public boolean registerUser(User user) throws DAOException{
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO public.users( login, pass) VALUES (?, ?) ON CONFLICT DO NOTHING;"
            );
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            int res = preparedStatement.executeUpdate();
            preparedStatement.close();
            return res == 1;
        }catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public double getBalance(User user) throws DAOException {
        try {
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
        }catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void createTransaction(UserTransaction transaction) throws DAOException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO public.transactions(user_id, sum) VALUES (?, ?)"
            );
            preparedStatement.setInt(1, transaction.getUserId());
            preparedStatement.setDouble(2, transaction.getSum());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public User getUserByLogin(String login) throws DAOException {
        try{
            User result = null;
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM public.users WHERE login = ?"
            );
            preparedStatement.setString(1, login);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                int id = results.getInt("id");
                String pass = results.getString("pass");
                result = new User(id, login, pass);
            }
            preparedStatement.close();
            return result;
        }catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
