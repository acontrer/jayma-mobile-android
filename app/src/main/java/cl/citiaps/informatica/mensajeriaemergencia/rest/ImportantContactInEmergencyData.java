package cl.citiaps.informatica.mensajeriaemergencia.rest;

import java.util.ArrayList;

/**
 * Created by kayjt on 15-12-2016.
 */

public class ImportantContactInEmergencyData {

    private ArrayList<ImportantContactAnswerData> contacts;
    private Boolean error;
    private String error_message;

    public ImportantContactInEmergencyData(ArrayList<ImportantContactAnswerData> contacts, Boolean error, String error_message) {
        this.contacts = contacts;
        this.error = error;
        this.error_message = error_message;
    }

    public ArrayList<ImportantContactAnswerData> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ImportantContactAnswerData> contacts) {
        this.contacts = contacts;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
