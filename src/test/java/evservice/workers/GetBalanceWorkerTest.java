package evservice.workers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;
import junit.framework.TestCase;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetBalanceWorkerTest extends TestCase {
    private DAO dao = mock(DAO.class);
    private ObjectMapper mapper = new ObjectMapper();
    private GetBalanceWorker worker = new GetBalanceWorker(dao);
    private User testUser = new User(0, "test", "test");

    public void testSuccessCase() throws SQLException {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser)).thenReturn(testUser);
        when(dao.getBalance(testUser)).thenReturn(12.5);
        assertEquals(worker.getResult(obj).get("extras").get("balance").asDouble(), 12.5);
    }

    public void testUserNotExist() throws SQLException {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser)).thenReturn(null);
        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.USER_NOT_EXIST);
    }

    public void testNotValidArgs() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = createRequestJson("");
        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.SOME_ERROR);
    }

    public void testSomeException() throws SQLException {
        ObjectNode obj = createRequestJson("test");
        when(dao.getUserByLogin(testUser)).thenThrow(SQLException.class);
        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.SOME_ERROR);
    }

    public void testPasswordNotValid() throws SQLException {
        ObjectNode obj = createRequestJson("test");
        User user = new User(0, "test", "other_pass");
        when(dao.getUserByLogin(user)).thenReturn(user);
        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.PASSWORD_FAIL);
    }

    private ObjectNode createRequestJson(String login){
        ObjectNode obj = mapper.createObjectNode();
        obj.put("login", login);
        obj.put("password", "test");
        return obj;
    }

}
