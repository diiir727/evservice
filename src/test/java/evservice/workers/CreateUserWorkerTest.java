package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import evservice.core.DAO;
import evservice.core.User;
import junit.framework.TestCase;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

import static evservice.workers.CreateUserWorker.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateUserWorkerTest extends TestCase {

    private DAO dao = mock(DAO.class);
    private User user = new User(0, "test", DigestUtils.md5Hex("test"));


    public void testSuccessResponse() throws Exception {
        when(dao.registerUser(user)).thenReturn(true);
        checkWorkerResult(USER_REGISTER_ANSWER);
    }

    public void testExistUserResponse() throws Exception {
        when(dao.registerUser(user)).thenReturn(false);
        checkWorkerResult(USER_EXIST_ANSWER);
    }

    public void testExceptionResponse() throws Exception {
        when(dao.registerUser(user)).thenThrow(SQLException.class);
        checkWorkerResult(SOME_ERROR_ANSWER);
    }

    private void checkWorkerResult(int actualRes){
        CreateUserWorker worker = new CreateUserWorker(dao);
        ObjectMapper mapper= new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put(REQUEST_LOGIN, "test");
        objectNode.put(REQUEST_PASSWORD, "test");
        JsonNode res = worker.getResult(objectNode);
        assertEquals(res.get(RESULT_FIELD).asInt(), actualRes);
    }
}
