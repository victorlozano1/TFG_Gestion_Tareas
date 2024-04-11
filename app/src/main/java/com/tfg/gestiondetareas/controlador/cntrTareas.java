package com.tfg.gestiondetareas.controlador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.vistaPrincipal;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class cntrTareas {

public Context contexto;

String nombreUsu;

public cntrTareas(Context contexto) {
    this.contexto = contexto;
}



   private final String urldb="https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";
   private final String ruta_tarea="Tareas";

   //Este método añadira la tarea a la base de datos
public void AñadirTarea(String nombreTarea, String descripcion){


    DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

    // Supongamos que tienes un objeto Date
    Date fecha = new Date();

    // Crea un objeto SimpleDateFormat para el formato deseado
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Convierte la fecha a una cadena en el formato especificado
    String fechaFormateada = sdf.format(fecha);

    cntrRegistrarCuentas UsuarioLogado = new cntrRegistrarCuentas();

    nombreUsu = UsuarioLogado.recogerUsuarioLogado();



    Map<String, Object> nuevaTarea = new HashMap<>();
    nuevaTarea.put("nombre", nombreTarea);
    nuevaTarea.put("descripcion", descripcion);
    nuevaTarea.put("fecha", fechaFormateada);
    nuevaTarea.put("completada", false);
    nuevaTarea.put("nombre_publicador", nombreUsu);


    tareasRef.push().setValue(nuevaTarea);


    Intent intent = new Intent().setClass(contexto.getApplicationContext(), vistaPrincipal.class);
    Toast.makeText(contexto.getApplicationContext(), "Se ha añadido la tarea correctamente", Toast.LENGTH_SHORT).show();
    contexto.startActivity(intent);


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


 //Metodo que recogerá todas las tareas almacenadas en la base de datos y lo retornará como lista
    public void retornarListaTareas(TareasCallBack callback) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

        tareasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Tarea> listaTareas = new ArrayList<>();
                for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = tareaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tareaSnapshot.child("descripcion").getValue(String.class);
                    String fecha= tareaSnapshot.child("fecha").getValue(String.class);
                    String asignadaA = tareaSnapshot.child("nombre_publicador").getValue(String.class);
                    boolean completado = tareaSnapshot.child("completada").getValue(boolean.class);


                    System.out.println(nombre);
                    listaTareas.add(new Tarea(nombre, descripcion,  fecha, asignadaA, completado));
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
