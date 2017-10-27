package cl.citiaps.informatica.mensajeriaemergencia.rest;

import java.util.Date;

/**
 * Created by kayjt on 23-11-2016.
 */

public class RegisterData {
    private String mail;
    private String pass;
    private String nombre_primero;
    private String nombre_segundo;
    private String apellido_paterno;
    private String apellido_materno;
    private String fecha_nacimiento;
    private String telefono;
    private String fb_Usuario;

    public RegisterData(String mail, String pass, String nombre_primero, String nombre_segundo,
                        String apellido_paterno, String apellido_materno, String fecha_nacimiento,
                        String telefono, String fb_Usuario){
        setMail(mail);
        setPass(pass);
        setNombre_primero(nombre_primero);
        setNombre_segundo(nombre_segundo);
        setApellido_paterno(apellido_paterno);
        setApellido_materno(apellido_materno);
        setFecha_nacimiento(fecha_nacimiento);
        setTelefono(telefono);
        setFb_Usuario(fb_Usuario);
    }


    public void setMail(String mail){this.mail = mail;}
    public String getMail(){return mail;}

    public void setPass(String pass){this.pass = pass;}
    public String getPass(){return pass;}

    public void setNombre_primero(String nombre_primero){this.nombre_primero = nombre_primero;}
    public String getNombre_primero(){return nombre_primero;}

    public void setNombre_segundo(String nombre_segundo){this.nombre_segundo = nombre_segundo;}
    public String getNombre_segundo(){return nombre_segundo;}

    public void setApellido_paterno(String apellido_paterno){this.apellido_paterno = apellido_paterno;}
    public String getApellido_paterno(){return apellido_paterno;}

    public void setApellido_materno(String apellido_materno){this.apellido_materno = apellido_materno;}
    public String getApellido_materno(){return apellido_materno;}

    public void setFecha_nacimiento(String fecha_nacimiento){this.fecha_nacimiento = fecha_nacimiento;}
    public String getFecha_nacimiento(){return fecha_nacimiento;}

    public void setTelefono(String telefono){this.telefono = telefono;}
    public String getTelefono(){return telefono;}

    public void setFb_Usuario(String fb_usuario){this.fb_Usuario = fb_usuario;}
    public String getFb_Usuario(){return fb_Usuario;}


}
