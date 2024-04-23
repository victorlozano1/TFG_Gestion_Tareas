package com.tfg.gestiondetareas.Modelo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Tarea {

    private String IdTarea;
    private String nombre;
    private String descripcion;
    private String Fecha_publicacion;

    private String nombre_publicador;

    private boolean Completada;



    public Tarea(String nombre, String descripcion, String fecha_publicacion, String nombre_publicador, boolean completo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.Fecha_publicacion = fecha_publicacion;
        this.nombre_publicador = nombre_publicador;
        this.Completada = completo;
    }

    public String getIdTarea() {
        return IdTarea;
    }

    public void setIdTarea(String idTarea) {
        IdTarea = idTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_publicacion() {

        return Fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        Fecha_publicacion = fecha_publicacion;
    }

    public String getNombre_publicador() {
        return nombre_publicador;
    }

    public void setNombre_publicador(String nombre_publicador) {
        this.nombre_publicador = nombre_publicador;
    }

    public boolean isCompletada() {
        return Completada;
    }

    public void setCompletada(boolean completada) {
        Completada = completada;
    }
}
