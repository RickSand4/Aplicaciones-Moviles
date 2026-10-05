package com.upiiz.practica_whatsapp.util;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.upiiz.practica_whatsapp.R;

public class Pantalla {
    // El mismo ajuste de barras utilizado en las actividades iniciales.
    public static void ajustarBarras(AppCompatActivity actividad) {
        EdgeToEdge.enable(actividad);
        ViewCompat.setOnApplyWindowInsetsListener(actividad.findViewById(R.id.main),(vista,insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            vista.setPadding(barras.left,barras.top,barras.right,barras.bottom);
            return insets;
        });
        WindowCompat.getInsetsController(actividad.getWindow(),actividad.findViewById(R.id.main))
                .setAppearanceLightStatusBars(true);
        WindowCompat.getInsetsController(actividad.getWindow(),actividad.findViewById(R.id.main))
                .setAppearanceLightNavigationBars(true);
    }
}
