package cl.citiaps.informatica.mensajeriaemergencia.rest;

import java.util.Date;

/**
 * Created by kayjt on 15-12-2016.
 */

public class ImportantContactAnswerData {

    private int userId;
    private String firstName;
    private String lastName;
    private boolean answer;
    private Double latitude;
    private Double longitude;
    private boolean isOk;
    private Date creationDateTime;
    private String emergencyName;
    private String fullAddress;

    public ImportantContactAnswerData(int userId, String firstName, String lastName, boolean answer, Double latitude, Double longitude, boolean isOk, Date creationDateTime, String emergencyName, String fullAddress) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.answer = answer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOk = isOk;
        this.creationDateTime = creationDateTime;
        this.emergencyName = emergencyName;
        this.fullAddress = fullAddress;
    }

    public String getFullName(){

        return this.firstName + " " + this.lastName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
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

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}


