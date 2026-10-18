package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChatActivity extends AppCompatActivity
        implements View.OnClickListener {

    // 1. Declarar variables
    TextView tvNombreChat;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            barras.left,
                            barras.top,
                            barras.right,
                            barras.bottom
                    );

                    return insets;
                }
        );

        // 2. Enlazar las vistas
        tvNombreChat = findViewById(R.id.tvNombreChat);
        btnRegresar = findViewById(R.id.btnRegresar);

        // 3. Recuperar el nombre recibido
        String nombre = getIntent()
                .getStringExtra("nombre_usuario");

        // Mostrarlo si el extra existe
        if (nombre != null) {
            tvNombreChat.setText(nombre);
        }

        // 4. Escuchar el botón
        btnRegresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegresar) {
            finish();
        }
    }
}