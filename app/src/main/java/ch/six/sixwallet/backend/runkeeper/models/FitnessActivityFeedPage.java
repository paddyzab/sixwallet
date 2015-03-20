package ch.six.sixwallet.backend.runkeeper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FitnessActivityFeedPage {

    private int size;
    @SerializedName("items")
    private List<FitnessActivity> mFitnessActivities;
    private String previous;

    public int getSize() {
        return size;
    }

    public List<FitnessActivity> getFitnessActivities() {
        return mFitnessActivities;
    }

    public String getPrevious() {
        return previous;
    }
}
