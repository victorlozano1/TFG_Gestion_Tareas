package com.tfg.gestiondetareas.controlador;

import com.tfg.gestiondetareas.Modelo.Tarea;

import java.util.ArrayList;

public interface TareasCallBack {
    void onTareasLoaded(ArrayList<Tarea> tareas);

}
