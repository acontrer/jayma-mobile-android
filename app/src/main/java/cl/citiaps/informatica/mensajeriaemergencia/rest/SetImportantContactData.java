package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 14-12-2016.
 */

public class SetImportantContactData {

    public int isImportant;
    public int importantTo;

    public SetImportantContactData(int isImportant, int importantTo) {
        this.isImportant = isImportant;
        this.importantTo = importantTo;
    }

    public int getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(int isImportant) {
        this.isImportant = isImportant;
    }

    public int getImportantTo() {
        return importantTo;
    }

    public void setImportantTo(int importantTo) {
        this.importantTo = importantTo;
    }
}
