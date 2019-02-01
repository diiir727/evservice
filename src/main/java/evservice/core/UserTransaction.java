package evservice.core;

public class UserTransaction {

    private int id;
    private int user_id;
    private int sum;

    public UserTransaction(int id, int user_id, int sum) {
        this.id = id;
        this.user_id = user_id;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getSum() {
        return sum;
    }
}
