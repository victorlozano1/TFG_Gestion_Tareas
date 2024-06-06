package com.tfg.gestiondetareas.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.TareasCallBack;
import com.tfg.gestiondetareas.controlador.cntrTareas;

import java.util.ArrayList;

public class vistaFragmentTareas extends Fragment {
    View view;
    FloatingActionButton btnAniadir;
    RecyclerView rvTareas;

    ArrayList<Tarea> listaTareas;
    TareaAdapter Adaptador;
    private MaterialToolbar ToolbarMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_tareas, container, false);

        inicializarComponentes();
        mostrarRecyclerView();

        //Evento para añadir una nueva tarea a la lista compartida
        btnAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(view.getContext(), crearTareaActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }


    public void inicializarComponentes() {
        btnAniadir = view.findViewById(R.id.btnAniadir);
        rvTareas = view.findViewById(R.id.rvTarea);
        ToolbarMenu = view.findViewById(R.id.toolBarMenu);
        ((AppCompatActivity) getActivity()).setSupportActionBar(ToolbarMenu);

        setHasOptionsMenu(true); // Indicar que el fragmento tiene un menú propio


        cntrTareas tar = new cntrTareas(view.getContext());
        tar.retornarListaTareas(new TareasCallBack() {
            @Override
            public void onTareasLoaded(ArrayList<Tarea> tareas) {
                listaTareas = tareas;
                Adaptador = new TareaAdapter(listaTareas, view.getContext());
                mostrarRecyclerView();
            }




        });
    }

    public void mostrarRecyclerView() {

        //Crea un layoutmanager para el reyclcerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

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
                cntrTareas tar = new cntrTareas(view.getContext());
                Tarea tarDeslizada = listaTareas.get(position);
                String IdTarea = tarDeslizada.getNombre();
                tar.BorrarTarea(IdTarea);


            }


        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTareas);







    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vista_tareas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.opcMenuTrabajo){
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();
            consulta.FiltrarPorTipoTarea(TipoTarea, new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }
            });
        }
        if(item.getItemId()==R.id.opcMenuDomestico){
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();
            consulta.FiltrarPorTipoTarea(TipoTarea, new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }
            });
        }
        if(item.getItemId()==R.id.opcMenuOcio){
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();
            consulta.FiltrarPorTipoTarea(TipoTarea, new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }
            });
        }

        if(item.getItemId()==R.id.opcNoFiltrar) {
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();
            consulta.retornarListaTareas(new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }
            });
        }



        return super.onOptionsItemSelected(item);

    }
}

