package com.tfg.gestiondetareas.controlador;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.tfg.gestiondetareas.R;

public class cntrAjustes extends PreferenceFragmentCompat {




    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferencias, rootKey);

        // Obtiene la preferencia de modo oscuro
        SwitchPreference modoOscuroPreference = findPreference("modo_oscuro");
        if (modoOscuroPreference != null) {
            // Agrega un listener para detectar cambios en la preferencia de modo oscuro
            modoOscuroPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean modoOscuroActivado = (boolean) newValue;
                // Aplica el modo oscuro en toda la aplicación
                aplicarModoOscuro(modoOscuroActivado);
                return true; // Indica que el cambio fue manejado correctamente
            });
        }

        SwitchPreference mantenerOpcionesFiltrado = findPreference("filtrado_activo");

        if(mantenerOpcionesFiltrado != null) {

            mantenerOpcionesFiltrado.setOnPreferenceChangeListener(((preference, newValue) -> {
                boolean opcActivada = (boolean) newValue;
                mantenerOpcionesFiltrado(opcActivada);
                return true;
            }));

        }
    }

    private void aplicarModoOscuro(boolean activado) {
        Context contexto = getContext().getApplicationContext();

        // Guardar el estado del modo oscuro en SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("modo_oscuro", activado);
        editor.apply();
        Toast.makeText(contexto , R.string.ReiniciarApp, Toast.LENGTH_SHORT).show();

    }

    private void mantenerOpcionesFiltrado(boolean activado) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("mantenerfiltro", activado);
        editor.apply();

    }

    }
