package com.tfg.gestiondetareas.Vista;

import android.os.Bundle;
import android.view.View;
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

public class registrarCuenta extends AppCompatActivity {



    Button btnRegistrar, btnCancelar;
    EditText usuarioNuevo, contraseniaNueva, correoNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarComponentes();

        //Si cancela el registro vuelve a la anterior Activity
        btnCancelar.setOnClickListener(v -> finish());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si cualquiera de los dos EditText estan vacios, se le avisará mediante un toast para que rellene todos los campos
                if (usuarioNuevo.getText().toString().isEmpty() || contraseniaNueva.getText().toString().isEmpty() || correoNuevo.getText().toString().isEmpty()) {
                    Toast.makeText(registrarCuenta.this, R.string.toastRegIncorrecto, Toast.LENGTH_LONG).show();
                } else {
                    // Almacena los datos escritos en los EditText
                    String nombreusu = usuarioNuevo.getText().toString();
                    String contraseniausu = contraseniaNueva.getText().toString();
                    String correousu=correoNuevo.getText().toString();

                    cntrRegistrarCuentas registrar= new cntrRegistrarCuentas(registrarCuenta.this);

                    //Metodo que se encargará de crear la nueva cuenta
                    registrar.registrarnuevoUsuario(correousu, contraseniausu, nombreusu);




                }
            }
        });
    }

    public void inicializarComponentes() {
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnCancelar = findViewById(R.id.btnCancelar);
        usuarioNuevo = findViewById(R.id.edtRegNom);
        contraseniaNueva = findViewById(R.id.edtCon);
        correoNuevo = findViewById(R.id.edtCorreo);
    }


}