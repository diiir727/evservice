package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;

/**
 * Обработчик для создания пользователя
 */
public class CreateUserWorker extends Worker{

    static final int USER_REGISTER = 0;
    static final int USER_EXIST = 1;
    static final int SOME_ERROR = 2;
    private DAO dao;

    public CreateUserWorker(DAO dao) {
        this.dao = dao;
    }

    @Override
    public JsonNode getResult(JsonNode request){
        ObjectMapper mapper= new ObjectMapper();
        ObjectNode jsonResult = mapper.createObjectNode();
        try {
            User user = getUserFromRequest(request);
            int type = dao.registerUser(user) ? USER_REGISTER : USER_EXIST;
            jsonResult.put("result", type);
        } catch (Exception e) {
            jsonResult.put("result", SOME_ERROR);
        }
        return jsonResult;
    }

}
