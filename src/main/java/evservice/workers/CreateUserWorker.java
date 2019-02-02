package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import evservice.core.DAO;
import evservice.core.User;
import org.json.JSONObject;

import java.sql.SQLException;

public class CreateUserWorker  implements Worker{

    static final int USER_REGISTER = 0;
    static final int USER_EXIST = 1;
    static final int SOME_ERROR = 2;
    private DAO dao;

    public CreateUserWorker(DAO dao) {
        this.dao = dao;
    }

    @Override
    public String getResult(JsonNode request){
        User user = getUserFromRequest(request);
        int type = registerUser(user);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("result", type);
        return jsonResult.toString();
    }

    private int registerUser(User user){
        try {
            return dao.registerUser(user) ? USER_REGISTER : USER_EXIST;
        } catch (SQLException e) {
            return SOME_ERROR;
        }
    }

    private User getUserFromRequest(JsonNode request) {
        String login = request.get("login").asText("");
        String password = request.get("password").asText("");

        if(login.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException();

        return new User(0, login, password);
    }
}
