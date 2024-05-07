package com.tfg.gestiondetareas.Vista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    public void onBindViewHolder(@NonNull TareaAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.Tarea.setText(listaTareas.get(position).getNombre());
        holder.Fecha.setText(listaTareas.get(position).getFecha_publicacion());
        holder.publicador.setText(listaTareas.get(position).getNombre_publicador());

        //Cuando pulsa click sobre un cardview, se dirige a una activity donde muestra en detalle la tarea



        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Crea un nuevo intent para ir a la activity
                Intent intent = new Intent(contexto, DetalleTareaActivity.class);
                //Almacena los datos de la tarea
                intent.putExtra("NombreTareaDetalle", listaTareas.get(position).getNombre());
                intent.putExtra("DescripcionTareaDetalle", listaTareas.get(position).getDescripcion());
                intent.putExtra("FechaPublicacionDetalle", listaTareas.get(position).getFecha_publicacion());
                intent.putExtra("NombrePublicadorDetalle",listaTareas.get(position).getNombre_publicador());
                contexto.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Tarea, Fecha, publicador;
        CheckBox completado;
        ConstraintLayout cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tarea=itemView.findViewById(R.id.tvNomTarea);
            Fecha=itemView.findViewById(R.id.tvFecha);
            publicador=itemView.findViewById(R.id.tvPublicador);
            cv=itemView.findViewById(R.id.clayCardView);

        }
    }
}
