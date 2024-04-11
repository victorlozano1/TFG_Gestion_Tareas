package com.tfg.gestiondetareas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.Vista.TareaAdapter;
import com.tfg.gestiondetareas.Vista.crearTareaActivity;
import com.tfg.gestiondetareas.controlador.TareasCallBack;
import com.tfg.gestiondetareas.controlador.cntrTareas;

import java.util.ArrayList;
import java.util.Date;

public class vistaPrincipal extends AppCompatActivity {


    FloatingActionButton btnAniadir;
    RecyclerView rvTareas;

    ArrayList<Tarea> listaTareas;
    TareaAdapter Adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        cntrTareas comrpobarLista = new cntrTareas();


        boolean vacia = comrpobarLista.comprobarListaVacia();
        mostrarRecyclerView();


        //Evento para añadir una nueva tarea a la lista compartida
        btnAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(vistaPrincipal.this, crearTareaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void inicializarComponentes() {
        btnAniadir=findViewById(R.id.btnAniadir);
        rvTareas=findViewById(R.id.rvTarea);
        cntrTareas tar = new cntrTareas();
        tar.retornarListaTareas(new TareasCallBack() {
            @Override
            public void onTareasLoaded(ArrayList<Tarea> tareas) {
                listaTareas = tareas;
                Adaptador = new TareaAdapter(listaTareas, vistaPrincipal.this);
                mostrarRecyclerView();
            }
        });
    }

    public void mostrarRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        rvTareas.setLayoutManager(layoutManager);
        rvTareas.setAdapter(Adaptador);

    }



}