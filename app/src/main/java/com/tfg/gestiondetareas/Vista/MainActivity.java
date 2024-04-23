package com.tfg.gestiondetareas.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tfg.gestiondetareas.R;
import com.tfg.gestiondetareas.controlador.cntrRegistrarCuentas;

public class MainActivity extends AppCompatActivity {


    Button btncrear_cuenta, btncerrarApp, btniniciarsesion;
    EditText correo, contrasenia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarcomponentes();
        //Si pulsa el boton cierra la aplicación
        btncerrarApp.setOnClickListener(v->finishAffinity());

        //Si pulsa este boton le redirigirá a la activity donde el usuario creará su cuenta
        btncrear_cuenta.setOnClickListener(v->AbrirActivityRegistro());

        btniniciarsesion.setOnClickListener(v->iniciarSesion());


    }




    //Método que inicializa los componentes de la aplicación
    public void inicializarcomponentes() {

        btncrear_cuenta=findViewById(R.id.btnCrearCuenta);
        btncerrarApp=findViewById(R.id.btnSalir);
        btniniciarsesion=findViewById(R.id.btnIniciarSesion);
        correo=findViewById(R.id.edtCorreoInit);
        contrasenia=findViewById(R.id.edtContr);


    }

    //Método que abre la activity donde se registra el usuario por primera vez
    private void AbrirActivityRegistro() {
        Intent registroIntent= new Intent(this, registrarCuenta.class);
        startActivity(registroIntent);

    }

    private void iniciarSesion() {
        cntrRegistrarCuentas sesion= new cntrRegistrarCuentas(this);
        if(correo.getText().toString().isEmpty() || contrasenia.getText().toString().isEmpty()) {

            Toast.makeText(this, R.string.toastRegIncorrecto, Toast.LENGTH_LONG);
        }

        else {

            String correoUsu=correo.getText().toString();
            String contraseniaUsu=contrasenia.getText().toString();
            sesion.iniciarSesion(correoUsu, contraseniaUsu);



        }

    }
}