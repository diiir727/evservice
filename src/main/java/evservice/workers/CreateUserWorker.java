package evservice.workers;

import evservice.core.DAO;
import evservice.core.User;
import org.json.JSONObject;

import java.sql.SQLException;

public class CreateUserWorker {

    static final int USER_REGISTER = 0;
    static final int USER_EXIST = 1;
    static final int SOME_ERROR = 2;
    private DAO dao;

    public CreateUserWorker(DAO dao) {
        this.dao = dao;
    }

    public JSONObject getResult(JSONObject request){
        User user = getUserFromRequest(request);
        int type = registerUser(user);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("result", type);
        return jsonResult;
    }

    private int registerUser(User user){
        try {
            return dao.registerUser(user) ? USER_REGISTER : USER_EXIST;
        } catch (SQLException e) {
            return SOME_ERROR;
        }
    }

    private User getUserFromRequest(JSONObject request) {
        String login = request.optString("login", "");
        String password = request.optString("password", "");

        if(login.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException();

        return new User(0, login, password);
    }
}
