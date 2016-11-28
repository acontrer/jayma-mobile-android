package cl.citiaps.informatica.mensajeriaemergencia.rest;


import java.util.Date;

/**
 * Created by kayjt on 28-11-2016.
 */

public class LocationData {

    Double latitude;
    Double longitude;
    Date creation_date;
    int user_id;
    boolean error;
    String error_message;

    public LocationData(int user_id, Double latitude, Double longitude) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creation_date = new Date();
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

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
