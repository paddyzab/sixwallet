package ch.six.sixwallet.backend.six_p2p.models;

import com.google.gson.annotations.SerializedName;

public class RequestTransaction {

    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("amount")
    private String mAmount;
    @SerializedName("comment")
    private String mComment;

    public RequestTransaction() {

    }

    public RequestTransaction setPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
        return this;
    }

    public RequestTransaction setAmount(final String amount) {
        mAmount = amount;
        return this;
    }

    public RequestTransaction setComment(final String comment) {
        mComment = comment;
        return this;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getComment() {
        return mComment;
    }
}
