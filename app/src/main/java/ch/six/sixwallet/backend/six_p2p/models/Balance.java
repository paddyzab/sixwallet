package ch.six.sixwallet.backend.six_p2p.models;

import com.google.gson.annotations.SerializedName;

public class Balance {

    @SerializedName("balance")
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
