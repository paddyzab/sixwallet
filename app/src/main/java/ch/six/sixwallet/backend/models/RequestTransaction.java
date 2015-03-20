package ch.six.sixwallet.backend.models;

import com.google.gson.annotations.SerializedName;

public class RequestTransaction {

    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("amount")
    private String mAmount;
    @SerializedName("comment")
    private String mComment;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
