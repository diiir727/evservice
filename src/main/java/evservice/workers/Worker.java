package evservice.workers;

import com.fasterxml.jackson.databind.JsonNode;

public interface Worker {

    String getResult(JsonNode request);
}
