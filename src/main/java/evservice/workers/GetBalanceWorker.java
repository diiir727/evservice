package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;

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
    public JsonNode getResult(JsonNode request){
        ObjectMapper mapper= new ObjectMapper();
        try {
            User userFromRequest = getUserFromRequest(request);
            User dbUser = dao.getUserByLogin(userFromRequest);
            ObjectNode resObj = mapper.createObjectNode();

            if(dbUser == null){
                resObj.put("result", USER_NOT_EXIST);
            } else if(!dbUser.getPassword().equals(userFromRequest.getPassword())) {
                resObj.put("result", PASSWORD_FAIL);
            } else {
                resObj.put("result", SUCCESS_RESULT);
                double balance = dao.getBalance(dbUser);
                ObjectNode extras = mapper.createObjectNode();
                extras.put("balance", balance);
                resObj.set("extras", extras);
            }

            return resObj;
        } catch (Exception e) {
            e.printStackTrace();
            ObjectNode failObj = mapper.createObjectNode();
            failObj.put("result", SOME_ERROR);
            return failObj;
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
