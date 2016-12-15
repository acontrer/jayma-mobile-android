package cl.citiaps.informatica.mensajeriaemergencia.task;

import java.util.ArrayList;

/**
 * Created by kayjt on 14-12-2016.
 */

public class CheckImportantContactsTaskParams {

    private ArrayList<String> phoneNumbers;
    private int userId;

    public CheckImportantContactsTaskParams(ArrayList<String> phoneNumbers, int userId) {
        this.phoneNumbers = phoneNumbers;
        this.userId = userId;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
