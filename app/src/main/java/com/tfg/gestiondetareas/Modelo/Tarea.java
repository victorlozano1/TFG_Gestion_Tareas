package com.tfg.gestiondetareas.Modelo;

public class Tarea {

    private String IdTarea;
    private String nombre;
    private String descripcion;
    private String Fecha_publicacion;

    private String nombre_publicador;

    private boolean Completada;

    private String TipoTarea;

    private String correo_publicador;

    public Tarea(String nombre, String descripcion, String fecha_publicacion, String nombre_publicador, boolean completo, String TipoTarea, String correo_publicador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.Fecha_publicacion = fecha_publicacion;
        this.nombre_publicador = nombre_publicador;
        this.Completada = completo;
        this.TipoTarea = TipoTarea;
        this.correo_publicador = correo_publicador;
    }



    public String getTipoTarea() {
        return TipoTarea;
    }

    public String getCorreo_publicador() {
        return correo_publicador;
    }

    public void setCorreo_publicador(String correo_publicador) {
        this.correo_publicador = correo_publicador;
    }

    public void setTipoTarea(String tipoTarea) {
        TipoTarea = tipoTarea;
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
