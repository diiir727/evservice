package evservice.workers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;
import junit.framework.TestCase;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

import static evservice.workers.GetBalanceWorker.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetBalanceWorkerTest extends TestCase {
    private DAO dao = mock(DAO.class);
    private ObjectMapper mapper = new ObjectMapper();
    private GetBalanceWorker worker = new GetBalanceWorker(dao);
    private User testUser = new User(0, "test", DigestUtils.md5Hex("test"));

    public void testSuccessCase() throws Exception {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser.getLogin())).thenReturn(testUser);
        when(dao.getBalance(testUser)).thenReturn(12.5);
        assertEquals(worker.getResult(obj).get(EXTRAS_FIELD).get(BALANCE_FIELD).asDouble(), 12.5);
    }

    public void testUserNotExist() throws Exception {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser.getLogin())).thenReturn(null);
        assertEquals(worker.getResult(obj).get(RESULT_FIELD).asInt(), GetBalanceWorker.USER_NOT_EXIST_ANSWER);
    }

    public void testNotValidArgs() {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = createRequestJson("");
        assertEquals(worker.getResult(obj).get(RESULT_FIELD).asInt(), GetBalanceWorker.ERROR_ANSWER);
    }

    public void testSomeException() throws Exception {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser.getLogin())).thenThrow(SQLException.class);
        assertEquals(worker.getResult(obj).get(RESULT_FIELD).asInt(), GetBalanceWorker.ERROR_ANSWER);
    }

    public void testPasswordNotValid() throws Exception {
        ObjectNode obj = createRequestJson("test");
        User user = new User(0, "test", "other_pass");
        when(dao.getUserByLogin(user.getLogin())).thenReturn(user);
        assertEquals(worker.getResult(obj).get(RESULT_FIELD).asInt(), GetBalanceWorker.PASSWORD_FAIL_ANSWER);
    }

    private ObjectNode createRequestJson(String login){
        ObjectNode obj = mapper.createObjectNode();
        obj.put(REQUEST_LOGIN, login);
        obj.put(REQUEST_PASSWORD, "test");
        return obj;
    }

}
