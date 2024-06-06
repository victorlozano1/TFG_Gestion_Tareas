package com.tfg.gestiondetareas.controlador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Vista.MainActivity;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.Vista.vistaPrincipal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class cntrCuentas {


    //Ruta donde se encuentra la base de datos
    public final String urldb = "https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";

    //Ruta donde se encuentra los usuarios
    public final String ruta_usuarios = "Usuarios";
    Context contexto;
    private FirebaseAuth auth;

    public cntrCuentas() {
    }

    public cntrCuentas(Context contexto) {

        this.contexto = contexto;


    }

    public void registrarnuevoUsuario(String email, String contrasenia, String nombreusuario) {

        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, contrasenia)


                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = auth.getCurrentUser();
                 




                        //Si el usuario es distinto de nulo, entonces se guardarán los datos registrados en la base de datos (salvo la contraseña)
                        if (user != null) {
                            // Guardar la dirección de correo electrónico del usuario en la base de datos
                            FirebaseDatabase database = FirebaseDatabase.getInstance(urldb);
                            DatabaseReference userRef = database.getReference().child(ruta_usuarios);
                            String clave = userRef.push().getKey();
                            Map<String, Object> nuevoUsuario = new HashMap<>();
                            nuevoUsuario.put("Correo", email);
                            nuevoUsuario.put("nombre_usuario", nombreusuario);
                            userRef.child(clave).setValue(nuevoUsuario);
                        }

                        Log.i("registroCorrecto", "Se ha registrado el usuario:" + nombreusuario);

                        Intent intent = new Intent().setClass(contexto, MainActivity.class);
                        contexto.startActivity(intent);

                    } else {

                        //Si se cumple esta condición significa que ya hay un correo electrónico usando la cuenta

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // El correo electrónico ya está en uso por otra cuenta
                            Toast.makeText(contexto, R.string.toastCorreoEnUso, Toast.LENGTH_SHORT).show();

                        }

                        // Registro fallido
                        Log.i("falloRegistro", "Ha fallado al registrarse un usuario");
                    }
                });

    }

    public void iniciarSesion(String username, String password) {
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    //Si se cumple la condición, se inicia la sesión
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        Toast.makeText(contexto, R.string.toastInicioSesionExitoso, Toast.LENGTH_SHORT).show();
                        Intent vistaIntent = new Intent().setClass(contexto, vistaPrincipal.class);
                        contexto.startActivity(vistaIntent);
                    } else {
                        // Inicio de sesión fallido
                        Toast.makeText(contexto, R.string.toastErrorInicioSesion, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    //Método que recoge el usuario que esta logado dentro de la Aplicación
    public void recogerUsuarioLogado(UsuarioCallBack callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser usuario = auth.getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_usuarios);

        if (usuario != null) {
            String correo = usuario.getEmail();
            Query consulta = db.orderByChild("Correo").equalTo(correo);

            consulta.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String nombre = dataSnapshot.child("nombre_usuario").getValue(String.class);
                        Usuario usuarioLogado = new Usuario(nombre, correo);
                        callback.onUsuarioRecogido(usuarioLogado);
                        return;
                    }
                    consulta.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
                }
            });
        } else {
            Log.i("ErrorAU", "No hay usuario autenticado");
        }
    }

    public void editarNombre(String correo, String nombreNuevo) {
        DatabaseReference db = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_usuarios);
        Query consulta = db.orderByChild("Correo").equalTo(correo);
        cntrTareas actualizarnombre = new cntrTareas();

        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Obtener la fecha actual
                        Date fechaActual = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaCambio = sdf.format(fechaActual);

                        // Crear un mapa con las actualizaciones
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("nombre_usuario", nombreNuevo);
                        updates.put("fecha_ultimo_cambio", fechaCambio);

                        // Realizar la actualización en Firebase Database
                        dataSnapshot.getRef().updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("EditarNombre", "Usuario actualizado exitosamente: " + correo);
                                    actualizarnombre.actualizarTareasNombreNuevo(nombreNuevo, dataSnapshot.child("nombre_usuario").getValue(String.class));
                                } else {
                                    Log.e("EditarNombre", "Error actualizando el usuario", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.i("EditarNombre", "No se encontró el usuario con el correo: " + correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ErrorLecturaBD", "Error al leer datos: " + error.getMessage());
            }
        });
    }

    public void obtenerFechaUltimoCambio(String correo, FechaCambioCallback callback) {
        DatabaseReference db = FirebaseDatabase.getInstance(urldb).getReference().child(ruta_usuarios);
        Query consulta = db.orderByChild("Correo").equalTo(correo);
        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String fechaUltimoCambio = dataSnapshot.child("fecha_ultimo_cambio").getValue(String.class);
                    callback.onFechaObtenida(fechaUltimoCambio);
                }
                consulta.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
    public boolean esCambioPermitido(String fechaUltimoCambio) {
        if (fechaUltimoCambio == null || fechaUltimoCambio.isEmpty()) {
            return true; // Permitir el cambio si no hay registro de un cambio anterior
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaActual = new Date();
            Date fechaUltima = sdf.parse(fechaUltimoCambio);

            long diferenciaMilisegundos = fechaActual.getTime() - fechaUltima.getTime();
            long diferenciaDias = diferenciaMilisegundos / (1000 * 60 * 60 * 24);

            return diferenciaDias >= 1; // Permitir el cambio si ha pasado al menos un día
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // En caso de error, no permitir el cambio
        }
    }
    public interface FechaCambioCallback {
        void onFechaObtenida(String fechaUltimoCambio);
        void onError(DatabaseError error);
    }

    }












