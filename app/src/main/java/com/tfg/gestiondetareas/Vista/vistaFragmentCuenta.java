package com.tfg.gestiondetareas.Vista;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String Usu;
    private String Correo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_cuenta, container, false);
        inicializarComponentes(view);
        return view;
    }

    private void inicializarComponentes(View view) {
        ImagenCuenta = view.findViewById(R.id.imgFotoPerfil);
        ImagenCuenta.setOnClickListener(v -> checkPermission());

        cntrCuentas nombreusu = new cntrCuentas(getContext());
        nombreusu.recogerUsuarioLogado(new UsuarioCallBack() {
            @Override
            public void onUsuarioRecogido(Usuario usuario) {
                Usu = usuario.getNombre();
                Correo = usuario.getCorreo_electronico();
                Nombre_Cuenta = view.findViewById(R.id.tvNombreUsuarioPerfil);
                Nombre_Cuenta.setText(Usu);

                cargarFotoUsuario(Correo);
            }
        });
    }

    private void cargarFotoUsuario(String correo) {
        cntrFotos fotosControlador = new cntrFotos();
        fotosControlador.obtenerFotoUsuario(correo, new cntrFotos.FotoCallBack() {
            @Override
            public void onFotoObtenida(Uri uri) {
                if (isAdded() && getContext() != null) {
                    RequestOptions options = new RequestOptions().circleCrop();
                    Glide.with(getContext()).load(uri).apply(options).into(ImagenCuenta);
                }
            }

            @Override
            public void onFotoNoEncontrada() {
                Uri uri = Uri.parse("android.resource://" + getResources().getResourcePackageName(R.drawable.img) + '/' + getResources().getResourceTypeName(R.drawable.img) + '/' + getResources().getResourceEntryName(R.drawable.img));
                if (isAdded() && getContext() != null) {
                    RequestOptions options = new RequestOptions().circleCrop();
                    Glide.with(getContext()).load(uri).apply(options).into(ImagenCuenta);
                }
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
            } else {
                openFilePicker();
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            } else {
                openFilePicker();
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            cntrFotos fotosControlador = new cntrFotos();
            fotosControlador.subirImagen(Correo, selectedImage);

            if (getContext() != null) {
                RequestOptions options = new RequestOptions().circleCrop();
                Glide.with(getContext()).load(selectedImage).apply(options).into(ImagenCuenta);
            }
        }
    }
}