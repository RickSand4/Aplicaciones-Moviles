package com.upiiz.dm_eohs_03;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etMetros, etPies;
    Button btnPies, btnMetros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMetros = findViewById(R.id.etMetros);
        etPies = findViewById(R.id.etPies);

        btnPies = findViewById(R.id.btnPies);
        btnMetros = findViewById(R.id.btnMetros);

        // Convertir metros a pies
        btnPies.setOnClickListener(v -> {

            String metrosTexto = etMetros.getText().toString();

            if (!metrosTexto.isEmpty()) {

                double metros = Double.parseDouble(metrosTexto);
                double pies = metros * 3.28084;

                etPies.setText(String.format("%.2f", pies));

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Ingrese la cantidad de metros",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Convertir pies a metros
        btnMetros.setOnClickListener(v -> {

            String piesTexto = etPies.getText().toString();

            if (!piesTexto.isEmpty()) {

                double pies = Double.parseDouble(piesTexto);
                double metros = pies * 0.3048;

                etMetros.setText(String.format("%.2f", metros));

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Ingrese la cantidad de pies",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}