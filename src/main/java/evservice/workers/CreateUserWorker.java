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

    static final int USER_REGISTER_ANSWER = 0;
    static final int USER_EXIST_ANSWER = 1;
    static final int SOME_ERROR_ANSWER = 2;

    static final String RESULT_FIELD = "result";
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
            int type = dao.registerUser(user) ? USER_REGISTER_ANSWER : USER_EXIST_ANSWER;
            jsonResult.put(RESULT_FIELD, type);
        } catch (Exception e) {
            jsonResult.put(RESULT_FIELD, SOME_ERROR_ANSWER);
        }
        return jsonResult;
    }

}
