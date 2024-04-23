package com.tfg.gestiondetareas.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.TareasCallBack;
import com.tfg.gestiondetareas.controlador.cntrTareas;

import java.util.ArrayList;

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
        cntrTareas comrpobarLista = new cntrTareas(this);




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
        cntrTareas tar = new cntrTareas(this);
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

        //Crea un layoutmanager para el reyclcerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        //Inserta el adaptador y el layoutmanager al recyclerView
        rvTareas.setLayoutManager(layoutManager);
        rvTareas.setAdapter(Adaptador);

        //Añade el evento de deslizar para borrar
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtiene la posición del elemento deslizado
                int position = viewHolder.getAdapterPosition();
                cntrTareas tar = new cntrTareas(vistaPrincipal.this);
                Tarea tarDeslizada = listaTareas.get(position);
                String IdTarea = tarDeslizada.getNombre();
                tar.BorrarTarea(IdTarea);


            }


        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTareas);







    }



}