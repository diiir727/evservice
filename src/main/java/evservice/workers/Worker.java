package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;

public interface Worker {

    JsonNode getResult(JsonNode request);
}
