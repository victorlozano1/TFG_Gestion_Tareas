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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.DatabaseError;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.UsuarioCallBack;
import com.tfg.gestiondetareas.controlador.cntrCuentas;
import com.tfg.gestiondetareas.controlador.cntrFotos;
import com.tfg.gestiondetareas.controlador.cntrTareas;

import java.text.SimpleDateFormat;
import java.util.Date;

public class vistaFragmentCuenta extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_PICK_FILE = 2;

    private ImageView ImagenCuenta;
    private EditText Nombre_Cuenta;
    private String Usu;
    private String Correo;
    private ImageButton btnEditar;
    private Button btnAplicarCambios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_cuenta, container, false);
        inicializarComponentes(view);
        return view;
    }

    private void inicializarComponentes(View view) {
        ImagenCuenta = view.findViewById(R.id.imgFotoPerfil);
        btnEditar = view.findViewById(R.id.btnEditarNombreCuenta);
        btnEditar.setOnClickListener(v -> editarNombre());
        btnAplicarCambios = view.findViewById(R.id.btnAplicarCambios);
        btnAplicarCambios.setOnClickListener(v -> aplicarCambios());
        ImagenCuenta.setOnClickListener(v -> checkPermission());

        cntrCuentas nombreusu = new cntrCuentas(getContext());
        nombreusu.recogerUsuarioLogado(new UsuarioCallBack() {
            @Override
            public void onUsuarioRecogido(Usuario usuario) {
                Usu = usuario.getNombre();
                Correo = usuario.getCorreo_electronico();
                Nombre_Cuenta = view.findViewById(R.id.edtNombreUsuarioPerfil);
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

    private void editarNombre() {
        cntrCuentas cuenta = new cntrCuentas(getContext());
        cuenta.obtenerFechaUltimoCambio(Correo, new cntrCuentas.FechaCambioCallback() {
            @Override
            public void onFechaObtenida(String fechaUltimoCambio) {
                if (cuenta.esCambioPermitido(fechaUltimoCambio)) {
                    Nombre_Cuenta.setEnabled(true);
                    btnAplicarCambios.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), R.string.ToastEsFechaHoy, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(getContext(), "Error al verificar la fecha del último cambio.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aplicarCambios() {
        String nuevoNombre = Nombre_Cuenta.getText().toString();

        if (nuevoNombre.isEmpty()) {
            Toast.makeText(getContext(), R.string.toastEditarNombreVacio, Toast.LENGTH_SHORT).show();
        } else {
            cntrCuentas cuenta = new cntrCuentas(getContext());
            cuenta.editarNombre(Correo, nuevoNombre);

            Nombre_Cuenta.setEnabled(false);
            btnAplicarCambios.setVisibility(View.INVISIBLE);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            openFilePicker();
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

            if (isAdded() && getContext() != null) {
                RequestOptions options = new RequestOptions().circleCrop();
                Glide.with(getContext()).load(selectedImage).apply(options).into(ImagenCuenta);
            }
        }
    }
}