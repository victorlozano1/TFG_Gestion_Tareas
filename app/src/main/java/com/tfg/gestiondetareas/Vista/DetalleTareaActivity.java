package com.tfg.gestiondetareas.Vista;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tfg.gestiondetareas.R;

public class DetalleTareaActivity extends AppCompatActivity {

    TextView tvDescripcion, tvTituloTarea, tvfechapub, tvnombrepub;
    String nombre, desc, fechapub, nombrepub;

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
}