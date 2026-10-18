package com.upiiz.practica_whatsapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LlamadasActivity extends AppCompatActivity {

    // 1. Declarar variables
    Button btnRegresar, btnMas;
    LinearLayout llFavoritos, llRecientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        // 2. Cargar el diseño de esta pantalla
        setContentView(R.layout.activity_llamadas);

        // Evitar que el contenido quede debajo de las barras del teléfono
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

        // 3. Enlazar las variables con las vistas del XML
        btnRegresar = findViewById(R.id.btnRegresar);
        btnMas = findViewById(R.id.btnMas);

        llFavoritos = findViewById(R.id.llFavoritos);
        llRecientes = findViewById(R.id.llRecientes);

        // 4. Acción del botón Regresar
        btnRegresar.setOnClickListener(v -> finish());
    }
}