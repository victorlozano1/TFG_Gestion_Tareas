package com.tfg.gestiondetareas.Modelo;

import java.util.Date;

public class Tarea {

    private String nombre;
    private String descripcion;
    private Date Fecha_publicacion;

    private String nombre_publicador;

    private boolean Completada;

    public Tarea(String nombre, String descripcion, Date fecha_publicacion, String nombre_publicador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.Fecha_publicacion = fecha_publicacion;
        this.nombre_publicador = nombre_publicador;
        this.Completada = false;
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

    public Date getFecha_publicacion() {
        return Fecha_publicacion;
    }

    public void setFecha_publicacion(Date fecha_publicacion) {
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
