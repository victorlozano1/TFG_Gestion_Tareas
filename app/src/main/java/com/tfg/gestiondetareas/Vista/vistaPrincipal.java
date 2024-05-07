package com.tfg.gestiondetareas.Vista;

import static android.app.UiModeManager.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.cntrAjustes;

public class vistaPrincipal extends AppCompatActivity {


    private cntrAjustes ajustesFragment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_vista_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clayVistaPrincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //BottomNavigationView para la vista principal

        BottomNavigationView bottom_navigation = findViewById(R.id.NavegadorVistaPrincipal);


        //Evento que servirá para navegar entre fragments
        bottom_navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                if (Item.getItemId() == R.id.opcTareas)
                    CargarFragment(new vistaFragmentTareas());
                if (Item.getItemId() == R.id.opcCuenta)
                    CargarFragment(new vistaFragmentCuenta());
                if (Item.getItemId() == R.id.opcAjustes) {
                    // Mostrar el fragmento de ajustes existente si está disponible
                    if (ajustesFragment == null) {
                        ajustesFragment = new cntrAjustes();
                    }
                    CargarFragment(ajustesFragment);
                }
                return true;
            }
        });
        CargarFragment(new vistaFragmentTareas());


    }

    //Método que se encarga de cambiar de fragment
    private void CargarFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }


}



