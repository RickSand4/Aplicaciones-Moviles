package com.upiiz.dm_eohs_07;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room3.Room;
import com.upiiz.dm_eohs_07.data.db.AppDataBase;
import com.upiiz.dm_eohs_07.data.db.Migraciones;
import com.upiiz.dm_eohs_07.data.entities.Mascota;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MascotaFormActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDataBase db;
    private long usuarioId, mascotaId = -1;
    private EditText nombre, raza, edad;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_mascota_form);
        usuarioId = getSharedPreferences("sesion", MODE_PRIVATE).getLong("usuario_id", -1);
        if (usuarioId < 0) { finish(); return; }
        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "veterinaria").addMigrations(Migraciones.VERSION_1_A_2, Migraciones.VERSION_2_A_3).build();
        nombre = findViewById(R.id.nombre); raza = findViewById(R.id.raza); edad = findViewById(R.id.edad);
        mascotaId = getIntent().getLongExtra("mascota_id", -1);
        boolean editar = mascotaId >= 0;
        ((android.widget.TextView)findViewById(R.id.titulo)).setText(editar ? "Actualizar mascota" : "Nueva mascota");
        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardar(editar));
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        if (editar) cargar();
    }
    private void cargar() { executor.execute(() -> { Mascota m = db.mascotaDAO().obtenerPorId(mascotaId, usuarioId); runOnUiThread(() -> { if (m == null) { Toast.makeText(this, "No se encontró la mascota", Toast.LENGTH_SHORT).show(); finish(); } else { nombre.setText(m.getNombre()); raza.setText(m.getRaza()); edad.setText(String.valueOf(m.getEdad())); } }); }); }
    private void guardar(boolean editar) {
        String n = nombre.getText().toString().trim(), r = raza.getText().toString().trim(), e = edad.getText().toString().trim();
        if (n.isEmpty() || r.isEmpty() || e.isEmpty()) { Toast.makeText(this, "Completa nombre, raza y edad", Toast.LENGTH_SHORT).show(); return; }
        int years;
        try { years = Integer.parseInt(e); } catch (NumberFormatException ex) { Toast.makeText(this, "La edad debe ser un número", Toast.LENGTH_SHORT).show(); return; }
        if (years < 0 || years > 100) { Toast.makeText(this, "Ingresa una edad entre 0 y 100", Toast.LENGTH_SHORT).show(); return; }
        executor.execute(() -> {
            boolean duplicada = db.mascotaDAO().contarNombre(usuarioId, n, editar ? mascotaId : -1) > 0;
            if (duplicada) { runOnUiThread(() -> Toast.makeText(this, "Ya tienes una mascota con ese nombre", Toast.LENGTH_SHORT).show()); return; }
            if (editar) db.mascotaDAO().actualizar(new Mascota(mascotaId, n, r, years, usuarioId));
            else db.mascotaDAO().crear(new Mascota(null, n, r, years, usuarioId));
            runOnUiThread(() -> { Toast.makeText(this, editar ? "Mascota actualizada" : "Mascota agregada", Toast.LENGTH_SHORT).show(); finish(); });
        });
    }
    @Override protected void onDestroy() { super.onDestroy(); executor.shutdown(); }
}




