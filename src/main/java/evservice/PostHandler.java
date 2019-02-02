package evservice;

import com.fasterxml.jackson.databind.JsonNode;
import evservice.core.DAO;
import evservice.workers.Factory;
import evservice.workers.Worker;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;


import static ratpack.jackson.Jackson.jsonNode;

public class PostHandler implements Handler {

    private final Factory factory;
    private Logger logger = LoggerFactory.getLogger(getClass());
    public PostHandler(DAO dao) {
        this.factory = new Factory(dao);
    }

    @Override
    public void handle(Context context) throws Exception {
        try {
            context.parse(jsonNode())
                    .onError(v -> sendInternalError(context))
                    .then(obj -> sendAnswer(context, obj));
        } catch (Exception e) {
            logger.warn("Handle error: ", e);
            sendInternalError(context);
        }
    }

    private void sendInternalError(Context context){
        context.getResponse().status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        context.getResponse().send();
    }

    private void sendAnswer(Context context, JsonNode obj){
        Worker worker = factory.createWorker(obj);
        JsonNode res = worker.getResult(obj);
        context.getResponse().contentTypeIfNotSet("application/json");
        context.getResponse().send(res.toString());
    }
}
