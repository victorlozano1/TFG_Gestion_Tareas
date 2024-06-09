package com.tfg.gestiondetareas.Vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ValueEventListener;
import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.ListenersCallBack;
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

    private SharedPreferences preferences;

    private boolean filtroGuardado;

    private ValueEventListener listenerglobal;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_tareas, container, false);

        inicializarComponentes();
        mostrarRecyclerView();

        return view;

    }


    //Metodo que inicializa las variables globales
    public void inicializarComponentes() {
        btnAniadir = view.findViewById(R.id.btnAniadir);
        rvTareas = view.findViewById(R.id.rvTarea);
        ToolbarMenu = view.findViewById(R.id.toolBarMenu);
        //Evento para añadir una nueva tarea a la lista compartida
        btnAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(view.getContext(), crearTareaActivity.class);
                startActivity(intent);
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(ToolbarMenu);

        setHasOptionsMenu(true); // Indicar que el fragmento tiene un menú propio

        //Recoge las default sharedpreferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Inicializa una variable que recoge si la opción esta en true o false
         filtroGuardado = preferences.getBoolean("filtrado_activo", false);

        if(filtroGuardado) {

            String filtro_seleccionado = preferences.getString("filtroguardado", "SinFiltro");

            if(filtro_seleccionado.equals("SinFiltro")) {
                Log.i("sinTarea", "El usuario no tenia filtrado por ningun tipo de tarea");
            }

            else {
                RecuperarFiltro(filtro_seleccionado);
            }


        }
        else {
            cntrTareas tar = new cntrTareas(view.getContext());
            tar.retornarListaTareas(new ListenersCallBack() {
                @Override
                public void listenerObtenido(ValueEventListener listener) {

                }
            }, new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }


            });
        }
    }

    private void RecuperarFiltro(String filtro) {

        //Comprobar si el usuario decidió no filtrar
        if(filtro.equals("No filtrar") || filtro.equals("Do not filter")) {
            cntrTareas consulta = new cntrTareas();
            consulta.retornarListaTareas(new ListenersCallBack() {
                @Override
                public void listenerObtenido(ValueEventListener listener) {

                }
            }, new TareasCallBack() {
                @Override
                public void onTareasLoaded(ArrayList<Tarea> tareas) {
                    listaTareas = tareas;
                    Adaptador = new TareaAdapter(listaTareas, view.getContext());
                    mostrarRecyclerView();
                }
            });
        }

        else {


            cntrTareas consulta = new cntrTareas();

            Log.i("StringFiltro", filtro);

            if (filtro.equals("Completada") || filtro.equals("Completed")) {
                consulta.retornarListaTareas(new ListenersCallBack() {
                    @Override
                    public void listenerObtenido(ValueEventListener listener) {

                    }
                }, new TareasCallBack() {
                    @Override
                    public void onTareasLoaded(ArrayList<Tarea> tareas) {
                        listaTareas = tareas;
                        Adaptador = new TareaAdapter(listaTareas, view.getContext());
                        mostrarRecyclerView();
                        consulta.ordenarPorCompletadas(listaTareas,Adaptador);
                    }
                });

            }
            else {

                consulta.retornarListaTareas(new ListenersCallBack() {
                    @Override
                    public void listenerObtenido(ValueEventListener listener) {

                    }
                }, new TareasCallBack() {
                    @Override
                    public void onTareasLoaded(ArrayList<Tarea> tareas) {
                        listaTareas = tareas;
                        Adaptador = new TareaAdapter(listaTareas, view.getContext());
                        mostrarRecyclerView();
                        consulta.OrdenarPorTipoTarea(filtro, listaTareas, Adaptador);
                    }
                });



            }

        }
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

    //Gestiona las opciones del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.opcMenuTrabajo){

            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();



            //Guardo el string en las sharedpreferences si esta guardado si el usuario tiene activada la opción
            if(filtroGuardado) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filtroguardado", TipoTarea);
                editor.apply();
            }

            consulta.OrdenarPorTipoTarea(TipoTarea, listaTareas, Adaptador);

        }
        if(item.getItemId()==R.id.opcMenuDomestico){
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();


            //Guardo el string en las sharedpreferences si esta guardado si el usuario tiene activada la opción
            if(filtroGuardado) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filtroguardado", TipoTarea);
                editor.apply();
            }

            consulta.OrdenarPorTipoTarea(TipoTarea, listaTareas, Adaptador);

        }
        if(item.getItemId()==R.id.opcMenuOcio){
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();
           // consulta.removerEventListener(1);

            //Guardo el string en las sharedpreferences si esta guardado si el usuario tiene activada la opción
            if(filtroGuardado) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filtroguardado", TipoTarea);
                editor.apply();
            }


            consulta.OrdenarPorTipoTarea(TipoTarea, listaTareas, Adaptador);

        }

        if(item.getItemId()==R.id.opcCompletas) {

            cntrTareas consulta = new cntrTareas();
            String TareasCompletadas =  item.getTitle().toString();

            if(filtroGuardado) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filtroguardado", TareasCompletadas);
                editor.apply();
            }

            consulta.ordenarPorCompletadas(listaTareas, Adaptador);



        }

        if(item.getItemId()==R.id.opcNoFiltrar) {
            cntrTareas consulta = new cntrTareas();
            String TipoTarea =  item.getTitle().toString();


            if(filtroGuardado) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filtroguardado", TipoTarea);
                editor.apply();
            }
            consulta.retornarListaTareas(new ListenersCallBack() {
                @Override
                public void listenerObtenido(ValueEventListener listener) {

                    consulta.removerEventListener(listenerglobal);
                    listenerglobal = listener;


                }
            },new TareasCallBack() {
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

