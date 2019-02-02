package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.core.User;

abstract class Worker {

    /**
     * Обрабатывает входные параметры и выдает необходимый результат
     * @param request входные параметры в формате JSON
     * @return результат в формате JSON
     */
    abstract JsonNode getResult(JsonNode request);

    User getUserFromRequest(JsonNode request) {
        String login = request.get("login").asText("");
        String password = request.get("password").asText("");

        if(login.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException();

        return new User(0, login, password);
    }
}
