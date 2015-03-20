package ch.six.sixwallet.backend.runkeeper.models;

import com.google.gson.annotations.SerializedName;

public class User {

    private int userId;
    private String profile;
    private String settings;
    @SerializedName("fitness_activities")
    private String fitnessActivities;
    @SerializedName("strength_training_activities")
    private String strengthTrainingActivities;
    private String sleep;
    private String nutrition;
    private String weight;
    @SerializedName("general_measurements")
    private String generalMeasurements;
    private String diabetes;
    private String records;
    private String team;

    public int getUserId() {
        return userId;
    }

    public String getProfile() {
        return profile;
    }

    public String getSettings() {
        return settings;
    }

    public String getFitnessActivities() {
        return fitnessActivities;
    }

    public String getStrengthTrainingActivities() {
        return strengthTrainingActivities;
    }

    public String getSleep() {
        return sleep;
    }

    public String getNutrition() {
        return nutrition;
    }

    public String getWeight() {
        return weight;
    }

    public String getGeneralMeasurements() {
        return generalMeasurements;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public String getRecords() {
        return records;
    }

    public String getTeam() {
        return team;
    }
}
