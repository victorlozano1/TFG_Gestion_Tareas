package com.tfg.gestiondetareas.Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.tfg.gestiondetareas.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class splashscreenactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreenactivity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clayVistaPrincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(splashscreenactivity.this);
        boolean modoOscuroActivado = preferences.getBoolean("modo_oscuro", false);
        String idioma = preferences.getString("listaIdiomas", "no_idioma");
        CambiarIdioma(idioma);
        aplicarModoOscuro(modoOscuroActivado);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {


                // Arrancamos la siguiente actividad
                Intent mainIntent = new Intent().setClass(splashscreenactivity.this, MainActivity.class);
                startActivity(mainIntent);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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


        } else {
            // En caso contrario establece el tema por defecto
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

    }

    private void CambiarIdioma(String idioma) {
        if (!idioma.equals("no_idioma")) {
            Locale locale;
            if (idioma.equals("Español") || idioma.equals("Spanish")) {
                locale = new Locale("es");
            } else {
                locale = new Locale("en");
            }

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        }

    }
}