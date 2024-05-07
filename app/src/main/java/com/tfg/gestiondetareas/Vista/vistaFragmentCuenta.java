package com.tfg.gestiondetareas.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.cntrAjustes;

public class vistaFragmentCuenta extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cntrAjustes ajustes = new cntrAjustes();
        return inflater.inflate(R.layout.layout_cuenta, container, false);

    }


}
