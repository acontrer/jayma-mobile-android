package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 30-11-2016.
 */

public class CheckUserInEmergencyData {

    private int user_id;
    private boolean user_in_active_emergency;
    private boolean error;
    private String error_message;

    public CheckUserInEmergencyData(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isUser_in_active_emergency() {
        return user_in_active_emergency;
    }

    public void setUser_in_active_emergency(boolean user_in_active_emergency) {
        this.user_in_active_emergency = user_in_active_emergency;
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
