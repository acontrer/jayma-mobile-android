package cl.citiaps.informatica.mensajeriaemergencia.rest;

/**
 * Created by kayjt on 21-11-2016.
 */

public class LoginData {

    private String username;
    private String password;
    private String token;
    private boolean error;
    private int user_id;
    private String error_message;
    private int code;

    public LoginData(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public void setCode(int code){this.code = code;}
    public Integer getCode(){return code;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}