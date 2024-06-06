package com.tfg.gestiondetareas.Vista;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;
import android.content.Intent;

import com.tfg.gestiondetareas.R;

public class splashscreenactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el modo oscuro antes de llamar a super.onCreate()
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean modoOscuroActivado = preferences.getBoolean("modo_oscuro", false);
        aplicarModoOscuro(modoOscuroActivado);



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreenactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clayVistaPrincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Arrancamos la siguiente actividad
                Intent mainIntent = new Intent().setClass(splashscreenactivity.this, MainActivity.class);
                startActivity(mainIntent);
                // Cerramos esta actividad para que el usuario no pueda volver a ella mediante botón de volver
                finish();
            }
        };

        // Simulamos un tiempo en el proceso de carga durante el cual mostramos el SplashScreen
        Timer timer = new Timer();
        timer.schedule(task, 2000); // Tiempo de espera del temporizador en milisegundos (2 sg)
    }

    private void aplicarModoOscuro(boolean activado) {
        if (activado) {
            // Si el modo oscuro está activado, establece el tema oscuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getDelegate().applyDayNight();


        } else {
            // Si el modo oscuro está desactivado, establece el tema predeterminado
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getDelegate().applyDayNight();
        }
    }



}