package ch.six.sixwallet.backend.runkeeper.models;

import com.google.gson.annotations.SerializedName;

public class FitnessActivity {

    private String type;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("total_distance")
    private float totalDistance;
    private double duration;
    private String source;
    @SerializedName("entry_mode")
    private String entryMode;
    @SerializedName("has_map")
    private String hasMap;
    private String uri;

    public String getType() {
        return type;
    }

    public String getStartTime() {
        return startTime;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public double getDuration() {
        return duration;
    }

    public String getSource() {
        return source;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public String getHasMap() {
        return hasMap;
    }

    public String getUri() {
        return uri;
    }
}
