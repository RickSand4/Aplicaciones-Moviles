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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDataBase db;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        if (getSharedPreferences("sesion", MODE_PRIVATE).contains("usuario_id")) {
            abrirMascotas();
            return;
        }
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "veterinaria").addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3).build();
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button entrar = findViewById(R.id.btnEntrar);
        Button registro = findViewById(R.id.btnRegistro);
        entrar.setOnClickListener(v -> {
            String correo = email.getText().toString().trim().toLowerCase();
            String clave = password.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches() || clave.isEmpty()) {
                Toast.makeText(this, "Ingresa un correo válido y tu contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
            executor.execute(() -> {
                com.upiiz.dm_eohs_07.data.entities.Usuario user = db.usuarioDAO().buscarPorCorreo(correo);
                runOnUiThread(() -> {
                    if (user == null || !user.getPassword().equals(clave)) {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    } else {
                        getSharedPreferences("sesion", MODE_PRIVATE).edit().putLong("usuario_id", user.getId()).putString("usuario_nombre", user.getNombre()).apply();
                        abrirMascotas();
                    }
                });
            });
        });
        registro.setOnClickListener(v -> startActivity(new Intent(this, RegistroActivity.class)));
    }

    private void abrirMascotas() { startActivity(new Intent(this, MascotasActivity.class)); finish(); }
    @Override protected void onDestroy() { super.onDestroy(); executor.shutdown(); }
}




