package evservice.server;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.db.PostgresDAO;
import evservice.workers.Factory;
import evservice.workers.Worker;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.RatpackServer;

import static ratpack.jackson.Jackson.jsonNode;

public class RatPackServer {

    //todo handle servlet exception
    public static void main(String[] args) throws Exception {
        Factory factory = new Factory(new PostgresDAO());
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .post(new Handler() {
                            @Override
                            public void handle(Context context) throws Exception {
                                context.parse(jsonNode()).then(obj -> {
                                    Worker worker = factory.createWorker(obj);
                                    JsonNode res = worker.getResult(obj);
                                    context.getResponse().contentTypeIfNotSet("application/json");
                                    context.getResponse().send(res.asText());
                                });
                            }
                        })
                )
        );
    }
}
