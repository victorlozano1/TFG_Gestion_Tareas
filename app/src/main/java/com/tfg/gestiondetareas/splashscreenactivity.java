package com.tfg.gestiondetareas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.TimerTask;
import java.util.Timer;
import android.content.Intent;

public class splashscreenactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreenactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
    }
