package evservice.workers;

import evservice.core.DAO;
import evservice.core.User;

import java.sql.SQLException;

public class CreateUserWorker {

    private static final int USER_REGISTER = 0;
    private static final int USER_EXIST = 1;
    private static final int SOME_ERROR = 2;
    private DAO dao;

    public CreateUserWorker(DAO dao) {
        this.dao = dao;
    }


    public void getResult(String login, String pass){
        User user = new User(0, login, pass);
        int res = registerUser(user);

        System.out.println(res);
    }

    private int registerUser(User user){
        try {
            return dao.registerUser(user) ? USER_REGISTER : USER_EXIST;
        } catch (SQLException e) {
            return SOME_ERROR;
        }
    }
}
