package evservice;

import evservice.core.DAO;
import evservice.db.PostgresDAO;
import ratpack.server.RatpackServer;

public class RatPackServer {

    public static void main(String[] args) throws Exception {
//        String host = "jdbc:postgresql://localhost:5432/den";
//        String login = "den";
//        String password = "123";
        if (args.length != 3)
            throw new IllegalArgumentException("not valid args");

        String jdbcUrl = args[0];
        String login = args[1];
        String password = args[2];

        DAO dao = new PostgresDAO(jdbcUrl, login, password);
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .post(new PostHandler(dao))));
    }
}
