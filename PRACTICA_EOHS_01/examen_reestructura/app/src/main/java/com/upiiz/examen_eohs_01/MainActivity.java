package com.upiiz.examen_eohs_01;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    //1. Declarar variables
    EditText etUsuario, etClave;
    Button btnIngresar, btnCrearCuenta;
    TextInputLayout tilUsuario, tilClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //2. Enlazar variables con las vistas
        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
        tilUsuario = findViewById(R.id.tilUsuario);
        tilClave = findViewById(R.id.tilClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        //3. Escuchar los clicks
        btnIngresar.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
        etClave.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnIngresar) {
            validarUsuario();
        } else if (id == R.id.btnCrearCuenta) {
            abrirRegistro();
        }
    }

    public void validarUsuario() {
        String usuario = etUsuario.getText().toString();
        String clave = etClave.getText().toString();
        tilUsuario.setError(null);
        tilClave.setError(null);

        if (usuario.isEmpty()) {
            tilUsuario.setError(getString(R.string.required_user));
            etUsuario.requestFocus();
        } else if (usuario.equals("admin") && clave.equals("123456")) {
            etClave.setText("");
            Intent intent = new Intent(this, CustomListViewActivity.class);
            startActivity(intent);
        } else {
            tilClave.setError(getString(R.string.invalid_login));
            etClave.requestFocus();
        }
    }

    public void abrirRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            validarUsuario();
            return true;
        }
        return false;
    }
}
