package evservice.workers;

import evservice.core.DAO;
import evservice.core.User;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetBalanceWorkerTest extends TestCase {
    private DAO dao = mock(DAO.class);

    public void testSuccessCase() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        JSONObject obj = new JSONObject();
        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenReturn(user);
        when(dao.getBalance(user)).thenReturn(12.5);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).getJSONObject("extras").getDouble("balance"), 12.5);
    }

    public void testUserNotExist() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        JSONObject obj = new JSONObject();
        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenReturn(null);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).getInt("result"), GetBalanceWorker.USER_NOT_EXIST);
    }

    public void testNotValidArgs() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        JSONObject obj = new JSONObject();
        obj.put("login", "");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).getInt("result"), GetBalanceWorker.SOME_ERROR);
    }

    public void testSomeException() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        JSONObject obj = new JSONObject();
        User user = new User(0, "test", "test");
        when(dao.getUserByLogin(user)).thenThrow(SQLException.class);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).getInt("result"), GetBalanceWorker.SOME_ERROR);
    }

    public void testPasswordNotValid() throws SQLException {
        GetBalanceWorker worker = new GetBalanceWorker(dao);
        JSONObject obj = new JSONObject();
        User user = new User(0, "test", "other_pass");
        when(dao.getUserByLogin(user)).thenReturn(user);
        obj.put("login", "test");
        obj.put("password", "test");

        assertEquals(worker.getResult(obj).getInt("result"), GetBalanceWorker.PASSWORD_FAIL);
    }

}
