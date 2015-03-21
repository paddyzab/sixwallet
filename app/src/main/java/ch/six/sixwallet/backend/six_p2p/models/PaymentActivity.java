package ch.six.sixwallet.backend.six_p2p.models;

public class PaymentActivity {

    //STATUSES
    public final static int OPEN = 1;
    public final static int DONE = 2;
    public final static int DONE_NEWUSER = 3;
    public final static int PENDING = 4;
    public final static int PENDING_NEWUSER = 5;
    public final static int ACCEPTED = 6;
    public final static int REJECTED = 7;

    //TYPES
    public final static int LOAD_FROM_CC = 1;
    public final static int UNLOAD_TO_BANK_ACCOUNT = 2;
    public final static int SEND = 3;
    public final static int REQUESTED = 4;

    private int id;
    private long timestamp;
    private int type;
    private int status;
    private int amount;
    private String phoneNumber;
    private int senderUserId;
    private int receiverUserId;
    private String clientDataRequestType;

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getAmount() {
        return amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public int getReceiverUserId() {
        return receiverUserId;
    }

    public String getClientDataRequestType() {
        return clientDataRequestType;
    }
}
