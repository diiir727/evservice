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
    private ObjectMapper mapper= new ObjectMapper();

    public void testSuccessCase() throws SQLException {
        ObjectNode obj = mapper.createObjectNode();
        obj.put("login", "test");
        obj.put("password", "test");
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenReturn(user);
        when(dao.getBalance(user)).thenReturn(12.5);

        assertEquals(worker.getResult(obj).get("extras").get("balance").asDouble(), 12.5);
    }

    public void testUserNotExist() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = mapper.createObjectNode();

        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenReturn(null);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.USER_NOT_EXIST);
    }

    public void testNotValidArgs() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = mapper.createObjectNode();

        obj.put("login", "");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.SOME_ERROR);
    }

    public void testSomeException() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = mapper.createObjectNode();

        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenThrow(SQLException.class);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.SOME_ERROR);
    }

    public void testPasswordNotValid() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        ObjectNode obj = mapper.createObjectNode();

        User user = new User(0, "test", "other_pass");
        when(dao.getUserByLogin(user)).thenReturn(user);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).get("result").asInt(), GetBalanceWorker.PASSWORD_FAIL);
    }

}
