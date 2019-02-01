package evservice.workers;

import evservice.core.DAO;
import evservice.core.User;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateUserWorkerTest extends TestCase {

    private DAO dao = mock(DAO.class);
    private User user = new User(0, "test", "test");


    public void testSuccessResponse() throws SQLException {
        when(dao.registerUser(user)).thenReturn(true);
        checkWorkerResult(CreateUserWorker.USER_REGISTER);
    }

    public void testExistUserResponse() throws SQLException {
        when(dao.registerUser(user)).thenReturn(false);
        checkWorkerResult(CreateUserWorker.USER_EXIST);
    }

    public void testExceptionResponse() throws SQLException {
        when(dao.registerUser(user)).thenThrow(SQLException.class);
        checkWorkerResult(CreateUserWorker.SOME_ERROR);
    }

    private void checkWorkerResult(int actualRes){
        CreateUserWorker worker = new CreateUserWorker(dao);
        JSONObject res = worker.getResult("test", "test");
        assertEquals(res.getInt("result"), actualRes);
    }
}
