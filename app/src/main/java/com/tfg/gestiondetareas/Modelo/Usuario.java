package com.tfg.gestiondetareas.Modelo;

public class Usuario {

    public String Nombre;

    public String correo_electronico;

    public String contrasenia;

    public Usuario(String nombre, String correo_electronico, String contrasenia) {
        Nombre = nombre;
        this.correo_electronico = correo_electronico;
        this.contrasenia = contrasenia;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public String getContrasenia() {
        return contrasenia;
    }
}
