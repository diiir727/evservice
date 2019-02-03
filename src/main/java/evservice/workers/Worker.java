package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.core.User;
import org.apache.commons.codec.digest.DigestUtils;

public abstract class Worker {

    static final String REQUEST_LOGIN = "login";
    static final String REQUEST_PASSWORD = "password";

    /**
     * Обрабатывает входные параметры и выдает необходимый результат
     * @param request входные параметры в формате JSON
     * @return результат в формате JSON
     */
    public abstract JsonNode getResult(JsonNode request);

    User getUserFromRequest(JsonNode request) {

        String login = request.get(REQUEST_LOGIN).asText("");
        String password = request.get(REQUEST_PASSWORD).asText("");

        if(login.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String md5Hex = DigestUtils.md5Hex(password);

        return new User(0, login, md5Hex);
    }
}
