package com.tfg.gestiondetareas.Vista;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg.gestiondetareas.Modelo.Tarea;
import com.tfg.gestiondetareas.R;

import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    ArrayList<Tarea> listaTareas;
    Context contexto;

    public TareaAdapter(ArrayList<Tarea> listaTareas, Context contexto) {
        this.listaTareas = listaTareas;
        this.contexto=contexto;
    }

    @NonNull
    @Override
    public TareaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_tareas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaAdapter.ViewHolder holder, int position) {

        holder.Tarea.setText(listaTareas.get(position).getNombre());
        holder.Fecha.setText(listaTareas.get(position).getFecha_publicacion());
        holder.publicador.setText(listaTareas.get(position).getNombre_publicador());


    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Tarea, Fecha, publicador;
        CheckBox completado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tarea=itemView.findViewById(R.id.tvNomTarea);
            Fecha=itemView.findViewById(R.id.tvDesc);
            publicador=itemView.findViewById(R.id.tvPublicador);
            completado=itemView.findViewById(R.id.chCompleto);

        }
    }
}
