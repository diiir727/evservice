package evservice;

import evservice.core.DAO;
import evservice.db.PostgresDAO;
import ratpack.server.RatpackServer;

public class RatPackServer {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            printHelp();
        } else {
            run(args);
        }
    }

    private static void run(String[] args){
        try {
            String jdbcUrl = args[0];
            String login = args[1];
            String password = args[2];

            DAO dao = new PostgresDAO(jdbcUrl, login, password);
            RatpackServer.start(server -> server
                    .handlers(chain -> chain
                            .post(new PostHandler(dao))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printHelp(){
        System.out.println("Not valid params.");
        System.out.println("usage: <jdbcUrl>, <login>, <password>");
    }
}
