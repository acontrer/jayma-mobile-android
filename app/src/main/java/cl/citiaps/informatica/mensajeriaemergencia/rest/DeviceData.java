package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 25-11-2016.
 */
public class DeviceData {

    private int user_id;
    private String token;
    private String device_kind = "ANDROID";
    private boolean error;
    private String error_message;

    public DeviceData(int user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_kind() {
        return device_kind;
    }

    public void setDevice_kind(String device_kind) {
        this.device_kind = device_kind;
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
