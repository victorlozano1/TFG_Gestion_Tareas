package com.tfg.gestiondetareas.controlador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.Vista.TareaAdapter;
import com.tfg.gestiondetareas.Vista.vistaPrincipal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class cntrTareas {

public Context contexto;


    private cntrCuentas controladorCuentas;

    public cntrTareas() {
    }

    public cntrTareas(Context contexto) {
    this.contexto = contexto;
}



   private final String urldb="https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";
   private final String ruta_tarea="Tareas";

   private ValueEventListener listenerPorDefecto, listenerFiltro;


   //Este método añadira la tarea a la base de datos
   public void AñadirTarea(String nombreTarea, String descripcion, String tipoTarea) {
       controladorCuentas = new cntrCuentas(contexto);
       FirebaseDatabase database = FirebaseDatabase.getInstance(urldb);
       DatabaseReference tareasRef = database.getReference().child(ruta_tarea);
       Date fecha = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
       String fechaFormateada = sdf.format(fecha);

       // Llama a recogerUsuarioLogado para obtener el usuario logado
       controladorCuentas.recogerUsuarioLogado(new UsuarioCallBack() {
           @Override
           public void onUsuarioRecogido(Usuario usuario) {
               // Se ejecuta cuando se recoge el usuario correctamente
               String nombrePublicador = usuario.getNombre(); // Obtiene el nombre del usuario
               // Crea un mapa con los datos de la nueva tarea
               Map<String, Object> nuevaTarea = new HashMap<>();
               nuevaTarea.put("nombre", nombreTarea);
               String clave = tareasRef.push().getKey();
               nuevaTarea.put("descripcion", descripcion);
               nuevaTarea.put("fecha", fechaFormateada);
               nuevaTarea.put("Tipo_Tarea", tipoTarea);
               nuevaTarea.put("completada", false);
               nuevaTarea.put("nombre_publicador", nombrePublicador); // Usa el nombre del usuario obtenido

               // Guarda la nueva tarea en la base de datos
               tareasRef.child(clave).setValue(nuevaTarea);
               // Tarea añadida correctamente
               Toast.makeText(contexto.getApplicationContext(), R.string.ToastTareaNueva, Toast.LENGTH_SHORT).show();
           }
       });
   }
 //Metodo que recogerá todas las tareas almacenadas en la base de datos y lo retornará como lista
    public void retornarListaTareas(TareasCallBack callback) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

        listenerPorDefecto = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Tarea> listaTareas = new ArrayList<>();
                for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {

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
        };

        tareasRef.addValueEventListener(listenerPorDefecto);


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
                    consulta.removeEventListener(this);
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
                consulta.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public boolean esPropietario(String usuarioLogado, String PropietarioTarea) {
       return usuarioLogado.equals(PropietarioTarea);
    }


    public void EditarTarea(String id, String nuevaDesc){

        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
        Query query = tareasRef.orderByChild("nombre").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> updates = new HashMap<>();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        String key = childSnapshot.getKey();
                        String rutaCompleta = ruta_tarea + "/" + key + "/descripcion";
                        updates.put(rutaCompleta, nuevaDesc);
                    }

                    // Realizar la actualización en Firebase Database
                    FirebaseDatabase.getInstance(urldb).getReference().updateChildren(updates);


            }

                query.removeEventListener(this);


        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

    });

}

 public String TraducirTipoTarea(String TipoTarea) {
     switch(TipoTarea) {
         case "Domestic":
             TipoTarea = "Domésticas";
             break;
         case "Job":
             TipoTarea = "Trabajo";
             break;
         case "Leisure":
             TipoTarea = "Ocio";
             break;
         default:
             Log.i("infoIdioma", "El usuario tiene el idioma en español");

     }

     return TipoTarea;
 }


 public void FiltrarPorTipoTarea(String TareaTipo, TareasCallBack callback) {

     DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
     Query query = tareasRef.orderByChild("Tipo_Tarea").equalTo(TareaTipo);

     listenerFiltro = new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {

             ArrayList<Tarea> listaTareas = new ArrayList<>();
             for (DataSnapshot tareaSnapshot : snapshot.getChildren()) {

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
         public void onCancelled(@NonNull DatabaseError error) {

         }
     };

     query.addValueEventListener(listenerFiltro);




   }

   //Método que gestionará los listeners de la base de datos
   public void removerEventListener(int listenerARemover) {

       if(listenerARemover==1) {
           if(listenerPorDefecto!=null) {
               DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
               tareasRef.removeEventListener(listenerPorDefecto);
               Log.i("TagListener", "Se ha borrado el filtro correctamente");
           }

       }

       if(listenerARemover==2) {
           if(listenerFiltro!=null) {
               DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
               tareasRef.removeEventListener(listenerFiltro);
               Log.i("TagListener", "Se ha borrado el filtro correctamente");
           }
       }

   }


}









