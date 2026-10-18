package com.upiiz.examen_eohs_01;

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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    //1. Declarar variables
    EditText etNombre, etCorreo, etUsuario, etClave, etConfirmarClave;
    Button btnRegistrar, btnTengoCuenta;
    TextInputLayout tilNombre, tilClave, tilConfirmarClave;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //2. Enlazar variables con las vistas
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        etConfirmarClave = findViewById(R.id.etConfirmarClave);
        tilNombre = findViewById(R.id.tilNombre);
        tilClave = findViewById(R.id.tilClave);
        tilConfirmarClave = findViewById(R.id.tilConfirmarClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnTengoCuenta = findViewById(R.id.btnTengoCuenta);
        toolbar = findViewById(R.id.toolbar);

        //3. Escuchar los clicks
        btnRegistrar.setOnClickListener(this);
        btnTengoCuenta.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegistrar) {
            registrarUsuario();
        } else {
            regresar();
        }
    }

    public void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String clave = etClave.getText().toString();
        String confirmarClave = etConfirmarClave.getText().toString();
        boolean datosCorrectos = true;

        tilNombre.setError(null);
        tilClave.setError(null);
        tilConfirmarClave.setError(null);

        if (nombre.length() < 3) {
            tilNombre.setError(getString(R.string.invalid_name));
            etNombre.requestFocus();
            datosCorrectos = false;
        }
        if (clave.trim().isEmpty()) {
            tilClave.setError(getString(R.string.required_password));
            datosCorrectos = false;
        }
        if (!clave.equals(confirmarClave) || confirmarClave.trim().isEmpty()) {
            tilConfirmarClave.setError(getString(R.string.password_mismatch));
            datosCorrectos = false;
        }

        if (datosCorrectos) {
            //El registro es simulado. El acceso del examen sigue siendo admin / 123456.
            Toast.makeText(this, R.string.registration_success, Toast.LENGTH_LONG).show();
            regresar();
        }
    }

    public void regresar() {
        //Cerrar el registro para volver al login que lo abrió.
        finish();
    }
}
