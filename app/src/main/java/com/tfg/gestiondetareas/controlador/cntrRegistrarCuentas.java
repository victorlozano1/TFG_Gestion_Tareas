package com.tfg.gestiondetareas.controlador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tfg.gestiondetareas.MainActivity;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.registrarCuenta;
import com.tfg.gestiondetareas.vistaPrincipal;

import java.util.HashMap;
import java.util.Map;

public class cntrRegistrarCuentas {



    //Ruta donde se encuentra la base de datos
  public final String urldb = "https://tfggestiondetareas-default-rtdb.europe-west1.firebasedatabase.app/";

  //Ruta donde se encuentra los usuarios
  public final String ruta_usuarios="Usuarios";

    Context contexto;
    private FirebaseAuth auth;
    public cntrRegistrarCuentas(Context contexto) {

        this.contexto=contexto;


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
                            String userId = user.getUid();
                            DatabaseReference userRef = database.getReference(ruta_usuarios).child(userId);
                            userRef.child("Correo").setValue(email);
                            userRef.child("nombre_usuario").setValue(nombreusuario);
                       }

                        Log.i("registroCorrecto", "Se ha registrado el usuario:" + nombreusuario);
                        Intent intent=new Intent().setClass(contexto, MainActivity.class);
                        contexto.startActivity(intent);

                    } else {
                        // Registro fallido
                        Log.i("falloRegistro", "Ha fallado al registrarse un usuario");
                    }
                });

    }

    public void iniciarSesion(String username, String password) {
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Toast.makeText(contexto, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent vistaIntent= new Intent().setClass(contexto, vistaPrincipal.class);
                            contexto.startActivity(vistaIntent);
                        } else {
                            // Inicio de sesión fallido
                            Toast.makeText(contexto, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }






