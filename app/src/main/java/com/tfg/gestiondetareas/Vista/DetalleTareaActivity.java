package com.tfg.gestiondetareas.Vista;

import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tfg.gestiondetareas.Modelo.Usuario;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.UsuarioCallBack;
import com.tfg.gestiondetareas.controlador.cntrCuentas;
import com.tfg.gestiondetareas.controlador.cntrTareas;

public class DetalleTareaActivity extends AppCompatActivity {

    TextView tvDescripcion, tvTituloTarea, tvfechapub, tvnombrepub;
    String nombre, desc, fechapub, nombrepub;

    private Button btnEditarDesc;
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
        btnEditarDesc.setOnClickListener(v->aplicarCambiosTarea());
        comprobarPropTarea();

        nombre = getIntent().getStringExtra("NombreTareaDetalle");
        desc = getIntent().getStringExtra("DescripcionTareaDetalle");
        fechapub = getIntent().getStringExtra("FechaPublicacionDetalle");
        nombrepub = getIntent().getStringExtra("NombrePublicadorDetalle");


        //Inserta los textos
        tvDescripcion.setText(desc);
        tvTituloTarea.setText(nombre);
        tvfechapub.setText(fechapub);
        tvnombrepub.setText(nombrepub);

    }

    private void comprobarPropTarea() {
        cntrCuentas usuarioLogado = new cntrCuentas();
        cntrTareas prop = new cntrTareas();

        usuarioLogado.recogerUsuarioLogado(new UsuarioCallBack() {
            @Override
            public void onUsuarioRecogido(Usuario usuario) {
                String nombre_usuario = usuario.getNombre();

              boolean esProp = prop.esPropietario(nombre_usuario, tvnombrepub.getText().toString());

              if(esProp) {
                  btnEditarDesc.setEnabled(true);
                  tvDescripcion.setEnabled(true);
              }

              else {
                  btnEditarDesc.setEnabled(false);
                  tvDescripcion.setEnabled(false);
              }

            }
        });
    }

    private void aplicarCambiosTarea() {

       String nuevaDesc = tvDescripcion.getText().toString();

       if(nuevaDesc.isEmpty()) {
           Toast.makeText(this, R.string.ToastDescVacio, Toast.LENGTH_LONG).show();
       }

       else {
           cntrTareas editarDesc = new cntrTareas();

           editarDesc.EditarTarea(tvTituloTarea.getText().toString(), nuevaDesc);
           Toast.makeText(this, R.string.ToastDescModificada, Toast.LENGTH_SHORT).show();
       }
    }


}
