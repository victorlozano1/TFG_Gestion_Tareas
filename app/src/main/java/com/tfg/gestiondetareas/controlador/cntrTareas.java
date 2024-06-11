package com.tfg.gestiondetareas.controlador;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    private final String urldb = "https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";
    private final String ruta_tarea = "Tareas";

    private ValueEventListener listenerPorDefecto;

    //Este método añadirá la tarea a la base de datos
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
                String correoPublicador = usuario.getCorreo_electronico();
                // Crea un mapa con los datos de la nueva tarea
                Map<String, Object> nuevaTarea = new HashMap<>();
                nuevaTarea.put("nombre", nombreTarea);
                String clave = tareasRef.push().getKey();
                nuevaTarea.put("descripcion", descripcion);
                nuevaTarea.put("fecha", fechaFormateada);
                nuevaTarea.put("Tipo_Tarea", tipoTarea);
                nuevaTarea.put("completada", false);
                nuevaTarea.put("nombre_publicador", nombrePublicador); // Usa el nombre del usuario obtenido
                nuevaTarea.put("correo_publicador", correoPublicador); // Usa el correo del usuario obtenido

                // Guarda la nueva tarea en la base de datos
                tareasRef.child(clave).setValue(nuevaTarea);
                // Tarea añadida correctamente
                Toast.makeText(contexto, R.string.ToastTareaNueva, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método que recogerá todas las tareas almacenadas en la base de datos y lo retornará como lista
    public void retornarListaTareas(ListenersCallBack listener, TareasCallBack callback) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
        // Remover el listener anterior si existe
        if (listenerPorDefecto != null) {
            tareasRef.removeEventListener(listenerPorDefecto);
            Log.i("ListenerBorrado", "Listener borrado correctamente");
        }

        // Definir el nuevo listener
        listenerPorDefecto = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Tarea> listaTareas = new ArrayList<>();
                for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = tareaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tareaSnapshot.child("descripcion").getValue(String.class);
                    String fecha = tareaSnapshot.child("fecha").getValue(String.class);
                    String publicador = tareaSnapshot.child("nombre_publicador").getValue(String.class);
                    String TareaTipo = tareaSnapshot.child("Tipo_Tarea").getValue(String.class);
                    String correo_publicador = tareaSnapshot.child("correo_publicador").getValue(String.class);
                    boolean completado = tareaSnapshot.child("completada").getValue(boolean.class);

                    listaTareas.add(new Tarea(nombre, descripcion, fecha, publicador, completado, TareaTipo, correo_publicador));
                }
                callback.onTareasLoaded(listaTareas);
                listener.listenerObtenido(listenerPorDefecto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.i("ErrorLecturaBD", "Error al leer datos: " + databaseError.getMessage());
            }
        };

        // Agregar el nuevo listener
        tareasRef.addValueEventListener(listenerPorDefecto);
    }


    public void BorrarTarea(String Id) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);

        Query consulta = tareasRef.orderByChild("nombre").equalTo(Id);

        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot tarSnapshot : snapshot.getChildren()) {
                        tarSnapshot.getRef().removeValue();
                    }
                    consulta.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
            }
        });
    }

    // Esto método se asegurará de que el nombre de la tarea sea único
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
                Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
            }
        });
    }

    public boolean esPropietario(String usuarioLogado, String PropietarioTarea) {
        return usuarioLogado.equals(PropietarioTarea);
    }



    public void EditarTarea(String id, String nuevaDesc) {
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
                Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
            }
        });
    }

    public String TraducirTipoTarea(String TipoTarea) {
        switch (TipoTarea) {
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


    //Método que ordenará por el tipo de tarea
    public void OrdenarPorTipoTarea(String TareaTipo, ArrayList<Tarea> Tarea, TareaAdapter adapter) {

        if(Tarea!=null) {

            Collections.sort(Tarea, (tarea1, tarea2) -> {
                String tipo1 = tarea1.getTipoTarea();
                String tipo2 = tarea2.getTipoTarea();

                if (tipo1.equals(TareaTipo) && !tipo2.equals(TareaTipo)) {
                    return -1;
                } else if (!tipo1.equals(TareaTipo) && tipo2.equals(TareaTipo)) {
                    return 1;
                } else {
                    return 0;
                }
            });

        }

        adapter.notifyDataSetChanged();


    }

    // Método que gestionará los listeners de la base de datos
    public void removerEventListener(ValueEventListener listener) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
        if(listener!=null) {
            tareasRef.removeEventListener(listener);
            Log.i("ListenerEliminado", "El listener se ha eliminado correctamente");
        }

    }

    public void marcarTareaComoCompleta(boolean completa, String IdTar) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_tarea);
        Query query = tareasRef.orderByChild("nombre").equalTo(IdTar);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> updates = new HashMap<>();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        String rutaCompleta = ruta_tarea + "/" + key + "/completada";
                        updates.put(rutaCompleta, completa);
                    }

                    // Realizar la actualización en Firebase Database
                    FirebaseDatabase.getInstance(urldb).getReference().updateChildren(updates);
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
            }
        });
    }



    public void ordenarPorCompletadas(ArrayList<Tarea> Lista, TareaAdapter adapter) {


        Collections.sort(Lista, new Comparator<Tarea>() {
            @Override
            public int compare(Tarea tarea1, Tarea tarea2) {
                // Si la tarea1 está completada y la tarea2 no lo está, colocamos tarea1 primero
                if (tarea1.isCompletada() && !tarea2.isCompletada()) {
                    return -1;
                }
                // Si la tarea2 está completada y la tarea1 no lo está, colocamos tarea2 primero
                else if (!tarea1.isCompletada() && tarea2.isCompletada()) {
                    return 1;
                }
                // En cualquier otro caso, no hay cambio en el orden
                else {
                    return 0;
                }
            }
        });
        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();


    }
}
