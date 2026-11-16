package com.upiiz.dm_eohs_practica_ii.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.dm_eohs_practica_ii.R;
import com.upiiz.dm_eohs_practica_ii.utils.Preferencias;

public class WelcomeActivity extends AppCompatActivity {
    private TextInputLayout tilUsuario;
    private TextInputEditText etUsuario;
    private TextView tvGreeting;
    private TextView tvUltimaSeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilUsuario = findViewById(R.id.tilUsuario);
        etUsuario = findViewById(R.id.etUsuario);
        tvGreeting = findViewById(R.id.tvGreeting);
        MaterialSwitch switchTema = findViewById(R.id.switchTema);
        tvUltimaSeccion = findViewById(R.id.tvUltimaSeccion);

        etUsuario.setText(Preferencias.obtenerUsuario(this));
        actualizarBienvenida();
        etUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarBienvenida();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        switchTema.setChecked(Preferencias.esTemaOscuro(this));
        actualizarUltimaSeccion();

        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Preferencias.guardarTemaOscuro(this, isChecked);
            Preferencias.aplicarTema(this);
        });

        findViewById(R.id.btnCatalogo).setOnClickListener(v -> abrirSeccion(Preferencias.SECCION_CATALOGO));
        findViewById(R.id.btnFavoritos).setOnClickListener(v -> abrirSeccion(Preferencias.SECCION_FAVORITOS));
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarBienvenida();
        actualizarUltimaSeccion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String nombre = etUsuario.getText() != null ? etUsuario.getText().toString().trim() : "";
        if (!nombre.isEmpty()) {
            Preferencias.guardarUsuario(this, nombre);
        }
    }

    private void abrirSeccion(String seccion) {
        String nombre = etUsuario.getText() != null ? etUsuario.getText().toString().trim() : "";
        if (nombre.isEmpty()) {
            tilUsuario.setError(getString(R.string.welcome_name_required));
            return;
        }
        tilUsuario.setError(null);
        Preferencias.guardarUsuario(this, nombre);
        Preferencias.guardarUltimaSeccion(this, seccion);

        Class<?> destino = Preferencias.SECCION_FAVORITOS.equals(seccion)
                ? FavoritesActivity.class
                : CatalogActivity.class;
        startActivity(new Intent(this, destino));
    }

    private void actualizarUltimaSeccion() {
        String seccion = Preferencias.obtenerUltimaSeccion(this);
        int texto = Preferencias.SECCION_FAVORITOS.equals(seccion)
                ? R.string.welcome_last_section_favorites
                : R.string.welcome_last_section_catalog;
        tvUltimaSeccion.setText(texto);
    }

    private void actualizarBienvenida() {
        String nombre = etUsuario.getText() != null ? etUsuario.getText().toString().trim() : "";
        tvGreeting.setVisibility(nombre.isEmpty() ? TextView.GONE : TextView.VISIBLE);
        if (!nombre.isEmpty()) {
            tvGreeting.setText(getString(R.string.welcome_greeting, nombre));
        }
    }
}
