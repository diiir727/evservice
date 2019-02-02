package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.core.DAO;
import evservice.core.User;
import org.json.JSONObject;

public class GetBalanceWorker implements Worker{

    static final int SUCCESS_RESULT = 0;
    static final int SOME_ERROR = 2;
    static final int USER_NOT_EXIST = 3;
    static final int PASSWORD_FAIL = 4;

    private DAO dao;

    public GetBalanceWorker(DAO dao) {
        this.dao = dao;
    }

    @Override
    public String getResult(JsonNode request){
        try {
            User userFromRequest = getUserFromRequest(request);
            User dbUser = dao.getUserByLogin(userFromRequest);
            JSONObject resObj = new JSONObject();

            if(dbUser == null){
                resObj.put("result", USER_NOT_EXIST);
            } else if(!dbUser.getPassword().equals(userFromRequest.getPassword())) {
                resObj.put("result", PASSWORD_FAIL);
            } else {
                resObj.put("result", SUCCESS_RESULT);
                double balance = dao.getBalance(dbUser);
                JSONObject extras = new JSONObject();
                extras.put("balance", balance);
                resObj.put("extras", extras);
            }

            return resObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject failObj = new JSONObject();
            failObj.put("result", SOME_ERROR);
            return failObj.toString();
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
