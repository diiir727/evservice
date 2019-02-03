package evservice.core;

import java.sql.SQLException;

public interface DAO {

    boolean registerUser(User user) throws SQLException;
    double getBalance(User user) throws SQLException;
    void createTransaction(UserTransaction transaction) throws SQLException;
    User getUserByLogin(User user) throws SQLException;

}
