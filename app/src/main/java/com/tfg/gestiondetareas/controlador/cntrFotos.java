package com.tfg.gestiondetareas.controlador;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class cntrFotos {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public cntrFotos() {
        //Referencia a la base de datos de las fotos
        storage = FirebaseStorage.getInstance("gs://tfggestiondetareas.appspot.com");
        storageReference = storage.getReference();
    }


    //Método que recogerá la foto almacenada por el usuario usuario
    public void obtenerFotoUsuario(String correo, final FotoCallBack callBack) {

        //Reemplaza los puntos por un _
        String sanitizedCorreo = correo.replace(".", "_");

        //Consulta a la ruta donde esta guardada la foto de perfil
        String path = "fotos_perfil/" + sanitizedCorreo;
        StorageReference photoRef = storageReference.child(path);


        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            callBack.onFotoObtenida(uri);
        }).addOnFailureListener(exception -> {
            Log.i("fotoSinEncontrar", "No se ha encontrado una foto, utilizando foto por defecto");
            callBack.onFotoNoEncontrada();
        });
    }




    //Método que se encargará de subir una imagen a la base de datos cuando el usuario la haya seleccionado
    public void subirImagen(String correo, Uri imagenUri) {
        String sanitizedCorreo = correo.replace(".", "_");
        String path = "fotos_perfil/" + sanitizedCorreo;
        StorageReference photoRef = storageReference.child(path);

        photoRef.putFile(imagenUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i("TagFotoSubida", "Se ha subido la foto correctamente");

                })
                .addOnFailureListener(exception -> {
                    Log.e("TagFotoSubida", "Hubo un problema al subir la foto");


                });
    }



    //Interfaz que funcionará como callback para las consultas y updates hacia la base de datos
    public interface FotoCallBack {
        void onFotoObtenida(Uri uri);

        void onFotoNoEncontrada();
    }
}


