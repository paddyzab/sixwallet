package ch.six.sixwallet.backend.six_p2p.models;

public class Transaction {

    private int id;
    private long timestamp;
    private int type;
    private int amount;
    private String phoneNumber;

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
