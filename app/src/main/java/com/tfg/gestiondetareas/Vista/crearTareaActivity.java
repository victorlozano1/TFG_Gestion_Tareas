package com.tfg.gestiondetareas.Vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.NombreTarInt;
import com.tfg.gestiondetareas.controlador.cntrTareas;

public class crearTareaActivity extends AppCompatActivity {



    //Declaración de los editText y el botón
    private EditText nombreTarea, descripcionTarea;
    private Button crear;

    String usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clayVistaPrincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        crear.setOnClickListener(v->RecogerTarea());


    }


    //Método que inicializa las variables declaradas
    public void inicializarComponentes()  {

        nombreTarea = findViewById(R.id.edtNombreNuevaTarea);
        descripcionTarea = findViewById(R.id.edtDescripcionTarea);
        crear = findViewById(R.id.btnConfTarea);

    }


    //Metodo que se encargara de comprobar si hay algún valor nulo y lo enviará al controlador
    public void RecogerTarea() {
        if(nombreTarea.getText().toString().isEmpty() || descripcionTarea.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos para enviar la tarea", Toast.LENGTH_LONG).show();
        } else {
            String nombreTar = nombreTarea.getText().toString();
            String descripcionTar = descripcionTarea.getText().toString();
            cntrTareas registrarTarea = new cntrTareas(this);
            registrarTarea.comprobarNombreTarea(nombreTar, new NombreTarInt() {
                @Override
                public void onResult(boolean existeTarea) {
                    if(existeTarea) {
                        Toast.makeText(crearTareaActivity.this, R.string.toastNombreTarUnico, Toast.LENGTH_SHORT).show();
                    } else {
                        registrarTarea.AñadirTarea(nombreTar, descripcionTar);
                    }
                }
            });
        }
    }
}