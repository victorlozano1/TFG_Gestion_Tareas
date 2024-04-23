package com.tfg.gestiondetareas.controlador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.Vista.vistaPrincipal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class cntrTareas {

public Context contexto;


    private cntrRegistrarCuentas controladorCuentas;
public cntrTareas(Context contexto) {
    this.contexto = contexto;
}



   private final String urldb="https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";
   private final String ruta_tarea="Tareas";

   private Usuario nombreUsu;
   private boolean Repite = false;

   //Este método añadira la tarea a la base de datos
   public void AñadirTarea(String nombreTarea, String descripcion) {

       controladorCuentas = new cntrRegistrarCuentas(contexto);
       DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
       Date fecha = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
       String fechaFormateada = sdf.format(fecha);

       // Llama a recogerUsuarioLogado para obtener el usuario logado
       controladorCuentas.recogerUsuarioLogado(new UsuarioCallBack() {
           @Override
           public void onUsuarioRecogido(Usuario usuario) {
               // Se ejecuta cuando se recoge el usuario correctamente
               String nombrePublicador = usuario.getNombre(); // Obtiene el nombre del usuario
               //DatabaseReference nodoId = tareasRef.push();
               //String idTarea = nodoId.getKey();

                              // Crea un mapa con los datos de la nueva tarea
               Map<String, Object> nuevaTarea = new HashMap<>();
              // nuevaTarea.put("IdTarea", idTarea);
               nuevaTarea.put("nombre", nombreTarea);
               nuevaTarea.put("descripcion", descripcion);
               nuevaTarea.put("fecha", fechaFormateada);
               nuevaTarea.put("completada", false);
               nuevaTarea.put("nombre_publicador", nombrePublicador); // Usa el nombre del usuario obtenido


               // Guarda la nueva tarea en la base de datos
               tareasRef.push().setValue(nuevaTarea);

                //Lo redirige a la vista principal nuevamente
               Intent intent = new Intent().setClass(contexto.getApplicationContext(), vistaPrincipal.class);
               Toast.makeText(contexto.getApplicationContext(), "Se ha añadido la tarea correctamente", Toast.LENGTH_SHORT).show();
               contexto.startActivity(intent);
           }
       });
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
                    //String IdTarea = tareaSnapshot.child("IdTarea").getValue(String.class);
                    String nombre = tareaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tareaSnapshot.child("descripcion").getValue(String.class);
                    String fecha= tareaSnapshot.child("fecha").getValue(String.class);
                    String publicador = tareaSnapshot.child("nombre_publicador").getValue(String.class);
                    boolean completado = tareaSnapshot.child("completada").getValue(boolean.class);


                    listaTareas.add(new Tarea(nombre, descripcion,  fecha, publicador, completado));
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


    public void BorrarTarea(String Id) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

        Query consulta = tareasRef.orderByChild("nombre").equalTo(Id);

        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot tarSnapshot: snapshot.getChildren()) {
                        tarSnapshot.getRef().removeValue();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Esto método se asegurará de que el nombre de la tarea sea único
    public void comprobarNombreTarea(String nomTar, NombreTarInt tarea) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
        Query consulta = tareasRef.orderByChild("nombre").equalTo(nomTar);

        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existeTarea = snapshot.exists();
                tarea.onResult(existeTarea);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de base de datos si es necesario
            }
        });
    }

}
