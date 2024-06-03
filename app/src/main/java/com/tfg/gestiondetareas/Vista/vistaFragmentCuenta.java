package com.tfg.gestiondetareas.Vista;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.UsuarioCallBack;
import com.tfg.gestiondetareas.controlador.cntrCuentas;
import com.tfg.gestiondetareas.controlador.cntrFotos;

public class vistaFragmentCuenta extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_PICK_FILE = 2;

    private ImageView ImagenCuenta;

    private TextView Nombre_Cuenta;
    private View view;

    private String Usu;

    private String Correo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_cuenta, container, false);

        inicializarComponentes();
        return view;
    }

    private void inicializarComponentes() {
        //ImageView que contiene la foto de perfil del usuario
        ImagenCuenta = view.findViewById(R.id.imgFotoPerfil);
        ImagenCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        cntrCuentas nombreusu = new cntrCuentas(getContext());
        nombreusu.recogerUsuarioLogado(new UsuarioCallBack() {
            @Override
            public void onUsuarioRecogido(Usuario usuario) {
                Usu = usuario.getNombre();
                Correo = usuario.getCorreo_electronico();
                Nombre_Cuenta = view.findViewById(R.id.edtNombreUsuarioPerfil);
                Nombre_Cuenta.setText(Usu);

                // Obtener la foto del usuario
                cntrFotos fotosControlador = new cntrFotos();
                fotosControlador.obtenerFotoUsuario(Correo, new cntrFotos.FotoCallBack() {
                    @Override
                    public void onFotoObtenida(Uri uri) {
                        // Cargar la foto del usuario
                        RequestOptions options = new RequestOptions()
                                .circleCrop();
                        Glide.with(getContext())
                                .load(uri)
                                .apply(options)
                                .into(ImagenCuenta);
                    }


                    //En caso de no haber encontrado una ruta relacionada con el correo del usuario, cargará una foto de perfil por defecto

                    @Override
                    public void onFotoNoEncontrada() {


                        Uri uri =    Uri.parse(
                                "android.resource://"
                                        + getResources().getResourcePackageName(R.drawable.img)
                                        + '/'
                                        + getResources().getResourceTypeName(R.drawable.img)
                                        + '/'
                                        + getResources().getResourceEntryName(R.drawable.img)
                        );

                        RequestOptions options = new RequestOptions()
                                .circleCrop();
                        Glide.with(getContext())
                                .load(uri)
                                .apply(options)
                                .into(ImagenCuenta);

                    }
                });
            }
        });
    }



    //Método que comprobará los permisos del usuario
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    //Método que permitirá al usuario abrir su galeria para seleccionar la foto de perfil
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    //Método que se ejecutará cuando el usuario quiera seleccionar una foto
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            cntrFotos fotosControlador = new cntrFotos();
            //Se guardará la foto en la base de datos
            fotosControlador.subirImagen(Correo, selectedImage);

            //Hacer que la foto de perfil se vea de forma circular
            RequestOptions options = new RequestOptions()
                    .circleCrop();
            Glide.with(getContext())
                    .load(selectedImage)
                    .apply(options)
                    .into(ImagenCuenta);
        }
    }
}