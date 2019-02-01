package evservice;

import evservice.core.DAO;
import evservice.db.PostgresDAO;
import evservice.workers.CreateUserWorker;

public class Main {

    public static void main(String[] args) {
        try {
            DAO dao = new PostgresDAO();
            CreateUserWorker worker = new CreateUserWorker(dao);
            worker.getResult("sasha1", "den");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
