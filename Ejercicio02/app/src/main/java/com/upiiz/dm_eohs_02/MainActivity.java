package com.upiiz.dm_eohs_02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //1. Declarar variables
    EditText etNombre;
    TextView tvSaludo;
    Button btnSaludo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Revisar siempre que corresponda la vista con lo que se carga.
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Nullpointer
        //2. Enlazar variables con vistas (layout, button, textview, etc)
        etNombre = findViewById(R.id.etNombre);
        tvSaludo = findViewById(R.id.tvSaludo);
        btnSaludo = findViewById(R.id.btnSaludo);
        //3. Desarrollo
        //3.1 el boton que escuche click o touch
        btnSaludo.setOnClickListener((View.OnClickListener) this);

    }
}