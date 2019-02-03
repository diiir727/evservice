package evservice;

import evservice.core.DAO;
import evservice.db.PostgresDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        try(DAO dao = new PostgresDAO(jdbcUrl, login, password)) {
            RatpackServer.start(server -> server
                    .handlers(chain -> chain
                            .post(new PostHandler(dao))));
        } catch (Exception e) {
            logger.error("server error:", e);
        }
    }

    private static void printHelp(){
        System.out.println("Not valid params.");
        System.out.println("usage: <jdbcUrl>, <login>, <password>");
    }
}
