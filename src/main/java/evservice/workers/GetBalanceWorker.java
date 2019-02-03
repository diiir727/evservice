package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчик для получения баланса
 */
public class GetBalanceWorker extends Worker{

    static final int SUCCESS_ANSWER = 0;
    static final int ERROR_ANSWER = 2;
    static final int USER_NOT_EXIST_ANSWER = 3;
    static final int PASSWORD_FAIL_ANSWER = 4;

    static final String RESULT_FIELD = "result";
    static final String EXTRAS_FIELD = "extras";
    static final String BALANCE_FIELD = "balance";


    private DAO dao;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public GetBalanceWorker(DAO dao) {
        this.dao = dao;
    }

    @Override
    public JsonNode getResult(JsonNode request){
        ObjectMapper mapper= new ObjectMapper();
        ObjectNode resObj = mapper.createObjectNode();
        try {
            User userFromRequest = getUserFromRequest(request);
            User dbUser = dao.getUserByLogin(userFromRequest.getLogin());

            if(dbUser == null){
                resObj.put(RESULT_FIELD, USER_NOT_EXIST_ANSWER);
            } else if(!dbUser.getPassword().equals(userFromRequest.getPassword())) {
                resObj.put(RESULT_FIELD, PASSWORD_FAIL_ANSWER);
            } else {
                resObj.put(RESULT_FIELD, SUCCESS_ANSWER);
                double balance = dao.getBalance(dbUser);
                ObjectNode extras = mapper.createObjectNode();
                extras.put(BALANCE_FIELD, balance);
                resObj.set(EXTRAS_FIELD, extras);
            }
        } catch (Exception e) {
            logger.warn("get-balance worker error: ", e);
            resObj.put(RESULT_FIELD, ERROR_ANSWER);
        }
        return resObj;
    }



}
