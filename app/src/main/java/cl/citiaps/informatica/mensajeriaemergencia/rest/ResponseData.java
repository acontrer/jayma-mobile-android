package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 28-11-2016.
 */

public class ResponseData {

    private boolean error;
    private String error_message;

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
