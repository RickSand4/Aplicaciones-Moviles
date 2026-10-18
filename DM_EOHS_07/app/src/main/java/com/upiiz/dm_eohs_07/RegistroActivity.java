package com.upiiz.dm_eohs_07;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room3.Room;
import com.upiiz.dm_eohs_07.data.db.AppDataBase;
import com.upiiz.dm_eohs_07.data.db.Migraciones;
import com.upiiz.dm_eohs_07.data.entities.Usuario;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDataBase db;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_registro);
        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "veterinaria").addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3).build();
        EditText nombre = findViewById(R.id.nombre);
        EditText correo = findViewById(R.id.email);
        EditText clave = findViewById(R.id.password);
        findViewById(R.id.btnCrearCuenta).setOnClickListener(v -> {
            String n = nombre.getText().toString().trim();
            String e = correo.getText().toString().trim().toLowerCase();
            String p = clave.getText().toString();
            if (n.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e).matches() || p.length() < 6) {
                Toast.makeText(this, "Completa los campos. Usa un correo válido y una contraseña de 6 caracteres o más.", Toast.LENGTH_LONG).show();
                return;
            }
            executor.execute(() -> {
                boolean creado = false;
                String mensaje;
                try {
                    if (db.usuarioDAO().buscarPorCorreo(e) != null) {
                        mensaje = "Ya existe una cuenta con ese correo.";
                    } else {
                        long id = db.usuarioDAO().crear(new Usuario(n, e, p));
                        getSharedPreferences("sesion", MODE_PRIVATE).edit().putLong("usuario_id", id).putString("usuario_nombre", n).apply();
                        creado = true;
                        mensaje = "Cuenta creada.";
                    }
                } catch (Exception ex) { mensaje = "No se pudo crear la cuenta. Intenta de nuevo."; }
                final boolean ok = creado;
                final String resultado = mensaje;
                runOnUiThread(() -> {
                    Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
                    if (ok) { startActivity(new Intent(this, MascotasActivity.class)); finish(); }
                });
            });
        });
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }
    @Override protected void onDestroy() { super.onDestroy(); executor.shutdown(); }
}




