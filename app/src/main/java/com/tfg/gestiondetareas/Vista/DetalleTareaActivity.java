package com.tfg.gestiondetareas.Vista;

import static java.security.AccessController.getContext;

import android.health.connect.datatypes.units.Length;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.NombreTarInt;
import com.tfg.gestiondetareas.controlador.UsuarioCallBack;
import com.tfg.gestiondetareas.controlador.cntrCuentas;
import com.tfg.gestiondetareas.controlador.cntrFotos;
import com.tfg.gestiondetareas.controlador.cntrTareas;

public class DetalleTareaActivity extends AppCompatActivity {

    private TextView tvDescripcion, tvTituloTarea, tvfechapub, tvnombrepub;
    private String nombre, desc, fechapub, nombrepub, correo_publicador;

    private Button btnEditarDesc;

    private ImageButton btnEditar;

    private CheckBox chCompleta;

    private ImageView ImagenCuenta;
    private boolean completada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clayVistaPrincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarComponentes();
    }

    public void inicializarComponentes() {

        //Inicializa los textView y las cadenas
        tvDescripcion=findViewById(R.id.tvDescripcionTareaDetalle);
        tvTituloTarea=findViewById(R.id.tvNombreTarea);
        tvfechapub = findViewById(R.id.tvFechaPub);
        tvnombrepub = findViewById(R.id.tvPublicadorDato);

        btnEditarDesc = findViewById(R.id.btnEditarTarea);
        btnEditar = findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditarDesc.setVisibility(View.VISIBLE);
                chCompleta.setEnabled(true);
                tvDescripcion.setEnabled(true);
            }
        });
        btnEditarDesc.setOnClickListener(v->aplicarCambiosTarea());
        chCompleta = findViewById(R.id.chCompletada);
        ImagenCuenta = findViewById(R.id.imgPfp);
        correo_publicador = getIntent().getStringExtra("Correo_publicador");
        comprobarPropTarea();


        nombre = getIntent().getStringExtra("NombreTareaDetalle");
        desc = getIntent().getStringExtra("DescripcionTareaDetalle");
        fechapub = getIntent().getStringExtra("FechaPublicacionDetalle");
        nombrepub = getIntent().getStringExtra("NombrePublicadorDetalle");
        completada = getIntent().getBooleanExtra("Completada", false);
        CargarFotoPublicador();


        //Inserta los textos
        tvDescripcion.setText(desc);
        tvTituloTarea.setText(nombre);
        tvfechapub.setText(fechapub);
        tvnombrepub.setText(nombrepub);

    }


    //Método que compara si el usuario logado es el propietario de una tarea en especifico
    private void comprobarPropTarea() {
        cntrCuentas usuarioLogado = new cntrCuentas();
        cntrTareas prop = new cntrTareas();

        usuarioLogado.recogerUsuarioLogado(new UsuarioCallBack() {
            @Override
            public void onUsuarioRecogido(Usuario usuario) {
                String correo_usuario = usuario.getCorreo_electronico();


              boolean esProp = prop.esPropietario(correo_usuario, correo_publicador);

              if(esProp) {

                  //Comprueba si esta consultando una tarea que esta completada, para que no pueda editarla nuevamente
                  if(completada) {
                      btnEditar.setVisibility(View.INVISIBLE);
                      chCompleta.setVisibility(View.INVISIBLE);
                      btnEditarDesc.setVisibility(View.INVISIBLE);
                      findViewById(R.id.tvCompletado).setVisibility(View.VISIBLE);

                  }

                  else {

                      btnEditar.setVisibility(View.VISIBLE);
                      chCompleta.setVisibility(View.VISIBLE);
                      btnEditarDesc.setVisibility(View.INVISIBLE);


                  }


               }
                 else {
                  btnEditar.setVisibility(View.INVISIBLE);
                  btnEditarDesc.setVisibility(View.INVISIBLE);
                  chCompleta.setVisibility(View.INVISIBLE);
                  tvDescripcion.setEnabled(false);

              }
            }
        });

    }










    //Método que aplicara los cambios que se hayan hecho en la descripción

    private void aplicarCambiosTarea() {

       String nuevaDesc = tvDescripcion.getText().toString();

       if(nuevaDesc.isEmpty()) {
           Toast.makeText(this, R.string.ToastDescVacio, Toast.LENGTH_LONG).show();
       }

       else {
           cntrTareas editar= new cntrTareas();

           editar.EditarTarea(tvTituloTarea.getText().toString(), nuevaDesc);

           if(chCompleta.isChecked()) {
               btnEditar.setVisibility(View.INVISIBLE);
               findViewById(R.id.tvCompletado).setVisibility(View.VISIBLE);
               boolean completo = chCompleta.isChecked();
               editar.marcarTareaComoCompleta(completo, tvTituloTarea.getText().toString());
           }

           Toast.makeText(this, R.string.ToastDescModificada, Toast.LENGTH_SHORT).show();
           btnEditarDesc.setVisibility(View.INVISIBLE);
           chCompleta.setEnabled(false);
           tvDescripcion.setEnabled(false);
       }
    }

    private void CargarFotoPublicador() {
        cntrFotos foto = new cntrFotos();
        foto.obtenerFotoUsuario(correo_publicador, new cntrFotos.FotoCallBack() {
            @Override
            public void onFotoObtenida(Uri uri) {

                    RequestOptions options = new RequestOptions().circleCrop();
                    Glide.with(DetalleTareaActivity.this).load(uri).apply(options).into(ImagenCuenta);

            }

            @Override
            public void onFotoNoEncontrada() {
                Uri uri = Uri.parse("android.resource://" + getResources().getResourcePackageName(R.drawable.img) + '/' + getResources().getResourceTypeName(R.drawable.img) + '/' + getResources().getResourceEntryName(R.drawable.img));

                    RequestOptions options = new RequestOptions().circleCrop();
                    Glide.with(DetalleTareaActivity.this).load(uri).apply(options).into(ImagenCuenta);
                }

        });
    }


}
