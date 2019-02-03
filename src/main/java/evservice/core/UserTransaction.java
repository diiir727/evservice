package evservice.core;

public class UserTransaction {

    private int id;
    private int userId;
    private double sum;

    public UserTransaction(int id, int userId, int sum) {
        this.id = id;
        this.userId = userId;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getSum() {
        return sum;
    }
}
