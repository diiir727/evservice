package evservice.core;

import java.sql.SQLException;

public interface DAO {

    boolean registerUser(User user) throws SQLException;

}
