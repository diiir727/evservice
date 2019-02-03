package evservice;

import evservice.core.DAO;
import evservice.db.PostgresDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Handler;
import ratpack.server.RatpackServer;

public class RatPackServer {

    private static final Logger logger = LoggerFactory.getLogger(RatPackServer.class);
    public static void main(String[] args) {
        if (args.length != 3) {
            printHelp();
        } else {
            run(args);
        }
    }

    private static void run(String[] args){
        String jdbcUrl = args[0];
        String login = args[1];
        String password = args[2];
        DAO dao = null;
        try {
            dao = new PostgresDAO(jdbcUrl, login, password);
            Handler handler = new PostHandler(dao);
            RatpackServer.start(server -> server
                    .handlers(chain -> chain
                            .post(handler)));
        } catch (Exception e) {
            close(dao);
            logger.error("server error:", e);
        }
    }

    private static void close(DAO dao) {
        if (dao != null) {
            try {
                dao.close();
            } catch (Exception e) {
                logger.warn("DAO close error: ", e);
            }
        }
    }

    private static void printHelp(){
        System.out.println("Not valid params.");
        System.out.println("usage: <jdbcUrl>, <login>, <password>");
    }
}
