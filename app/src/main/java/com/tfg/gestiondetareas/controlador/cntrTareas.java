package com.tfg.gestiondetareas.controlador;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class cntrTareas {

public cntrTareas() {}

    ArrayList<Tarea> listaTareas = new ArrayList<>();


   private final String urldb="https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";
   private final String ruta_tarea="Tareas";

    //Este método añadira la tarea a la base de datos
public void AñadirTarea(String nombre, String descripcion) {


    DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

    Map<String, Object> nuevoUsuario = new HashMap<>();
    nuevoUsuario.put("nombre", nombre);
    nuevoUsuario.put("descripcion", descripcion);
    nuevoUsuario.put("fecha", new Date());
    nuevoUsuario.put("completada", false);
    nuevoUsuario.put("nombre_publicador", "Pepe");

    tareasRef.push().setValue(nuevoUsuario);



}


//Método que comprueba si la lista se encuentra vacia
public boolean comprobarListaVacia() {


    final boolean[] listaVacia = {true}; // Usamos un array para poder modificar el valor dentro del ValueEventListener
    DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

    // Realizamos la consulta a la base de datos
    tareasRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Actualizamos el valor de listaVacia de acuerdo a los datos obtenidos de la base de datos
            listaVacia[0] = !dataSnapshot.exists() || !dataSnapshot.hasChildren();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Manejar errores de lectura de la base de datos
            System.out.println("Error al leer datos: " + databaseError.getMessage());
        }
    });

    // Devolvemos el valor actual de listaVacia
    return listaVacia[0];

 }


    public void retornarListaTareas(TareasCallBack callback) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

        tareasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Tarea> listaTareas = new ArrayList<>();
                for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = tareaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tareaSnapshot.child("descripcion").getValue(String.class);
                   // Long fechaLong = dataSnapShot.child("fecha_publicacion").getValue(Long.class);
                    String asignadaA = tareaSnapshot.child("nombre_publicador").getValue(String.class);

                    System.out.println(nombre);
                    listaTareas.add(new Tarea(nombre, descripcion, new Date(), asignadaA));
                }
                callback.onTareasLoaded(listaTareas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.i("ErrorLecturaBD", "Error al leer datos: " + databaseError.getMessage());
            }
        });
    }

}
