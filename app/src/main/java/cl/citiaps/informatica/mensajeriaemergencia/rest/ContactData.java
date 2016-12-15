package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 13-12-2016.
 */

public class ContactData {

    private int user_id;
    private String first_name;
    private String last_name;
    private Boolean is_important;

    public ContactData(int user_id, String first_name, String last_name, Boolean is_important) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_important = is_important;
    }

    public String getFullName(){

        return first_name + " " + last_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Boolean getIs_important() {
        return is_important;
    }

    public void setIs_important(Boolean is_important) {
        this.is_important = is_important;
    }
}
