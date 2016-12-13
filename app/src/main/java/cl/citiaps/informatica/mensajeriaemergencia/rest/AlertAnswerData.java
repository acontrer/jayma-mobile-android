package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 08-12-2016.
 */

public class AlertAnswerData {

    private int userID;
    private boolean isOK;
    private String[] problems;
    private Double latitude;
    private Double longitude;


    public AlertAnswerData(int userID, boolean isOK, String[] problems, Double latitude, Double longitude) {
        this.userID = userID;
        this.isOK = isOK;
        this.problems = problems;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }

    public String[] getProblems() {
        return problems;
    }

    public void setProblems(String[] problems) {
        this.problems = problems;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
