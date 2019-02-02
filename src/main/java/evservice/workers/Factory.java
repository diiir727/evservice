package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.core.DAO;

public class Factory {

    private DAO dao;

    public Factory(DAO dao) {
        this.dao = dao;
    }

    public Worker createWorker(JsonNode request) {
        String type = request.get("type").asText("");
        if("create".equals(type)){
            return new CreateUserWorker(dao);
        } else if ("get-balance".equals(type)) {
            return new GetBalanceWorker(dao);
        }
        return null;
    }
}
